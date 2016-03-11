/*
 *
 *  * Copyright (c) 2015-2020, Chen Rui
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.rapid.develop.baidu.plugin;


import com.baidubce.BceClientConfiguration;
import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.auth.BceCredentials;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bmr.BmrClient;
import com.baidubce.services.bmr.model.*;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.PutObjectResponse;
import org.apache.commons.lang.StringUtils;
import org.gradle.model.ModelSet;
import org.rapid.develop.baidu.plugin.mapreduce.model.*;

import java.io.File;
import java.util.function.Consumer;

public class BaiduUtil {

    public static BceCredentials getCredentials(String access, String secret) {
        BceCredentials credentials = new DefaultBceCredentials(access, secret);
        return credentials;
    }

    public static void publishFileToOSS(BosClient client, String bucketName, String group, String version, File file) {
        checkAndCreateBucketIfNotExsit(client, bucketName);

        String key = group + "/" + version + "/" + file.getName();
        try {
            BosObject bosObject = client.getObject(bucketName, key);
            if(bosObject != null) {
                client.deleteObject(bucketName, key);
            }
        } catch (BceServiceException e) {

        }
        PutObjectResponse response = client.putObject(bucketName, key, file);
    }

    public static BosClient getBosClient(String accessKey, String secretKey) {
        BosClientConfiguration bosconfig = new BosClientConfiguration();
        bosconfig.setCredentials(BaiduUtil.getCredentials(accessKey, secretKey));
        BosClient client = new BosClient(bosconfig);
        return client;
    }


    public static void checkAndCreateBucketIfNotExsit(BosClient client, String bucketName) {
        boolean exist = client.doesBucketExist(bucketName);
        if(!exist) {
            client.createBucket(bucketName);
        }
    }

    public static void runMapReduce(String accessKey, String secretKey, RunMapReduce mapReduce) {

        BceClientConfiguration config = new BceClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(accessKey, secretKey));
        BmrClient client = new BmrClient(config);


        Master master = mapReduce.getMaster();
        final ModelSet<Slave> slaves = mapReduce.getSlaves();
        ModelSet<org.rapid.develop.baidu.plugin.mapreduce.model.Step> steps = mapReduce.getSteps();
        Pig pig = mapReduce.getPig();
        HBase hbase = mapReduce.getHbase();
        Hive hive = mapReduce.getHive();

        String clusterId = null;
        try {
            final CreateClusterRequest request = new CreateClusterRequest()
                    .withName(mapReduce.getName())
                    .withImageType(mapReduce.getImageType())
                    .withImageVersion(mapReduce.getImageVersion())
                    .withAutoTerminate(mapReduce.getAutoTerminate())
                    .withLogUri(mapReduce.getLogUri())
                    .withInstanceGroup(new InstanceGroupConfig()
                            .withName("ig-master")
                            .withType("Master")
                            .withInstanceType(master.getInstanceType())
                            .withInstanceCount(master.getInstanceCount()));

            slaves.forEach(new Consumer<Slave>() {
                @Override
                public void accept(Slave slave) {
                    request.withInstanceGroup(new InstanceGroupConfig()
                            .withName("ig-core")
                            .withType("Core")
                            .withInstanceType(slave.getInstanceType())
                            .withInstanceCount(slave.getInstanceCount()));
                }
            });

            steps.forEach(new Consumer<org.rapid.develop.baidu.plugin.mapreduce.model.Step>() {
                @Override
                public void accept(org.rapid.develop.baidu.plugin.mapreduce.model.Step step) {
                    if(step.getType().equals("pig")) {
                        request.withStep(new PigStepConfig()
                                .withName(step.getName())
                                .withActionOnFailure(step.getActionOnFailure())
                                .withScript(step.getJar())
                                .withArguments(step.getArguments()));
                    } else if(step.getType().equals("hive")) {
                        request.withStep(new HiveStepConfig()
                                .withName(step.getName())
                                .withActionOnFailure(step.getActionOnFailure())
                                .withScript(step.getJar())
                                .withArguments(step.getArguments()));
                    } else {
                        request.withStep(new JavaStepConfig()
                                .withName(step.getName())
                                .withActionOnFailure(step.getActionOnFailure())
                                .withJar(step.getJar())
                                .withMainClass(step.getMainClass())
                                .withArguments(step.getArguments()));
                    }
                }
            });

            if(StringUtils.isNotEmpty(pig.getVersion())) {
                request.withApplication(new PigApplicationConfig().withVersion(pig.getVersion()));
            }
            if(StringUtils.isNotEmpty(hive.getVersion())) {
                request.withApplication(new HiveApplicationConfig().withVersion(hive.getVersion()).withMetastore(hive.getMetastore()));
            }
            if(StringUtils.isNotEmpty(hbase.getVersion())) {
                request.withApplication(new HBaseApplicationConfig()
                        .withVersion(hbase.getVersion())
                        .withBackupEnabled(hbase.getBackbupEnabled())
                        .withBackupLocation(hbase.getBackupLocation())
                        .withBackupIntervalInMinutes(hbase.getBackupIntervalInMinutes())
                        .withBackupStartDatetime(hbase.getBackupStartDatetime()));
            }

            CreateClusterResponse response = client.createCluster(request);
            clusterId = response.getClusterId();
        } catch (BceServiceException e) {
            System.out.println("Create cluster failed: " + e.getErrorMessage());
        } catch (BceClientException e) {
            System.out.println(e.getMessage());
        }
    }
}
