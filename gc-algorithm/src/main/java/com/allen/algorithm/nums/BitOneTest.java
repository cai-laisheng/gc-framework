package com.allen.algorithm.nums;

import org.junit.Test;

/**
 * @author xuguocai on 2021/7/21 14:34  编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 ‘1’ 的个数（也被称为汉明重量）。
 */
public class BitOneTest {

    @Test
    public void test(){
        int num = 127;
        if (num == 1){
            System.out.println("二进制出现1的次数："+1);
            return;
        }
        int index = 0;
        while (num > 1){
            int tmp = num % 2;
            if (tmp == 1){
                index++;
            }

            num /=2;
        }

        System.out.println("二进制出现1的次数："+index);
    }

    /**
     * 这里判断 n&mask 的时候，千万不要错写成 (n&mask) == 1，因为这里你对比的是十进制数。
     *
     * 我们只需要让这个掩码每次向左移动一位，然后与目标值求“&”，就可以判断目标值的当前位是不是1。
     *
     *  1&5     0000 0001 & 0000 0101 = 0000 0001 ，既是 1
     *
     *  2&5     0000 0010 & 0000 0101 = 0000 0000 ，既是 0
     *
     *  3&5     0000 0011 & 0000 0101 = 0000 0001 ，既是 1
     *
     *  4&5     0000 0100 & 0000 0101 = 0000 0100 ，既是 4
     *
     *  5&5     0000 0101 & 0000 0101 = 0000 0100 ，既是 5
     *
     *  总结： 0000 0010
     *        0000 0101
     *    ---------------
     *        0000 0000
     *
     *     位数相同保持位数，位数不同取0
     */
    @Test
    public void test2(){
        int n = 5;

        int result = 0;
        //初始化掩码为1
        int mask = 1;
        for (int i = 0; i < 32; i++) {

            if ((n & mask) != 0) {
                result++;
            }
            mask = mask << 1;
            System.out.println(mask);
        }

        System.out.println(result);
    }
}
