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
