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


import org.rapid.develop.baidu.plugin.mapreduce.RunMapReduceTask;
import org.rapid.develop.baidu.plugin.oss.PublishBaiduOssTask;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.model.Model;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;


public class BaiduPlugin extends RuleSource {

    @Model
    public void baidu(Baidu baidu) {}

    @Mutate
    public void createBaiduTasks(ModelMap<Task> tasks, final Baidu baidu) {
        System.out.println("loading BaiduPlugin!");
        tasks.create("publishBaiduOss", PublishBaiduOssTask.class, new Action<PublishBaiduOssTask>() {
            @Override
            public void execute(PublishBaiduOssTask task) {
                task.setBaidu(baidu);
                task.dependsOn("jar");
            }
        });
        tasks.create("runMapReduceTask", RunMapReduceTask.class, new Action<RunMapReduceTask>() {
            @Override
            public void execute(RunMapReduceTask task) {
                task.setBaidu(baidu);
            }
        });
        tasks.get("runMapReduceTask").dependsOn("publishBaiduOss");
    }

}
