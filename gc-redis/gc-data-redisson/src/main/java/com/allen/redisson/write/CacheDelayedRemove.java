package com.allen.redisson.write;

import com.allen.redisson.mapper.SysUserMapper;
import com.allen.redisson.po.SysUser;
import com.allen.redisson.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Allen 2021/7/31 20:14  缓存延时双删
 **/
public class CacheDelayedRemove {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private SysUserMapper sysUserMapper;

    private ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

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

        // 休眠一会（比如1秒），再次删除缓存。--》这个休眠时间 = 读业务逻辑数据的耗时 + 几百毫秒。为了确保读请求结束，写请求可以删除读请求可能带来的缓存脏数据。
        try {
            Thread.sleep(100);
            redisRepository.del(value.getMobile());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 利用线程池删除--》异步
     * @param value
     */
    public void write2(SysUser value){
        // 删除缓存
        redisRepository.del(value.getMobile());
        // 更新数据库
        sysUserMapper.updateByPrimaryKey(value);

        // 休眠一会（比如1秒），再次删除缓存。--》这个休眠时间 = 读业务逻辑数据的耗时 + 几百毫秒。为了确保读请求结束，写请求可以删除读请求可能带来的缓存脏数据。
        executorService.execute(new DelCacheByThread(value.getMobile()));

    }

    /**
     * @Author Allen 2021/8/1 10:00  线程操作  --- 达到异步目的
     **/
    private class DelCacheByThread implements Runnable{

        private String key;

        public DelCacheByThread(String key ) {
            this.key = key;
        }

        @Override
        public void run() {
            // 删除
            try {
                Thread.sleep(100);
                redisRepository.del(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
