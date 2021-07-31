package com.allen.redisson.write;

import com.allen.redisson.config.CacheProvider;
import com.allen.redisson.po.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Allen 2021/7/29 22:41
 *
 * Read/Write Through模式中，服务端把缓存作为主要数据存储。应用程序跟数据库缓存交互，都是通过抽象缓存层完成的。
 **/
public class ReadWritThrough {

    @Autowired
    private CacheProvider cacheProvider;

    /**
     * 抽象层实现 读操作
     * @param cacheKey
     * @return
     */
    public String readThrough(String cacheKey){
        return cacheProvider.readThrough(cacheKey);
    }

    /**
     * 抽象层实现  写操作
     * @param value
     */
    public void writeThrough(SysUser value){
        cacheProvider.writeThrough(value);
    }
}
