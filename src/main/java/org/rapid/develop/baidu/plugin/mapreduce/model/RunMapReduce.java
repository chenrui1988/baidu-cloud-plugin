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

package org.rapid.develop.baidu.plugin.mapreduce.model;



import org.gradle.model.Managed;
import org.gradle.model.ModelSet;

@Managed
public interface RunMapReduce {

    public String getName();

    public void setName(String name);

    public String getImageType();

    public void setImageType(String imageType);

    public String getImageVersion();

    public void setImageVersion(String imageVersion);

    public Boolean getAutoTerminate();

    public void setAutoTerminate(Boolean autoTerminate);

    public String getLogUri();

    public void setLogUri(String logUri);

    public Master getMaster();

    public ModelSet<Slave> getSlaves();

    public Pig getPig();

    public Hive getHive();

    public HBase getHbase();

    public ModelSet<Step> getSteps();

}
