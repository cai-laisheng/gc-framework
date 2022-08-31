package com.allen.thread.aqs;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xuguocai
 * @date 2022/8/31 15:12  学习 ReentrantReadWriteLock 示例 (demo1 锁降级)
 *
 * 可以用来实现并发业务场景及并发容器
 *
 * 一个缓存对象的使用案例，缓存对象在使用时，一般并发读的场景远远大于并发写的场景，所以缓存对象是非常适合使用ReentrantReadWriteLock来做控制的
 */
public class ReentrantReadWriteLockTest {
    // 读写锁
    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // 读锁
    public static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    // 写锁
    public static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            // 锁降级
            demo1();
        }
    }

    //被缓存的具体对象
    public static int value = 0;
    //当前对象是否可用，使用volatile来保证可见性
    public static volatile boolean flag ;

    private static void demo1() {
        // 读数据，先加读锁。如果成功，说明此时没有人在并发写
        readLock.lock();

        //拿到读锁后，判断当前对象是否有效
        if (!flag){
            // Must release read lock before acquiring write lock
            //这里的处理非常经典，当你持有读锁之后，不能直接获取写锁，
            //因为写锁是独占锁，如果直接获取写锁，那代码就在这里死锁了
            //所以必须要先释放读锁，然后手动获取写锁
            readLock.unlock();
            writeLock.lock();

            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                //经典处理之二，在独占锁内部要处理数据时，一定要做二次校验
                //因为可能同时有多个线程全都在获取写锁，
                //当时线程1释放写锁之后，线程2马上获取到写锁，此时如果不做二次校验那可能就导致某些操作做了多次
                if (!flag){
                    value +=1;
                    //当缓存对象更新成功后，重置标记为true
                    flag = true;
                }
                // Downgrade by acquiring read lock before releasing write lock
                //这里有一个非常神奇的锁降级操作，所谓降级是说当你持有写锁后，可以再次获取读锁
                //这里之所以要获取一次读锁是为了防止当前线程释放写锁之后，其他线程马上获取到写锁，改变缓存对象
                //因为读写互斥，所以有了这个读锁之后，在读锁释放之前，别的线程是无法修改缓存对象的
                readLock.lock();
            }finally {
                writeLock.unlock();
            }
        }

        try {
            System.out.println("输出value值："+value);
        }finally {
            readLock.unlock();
        }
    }

    //原来非并发安全的容器  -----------------demo2 实现并发容器 ReadWriteLockCacheContainer -----------------------
    class ReadWriteLockCacheContainer{
        private final Map<String, String> m = new TreeMap<String, String>();

        public String get(String key) {
            //读数据，上读锁
            readLock.lock();
            try {
                return m.get(key);
            } finally {
                readLock.unlock();
            }
        }

        public Object[] allKeys() {
            //读数据，上读锁
            readLock.lock();
            try {
                return m.keySet().toArray();
            }
            finally {
                readLock.unlock();
            }
        }

        public String put(String key, String value) {
            //写数据，上写锁
            writeLock.lock();
            try {
                return m.put(key, value);
            }
            finally {
                writeLock.unlock();
            }
        }

        public void clear() {
            //写数据，上写锁
            writeLock.lock();
            try {
                m.clear();
            }
            finally {
                writeLock.unlock();
            }
        }
    }

}
