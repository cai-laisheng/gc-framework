## gc log
就是在gc时打印出来的日志，关于这一块的分析，对于问题定位也是非常有用的。这里gclog的垃圾回收器是ParallerGC

在jvm启动参数加上以下，可以开启gc log,配置gclog的输出位置

-XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:/usr/local/project/jvmtest/gc.log

gc log分析，借用网上的一张图片

![](https://img-blog.csdnimg.cn/20190123233709829.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIwNTk3NzI3,size_16,color_FFFFFF,t_70)

Minior GC:

[GC (Metadata GC Threshold) [PSYoungGen: 174124K->17212K(298496K)] 174124K->17244K(981504K), 0.0215908 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]

Full GC:

[Full GC (Metadata GC Threshold) [PSYoungGen: 17212K->0K(298496K)] [ParOldGen: 32K->16210K(683008K)] 17244K->16210K(981504K), [Metaspace: 20399K->20399K(1069056K)], 0.0197120 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 

## dump文件生成
JVM会在遇到OutOfMemoryError时拍摄一个“堆转储快照”，并将其保存在一个文件中。

1、配置方法 在JAVA_OPTIONS变量中增加 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${目录}。

2、参数说明

（1）-XX:+HeapDumpOnOutOfMemoryError参数表示当JVM发生OOM时，自动生成DUMP文件。

（2）-XX:HeapDumpPath=${目录}参数表示生成DUMP文件的路径，也可以指定文件名称，例如：-XX:HeapDumpPath=${目录}/java_heapdump.hprof。如果不指定文件名，默认为：java_<pid>_<date>_<time>_heapDump.hprof。

