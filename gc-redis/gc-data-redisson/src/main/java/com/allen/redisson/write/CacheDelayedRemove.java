package com.allen.redisson.write;

import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.po.SysUser;
import com.allen.redisson.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Allen 2021/7/31 20:14  缓存延时双删
 **/
public class CacheDelayedRemove {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 先删除缓存
     * 再更新数据库
     * 休眠一会（比如1秒），再次删除缓存。
     *
     * 这个休眠时间 = 读业务逻辑数据的耗时 + 几百毫秒。 为了确保读请求结束，写请求可以删除读请求可能带来的缓存脏数据。
     *
     * @param value
     */
    public void write(SysUser value){
        // 删除缓存
        redisRepository.del(value.getMobile());
        // 更新数据库
        sysUserMapper.updateByPrimaryKey(value);

        // 休眠一会（比如1秒），再次删除缓存。--》为了确保读请求结束，写请求可以删除读请求可能带来的缓存脏数据。
        redisRepository.del(value.getMobile());
    }

}
