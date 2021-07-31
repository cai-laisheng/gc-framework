package com.allen.redisson.write;

import com.allen.redisson.config.CacheProvider;
import com.allen.redisson.po.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Allen 2021/7/29 22:55  Write behind （异步缓存写入）
 *
 * Write behind跟Read-Through/Write-Through有相似的地方，都是由Cache Provider来负责缓存和数据库的读写。
 * 它两又有个很大的不同：Read/Write Through是同步更新缓存和数据的，Write Behind则是只更新缓存，
 * 不直接更新数据库，通过批量异步的方式来更新数据库。
 *
 **/
public class Writebehind {

    @Autowired
    CacheProvider cacheProvider;

    /**
     * 这种方式下，缓存和数据库的一致性不强，对一致性要求高的系统要谨慎使用。但是它适合频繁写的场景，
     * MySQL的InnoDB Buffer Pool机制就使用到这种模式。
     *
     * @param value
     */
    public void update(SysUser value){
        cacheProvider.writeBehind(value);
    }

}
