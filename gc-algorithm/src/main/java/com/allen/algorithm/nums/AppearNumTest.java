package com.allen.algorithm.nums;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xuguocai on 2021/7/22 14:41
 *
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 */
public class AppearNumTest {

    @Test
    public void test(){
        int[] arr = {1,2,3,4,4,3,2,5};

        // 统计出现得次数
        Map<Integer,Integer> map = new HashMap<>();
        for (int i=0;i<arr.length;i++){
            int tmp = arr[i];
            Integer num = map.get(tmp);
            if (num == null){
                map.put(tmp,1);
            }else {
                num +=1;
                map.put(tmp,num);
            }
        }

        // 过滤符合条件的数据
        for (Map.Entry<Integer,Integer> item : map.entrySet()){
            Integer key = item.getKey();
            Integer value = item.getValue();
            if (value == 1){
                System.out.print(key +" " );
            }
        }
    }

    /**
     * “除了某个元素只出现一次以外，其余每个元素均出现二次”的条件下，通过使用“异或”的操作，找到了只出现一次的元素。
     * 原因是因为“异或”操作可以让两数相同归 0。
     *
     * 利用 异或 处理
     *  过程：1 --》0001 ; 2  --》 0010 ; 3 --》0011
     *    1^2
     *         0001
     *         0010
     *       --------
     *         0011  -- > 3
     *
     *     数组存在不相等的元素，最后 异或 的结果就是那个单独的元素。 这里的数组只能只有一个 出现次数唯一的元素。
     *
     */
    @Test
    public void test2(){
        int[] nums = {1,2,3,4,4,3,2};

        int ans = nums[0];
        for (int i = 1; i < nums.length; i++) {
            // 若是存在相同的元素，异或的结果为 0，若是不相等，则最后一个异或必定是结果
            ans = ans ^ nums[i];
        }
        System.out.println(ans);

    }

    /**
     * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现了三次。找出那个只出现了一次的元素。说明：你的算法应该具有线性时间复杂度。你可以不使用额外空间来实现吗？
     *
     * 数学处理：
     * 原理：[A,A,A,B,B,B,C,C,C] 和 [A,A,A,B,B,B,C]，差了两个C。即：
     *
     * 3×(a b c)−(a a a b b b c)=2c
     *
     * 也就是说，如果把原数组去重、再乘以3得到的值，刚好就是要找的元素的2倍
     *
     */
    @Test
    public void test3(){
        int[] nums = {2,3,2,2,3,3,1};
        // 去重
        Set<Integer> set = new HashSet<>();
        int tmp = 0;
        for (int i = 0; i < nums.length; i++) {
            tmp +=nums[i];
           set.add(nums[i]);
        }
        System.out.println("tmp=="+tmp);
        // 出现三次
        int total = 0;
        for (Integer val : set){
            int sum = val * 3;
            total += sum;
        }

        System.out.println("total=="+total);

        int value = total - tmp;

        // 唯一的元素
        int item = value/2;
        System.out.println(item);
    }

    /**
     * https://www.geekxh.com/1.8.%E4%BD%8D%E8%BF%90%E7%AE%97%E7%B3%BB%E5%88%97/805.html#_04%E3%80%81%E4%BD%8D%E8%BF%90%E7%AE%97
     */
    @Test
    public void test4(){
        int[] nums = {2,3,2,2,3,3,1};
        int a = 0, b = 0;
        for (int next : nums) {
            b = (b ^ next) & ~a;
            a = (a ^ next) & ~b;
        }

        System.out.println(b);
    }
}
