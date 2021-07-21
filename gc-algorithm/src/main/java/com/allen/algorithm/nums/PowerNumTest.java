package com.allen.algorithm.nums;

import org.junit.Test;

/**
 * @author xuguocai on 2021/7/21 13:38  给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
 *
 */
public class PowerNumTest {

    @Test
    public void test(){
        int num = 218;
        if (num == 1){
            System.out.println("次数不是2的幂数");
            return;
        }

        while (num != 1){
            // 若是余数不等于0，且 num 不等于 1，则不是 幂数
            int tmp = num % 2;

            if (tmp == 1 && num > 2){
                System.out.println("此数字不是 2 的幂数");
                break;
            }
            num = num / 2;
        }

        if (num == 1){
            System.out.println("是 2 的幂数");
        }
    }

}
