package com.allen.jvm.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuguocai on 2020/12/24 14:18
 */
public class ListTest {
    public static List<Integer> list = new ArrayList<>(10);
    public static void main(String[] args) {
        add();

        System.out.println("数组大小："+list.size());

        delete(11);

        System.out.println("数组大小："+list);
    }

    public static void add(){
        for (int i=0;i<20;i++){
            list.add(i);
        }
    }

    public static void delete(int i){
        list.remove(i);
    }

}
