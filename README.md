###Baidu 云服务Gradle 插件


基于Gradle 开发的便于开发基于Baidu云服务的程序，插件采用Gradle Rule Base Model 形式开发

Baidu 云服务Gradle 插件支持 发布文件到OSS 和创建百度Hadoop集群并执行 Map-Reduce程序

使用示例代码：
```CoffeeScript
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.org.rapid.develop:baidu-cloud-plugin:1.1"
  }
}

apply plugin: "org.rapid.develop.baidu-cloud-plugin"

model {
    baidu {
        accessKey = 'accessKey'
        secretKey = 'secretKey'
        // OSS 上传设置
        // 上传 文件到指定的bucketName
        ossPublish {
            bucketName = 'bucketName'
            files = [jar.archivePath, new File('../logs/accesslog-10k.log')]
        }
        // MapReduce 设置
        // 可创建Baidu Hadoop集群，并添加应用，并执行任务
        // 可执行多个任务
        mapReduce {
            name = project.name
            imageType = 'hadoop'
            imageVersion = '0.1.0'
            autoTerminate = false
            logUri = "bos://$bucketName/logs/"
            master {
                instanceType = 'g.small'
                instanceCount = 1
            }
            slaves.create {
                instanceType = 'g.small'
                instanceCount = 2
            }
            steps.create {
                name = "$project.name-$project.version"
                actionOnFailure = 'Continue'
                mainClass = 'com.vianet.cie.hadoop.AccessLogAnalyzer'
                jar = "bos://$bucketName/$project.group/$project.version/$project.name-${project.version}.jar"
                arguments = "bos://$bucketName/$project.group/$project.version/accesslog-10k.log bos://$bucketName/out"
            }
        }
    }
}
```
