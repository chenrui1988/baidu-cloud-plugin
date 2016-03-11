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

@Managed
public interface HBase extends Application {

    public Boolean getBackbupEnabled();

    public void setBackbupEnabled(Boolean backbupEnabled);

    public String getBackupLocation();

    public void setBackupLocation(String backupLocation);

    public Integer getBackupIntervalInMinutes();

    public void setBackupIntervalInMinutes(Integer backupIntervalInMinutes);

    public String getBackupStartDatetime();

    public void setBackupStartDatetime(String backupStartDatetime);

    public String getRestoreEnabled();

    public void setRestoreEnabled(String restoreEnabled);

    public String getRestoreLocation();

    public void setRestoreLocation(String restoreLocation);

    public String getRestoreVersion();

    public void setRestoreVersion(String restoreVersion);

}
