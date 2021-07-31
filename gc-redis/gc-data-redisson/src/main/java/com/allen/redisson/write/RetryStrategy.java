package com.allen.redisson.write;

import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.po.SysUser;
import com.allen.redisson.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Allen 2021/7/31 20:20  删除缓存重试机制
 *
 * 删除失败就多删除几次呀,保证删除缓存成功呀~ 所以可以引入删除缓存重试机制
 **/
public class RetryStrategy {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 写请求更新数据库
     * 缓存因为某些原因，删除失败
     * 把删除失败的key放到消息队列
     *
     * @param value
     */
    public void write(SysUser value){
        // 更新数据
        sysUserMapper.updateByPrimaryKey(value);
        // 删除缓存
        long del = redisRepository.del(value.getMobile());
        if (del != 0){
            // 删除失败，丢到kafka队列
            // todo
        }
    }

    /**
     * 队列监听器
     *
     * 消费消息队列的消息，获取要删除的key
     * 重试删除缓存操作
     * @param str
     */
    //@KafkaListener(topics = {"qywxopen-contactUserMsg"}, groupId = "wxGroup")
    public void listener(String str){
        String cacheKey = str;
        // 重试删除缓存操作
        long del = redisRepository.del(cacheKey);
        if (del != 0){
            // 删除失败，丢到队列

        }
    }

}
