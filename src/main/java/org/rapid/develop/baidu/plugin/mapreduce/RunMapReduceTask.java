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

package org.rapid.develop.baidu.plugin.mapreduce;


import org.rapid.develop.baidu.plugin.Baidu;
import org.rapid.develop.baidu.plugin.BaiduUtil;
import org.rapid.develop.baidu.plugin.mapreduce.model.Master;
import org.rapid.develop.baidu.plugin.mapreduce.model.RunMapReduce;
import org.rapid.develop.baidu.plugin.mapreduce.model.Step;
import org.rapid.develop.baidu.plugin.oss.OSSPublish;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.model.ModelSet;
import org.rapid.develop.baidu.plugin.mapreduce.model.Slave;

import java.util.function.Consumer;

public class RunMapReduceTask extends DefaultTask {

    private Baidu baidu;

    @TaskAction
    public void run() {
        String accessKey = this.baidu.getAccessKey();
        String secretKey = this.baidu.getSecretKey();
        assertNotEmpty(accessKey, "baidu.accessKey can not be null or empty!");
        assertNotEmpty(secretKey, "baidu.secretKey can not be null or empty!");

        final OSSPublish ossExtension = this.baidu.getOssPublish();
        assertNotEmpty(ossExtension.getBucketName(), "OSSPublish.bucketName can not be null or empty!");

        RunMapReduce runMapReduce = this.baidu.getMapReduce();
        if(runMapReduce != null) {
            assertNotEmpty(runMapReduce.getName(), "mapreduce.name can not be null or empty!");
            assertNotEmpty(runMapReduce.getImageType(), "mapreduce.imageType can not be null or empty!");
            assertNotEmpty(runMapReduce.getImageVersion(), "mapreduce.imageVersion can not be null or empty!");

            Master master = runMapReduce.getMaster();
            assertNotEmpty(master.getInstanceType(), "mapreduce.master.instanceType can not be null or empty!");

            ModelSet<Slave> slaves = runMapReduce.getSlaves();
            slaves.forEach(new Consumer<Slave>() {
                @Override
                public void accept(Slave slave) {
                    assertNotEmpty(slave.getInstanceType(), "mapreduce.slaves.instanceType can not be null or empty!");
                }
            });

            ModelSet<Step> steps = runMapReduce.getSteps();
            steps.forEach(new Consumer<Step>() {
                @Override
                public void accept(Step step) {
                    assertNotEmpty(step.getMainClass(), "mapreduce.step.arguments can not be null or empty!");
                    assertNotEmpty(step.getArguments(), "mapreduce.step.arguments can not be null or empty!");
                    System.out.println(step.getJar());
                }
            });

            BaiduUtil.runMapReduce(accessKey, secretKey, runMapReduce);
        }
    }

    public void assertNotEmpty(String text, String exception) throws IllegalArgumentException {
        if(text == null || text.equals("")) {
            throw new IllegalArgumentException(exception);
        }
    }

    public Baidu getBaidu() {
        return baidu;
    }

    public void setBaidu(Baidu baidu) {
        this.baidu = baidu;
    }
}
