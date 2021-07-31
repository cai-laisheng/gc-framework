package com.allen.redisson.write;

import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.po.SysUser;
import com.allen.redisson.repository.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Allen 2021/7/29 22:32 Cache-Aside Pattern，即旁路缓存模式，它的提出是为了尽可能地解决缓存与数据库的数据不一致问题。
 **/
public class CacheAsidePattern {
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * Cache-Aside读流程
     *
     * 读的时候，先读缓存，缓存命中的话，直接返回数据
     * 缓存没有命中的话，就去读数据库，从数据库取出数据，放入缓存后，同时返回响应。
     *
     * @param cacheKey
     * @return
     */
    public String readCacheAside(String cacheKey){
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
     * Cache-Aside 写流程
     *
     * 更新的时候，先更新数据库，然后再删除缓存。
     * @param value
     */
    public void writeCacheAside(SysUser value){
        // 更新数据库
      sysUserMapper.updateByPrimaryKey(value);

      String cacheKey = value.getMobile();
      // 删除缓存
      redisRepository.del(cacheKey);
    }
}
