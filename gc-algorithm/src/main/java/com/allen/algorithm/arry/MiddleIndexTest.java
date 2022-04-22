package com.allen.algorithm.arry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author xuguocai  @date 2022/4/20 10:42 寻找数组的中心索引
 */
public class MiddleIndexTest {

    /**
     * 寻找数组的中心索引
     *
     * 处理思路：
     * 1、数组累加值  --》左边数相加  --》总数 -左边数
     * @param nums
     * @return
     */
    public static int getIndexVal(int[] nums){
        int sum = 0;
        // 获取数组所有累加值
        for(int i=0;i<nums.length;i++){
            sum +=nums[i];
        }
        int leftSum = 0;
        for(int j=0;j<nums.length;j++){
            // 总数减去当前节点数
            sum -=nums[j];
            // 剩余总数等于左边，即是剩余右边数 == 左边数
            if(sum == leftSum){
                return j;
            }
            // 左边数累加
            leftSum +=nums[j];
        }

        return -1;
    }

    /**
     * 合并区间
     * @param nums
     * @return
     */
    public static int[][] merge(int[][] nums){
        int len = nums.length;
        if (len ==0){
            return new int[0][2];
        }

        // 按照区间的起始值进行排序
        Arrays.sort(nums, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });

        List<int[]> list = new ArrayList<>();
        // 对nums 进行遍历
        for (int i = 0;i<len;i++){
            // 取出数组第一个元素
            int start = nums[i][0];
            // 取出数组第二个元素
            int end = nums[i][1];
            if (list.size() == 0 || start > list.get(list.size()-1)[1]){
                list.add(new int[]{start,end});
            }else {// 否则 将重复的区间进行合并
                list.get(list.size()-1)[1] = Math.max(end,list.get(list.size()-1)[1]);
            }
        }

        return list.toArray(new int[list.size()][]);
    }

    public static void main(String[] args) {
        int[] nums = {1, 7, 3, 6, 5, 6};
        System.out.println(getIndexVal(nums));

//        int[][] arr={ {1,3},{2,6},{8,10},{15,18}};
//        final int[][] merge = merge(arr);
//        for (int[] a:merge) {
//            System.out.println(Arrays.toString(a));
//        }
    }
}
