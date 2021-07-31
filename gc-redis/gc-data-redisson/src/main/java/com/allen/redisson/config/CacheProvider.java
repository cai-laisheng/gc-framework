package com.allen.redisson.config;

import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.po.SysUser;
import com.allen.redisson.repository.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Allen 2021/7/29 22:44  Read/Write Through模式的 抽象缓存层
 *
 * 可以是单独服务，也可以是一个类
 **/
@Component
public class CacheProvider {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * Read-Through的简要流程如下
     *
     * 1、从缓存读取数据，读到直接返回
     * 2、如果读取不到的话，从数据库加载，写入缓存后，再返回响应。
     *
     * @param cacheKey
     * @return
     */
    public String readThrough(String cacheKey){
        String result = redisRepository.get(cacheKey);
        if (StringUtils.isNotBlank(result)){
            return result;
        }

        result = sysUserMapper.selectAll().toString();
        if (StringUtils.isNotBlank(result)){
            redisRepository.set(cacheKey,result);
        }

        return result;
    }

    /**
     * Write-Through
     * Write-Through模式下，当发生写请求时，也是由缓存抽象层完成数据源和缓存数据的更新
     *
     * @param value
     */
    public void writeThrough(SysUser value){
        sysUserMapper.updateByPrimaryKey(value);

        redisRepository.set(value.getMobile(),value.toString());
    }

    //////////////////////////////Write behind （异步缓存写入） 抽象层////////////////////////////////////////

    /**
     * 更新缓存
     *
     * 这种方式下，缓存和数据库的一致性不强，对一致性要求高的系统要谨慎使用。但是它适合频繁写的场景，MySQL的InnoDB Buffer Pool机制就使用到这种模式。
     *
     * @param value
     */
    public void writeBehind(SysUser value){
        // 更新缓存
        redisRepository.set(value.getMobile(),value.toString());

        // 批量异步 更新数据库
        sysUserMapper.updateByPrimaryKey(value);
    }


}
