package com.allen.algorithm.nums;

import org.junit.Test;

/**
 * @author xuguocai on 2021/7/20 15:52 位运算
 *
 * 求 1 2 ... n ，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
 */
public class BitNumTest {

    /**
     * 利用 for 遍历，获取的数累加
     */
    @Test
    public void test(){
        int num = 9;

        int total = 0;
        for (int i = num;i>0;i--){
            total +=i;
        }

        System.out.println(total);
    }

    /**
     *
     * 递归处理
     */
    @Test
    public void test2(){
        int i = sumNums(3);
        System.out.println(i);
    }

    /**
     * 利用递归处理，关键在于 n  += sumNums(n - 1)  及 n > 0
     *
     * 将递归的返回条件取非然后作为 && 的第一个条件，递归主体转换为第二个条件语句
     * @param n
     * @return
     */
    public int sumNums(int n) {
        System.out.println("start:"+n);
        boolean b = n > 0 && ((n  += sumNums(n - 1)) > 0);
        System.out.println(b+" end:"+n);
        return n;
    }

}
