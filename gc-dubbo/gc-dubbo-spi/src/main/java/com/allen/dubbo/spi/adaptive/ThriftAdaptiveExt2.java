package com.allen.dubbo.spi.adaptive;


import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;


/**
 * @Author Allen 2021/4/16 20:40
 **/
@Adaptive
public class ThriftAdaptiveExt2 implements AdaptiveExt2 {
    @Override
    public String echo(String msg, URL url) {
        System.out.printf("--------ThriftAdaptiveExt2---------");
        return "thrift";
    }
}
