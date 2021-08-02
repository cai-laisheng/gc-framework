package com.allen.redisson.bloom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author xuguocai on 2021/8/2 14:48    布隆过滤器
 */
@Component
public class BloomFilterUtil {

    /**
     * 布隆过滤器信息key的后缀
     */
    private final static String BLOOM_FILTER_INFO_SUFFIX = ":info";

    /**
     * 布隆过滤器信息容错率的key
     */
    private final static String FPP = "fpp";

    /**
     * 布隆过滤器信息bit数组长度的key
     */
    private final static String NUM_BITS = "numBits";
    /**
     * 布隆过滤器信息hash次数的key
     */
    private final static String NUM_HASH_FUNCTIONS = "numHashFunctions";

    /**
     * 布隆过滤器数组key的后缀
     */
    private final static String BLOOM_FILTER_BIT_ARRAY_SUFFIX = ":bitarray";

    /**
     * 注入redistemplate
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 添加一个值
     * @param bloomFilterName bloomFilter名称
     * @param key 添加的key
     * @throws Exception 没有找到当前过滤器
     */
    public void put(String bloomFilterName, String key) throws Exception {
        long[] hashIndexArray = hash(bloomFilterName, key);

        StringBuilder sbName = new StringBuilder(bloomFilterName);
        sbName.append(BLOOM_FILTER_BIT_ARRAY_SUFFIX);

        for (long hashIndex : hashIndexArray) {
            redisTemplate.opsForValue().setBit(sbName.toString().intern(), hashIndex, true);
        }
    }

    /**
     * 判断当前key是否可能存在
     * @param bloomFilterName bloomFilter名称
     * @param key 判断的key
     * @return 是否可能存在
     * @throws Exception 没有找到bloomFilter
     */
    public boolean mightContain(String bloomFilterName, String key) throws Exception {
        boolean keyIsContain = false;
        long[] hashIndexArray = hash(bloomFilterName, key);

        StringBuilder sbName = new StringBuilder(bloomFilterName);
        sbName.append(BLOOM_FILTER_BIT_ARRAY_SUFFIX);

        for (long hashIndex : hashIndexArray) {
            if (keyIsContain = redisTemplate.opsForValue().getBit(sbName.toString().intern(), hashIndex)) {
                return keyIsContain;
            }
        }
        return keyIsContain;
    }


    /**
     * 计算key的hash
     * 根据名称拿到bloomFilter信息
     * @param key
     * @return
     */
    private long[] hash(String bloomFilterName, String key) throws Exception {
        long numBits;
        int numHashFunctions;

        StringBuilder sbName = new StringBuilder(bloomFilterName);
        sbName.append(BLOOM_FILTER_INFO_SUFFIX);

        Object numBitsString = redisTemplate.opsForHash().get(sbName.toString().intern(), NUM_BITS);
        Object numHashFunctionsString = redisTemplate.opsForHash().get(sbName.toString().intern(), NUM_HASH_FUNCTIONS);
        if (ObjectUtils.isEmpty(numBitsString) || ObjectUtils.isEmpty(numHashFunctionsString)) {
            // redis中找不到当前BloomFilter信息
            throw new Exception();
        } else {
            numBits = Integer.valueOf(numBitsString.toString());
            numHashFunctions = Integer.valueOf(numHashFunctionsString.toString());
        }
        return hash(key, numHashFunctions, numBits);
    }

    /**
     * 计算key的hash(动态key + 求余法)
     * @return
     */
    private long[] hash(String key, int numHashFunctions, long numBits) {
        long[] hashIndexArray = new long[numHashFunctions];
        for (int i = 0, j = hashIndexArray.length; i < j; i++) {
            hashIndexArray[i] = (key.hashCode() + numHashFunctions) % numBits;
        }
        return hashIndexArray;
    }

    /**
     * 计算bit数组长度
     * 来自google
     *
     * @param expectedInsertions 已有数据长度
     * @param fpp                容错率
     * @return 每个hash索引个数
     */
    static long optimalNumOfBits(long expectedInsertions, double fpp) {
        if (fpp == 0.0D) {
            fpp = 4.9E-324D;
        }

        return (long) ((double) (-expectedInsertions) * Math.log(fpp) / (Math.log(2.0D) * Math.log(2.0D)));
    }

    /**
     * 计算每个key的index个数
     * 来自google
     *
     * @param expectedInsertions 已有数据长度
     * @param numBits            bit数组长度
     * @return 每个key的index个数
     */
    static int optimalNumOfHashFunctions(long expectedInsertions, long numBits) {
        return Math.max(1, (int) Math.round((double) numBits / (double) expectedInsertions * Math.log(2.0D)));
    }

    /**
     * 默认容错率0.03
     */
    public boolean initBloomFilter(String bloomFilterName, List<String> dataList) {
        return initBloomFilter(bloomFilterName, dataList, 0.03d);
    }

    /**
     * 初始化数据
     * @param bloomFilterName 过滤器名称
     * @param dataList 数据id列表
     * @param fpp 容错率
     * @return 成功失败
     */
    public boolean initBloomFilter(String bloomFilterName, List<String> dataList, double fpp) {
        int numHashFunctions;
        long expectedInsertions, numBits;

        expectedInsertions = dataList.size();
        numBits = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);

        // 1.executePipelined 重写 入参 RedisCallback 的doInRedis方法
        List<Object> resultList = redisTemplate.executePipelined((RedisConnection connection) -> {
            // 2.connection 打开管道
            connection.openPipeline();

            // 3.1 保存当前bloomFilter信息
            connection.hSet((bloomFilterName + BLOOM_FILTER_INFO_SUFFIX).getBytes(), FPP.getBytes(), String.valueOf(fpp).getBytes());
            connection.hSet((bloomFilterName + BLOOM_FILTER_INFO_SUFFIX).getBytes(), NUM_BITS.getBytes(), String.valueOf(numBits).getBytes());
            connection.hSet((bloomFilterName + BLOOM_FILTER_INFO_SUFFIX).getBytes(), NUM_HASH_FUNCTIONS.getBytes(), String.valueOf(numHashFunctions).getBytes());

            //  3.connection 给本次管道内添加 要一次性执行的多条命令
            for (String key : dataList) {
                long[] hashIndexArray = hash(key, numHashFunctions, numBits);
                for (long hashIndex : hashIndexArray) {
                    connection.setBit((bloomFilterName + BLOOM_FILTER_BIT_ARRAY_SUFFIX).getBytes(), hashIndex, true);
                }
            }

            // 4.关闭管道 不需要close 否则拿不到返回值
            connection.closePipeline();
            // 这里一定要返回null，最终pipeline的执行结果，才会返回给最外层
            return null;
        });
        return true;
    }

}
