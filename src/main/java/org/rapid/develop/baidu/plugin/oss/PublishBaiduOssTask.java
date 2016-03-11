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

package org.rapid.develop.baidu.plugin.oss;

import com.baidubce.services.bos.BosClient;
import org.rapid.develop.baidu.plugin.Baidu;
import org.rapid.develop.baidu.plugin.BaiduPlugin;
import org.rapid.develop.baidu.plugin.BaiduUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class PublishBaiduOssTask extends DefaultTask {

    private static Logger logger = LoggerFactory.getLogger(BaiduPlugin.class);

    private Baidu baidu;

    @TaskAction
    public void publish() {
        assertNotEmpty(this.baidu.getAccessKey(), "baidu.accessKey can not be null or empty!");
        assertNotEmpty(this.baidu.getSecretKey(), "baidu.secretKey can not be null or empty!");

        OSSPublish ossPublish = this.baidu.getOssPublish();
        if(ossPublish != null) {
            assertNotEmpty(ossPublish.getBucketName(), "bucketName can not be null or empty!");
            BosClient client = BaiduUtil.getBosClient(this.baidu.getAccessKey(), this.baidu.getSecretKey());

            if(ossPublish.getFiles()!= null) {
                for (File file : ossPublish.getFiles()) {
                    logger.info("begin publish {0} to baidu oss", file.getName());
                    BaiduUtil.publishFileToOSS(client, ossPublish.getBucketName(), this.getProject().getGroup().toString(),
                            this.getProject().getVersion().toString(), file);
                    logger.info("publish {0} to baidu oss success", file.getName());
                }
            }
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
