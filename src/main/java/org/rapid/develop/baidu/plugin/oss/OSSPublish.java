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


import org.gradle.api.Incubating;
import org.gradle.model.Managed;

import java.io.File;
import java.util.List;

@Managed
public interface OSSPublish {

    public String getBucketName();

    public void setBucketName(String bucketName);

    public List<File> getFiles();

    public void setFiles(List<File> files);

}
