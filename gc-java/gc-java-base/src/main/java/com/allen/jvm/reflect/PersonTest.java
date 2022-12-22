package com.allen.jvm.reflect;

import java.lang.reflect.Field;

public class PersonTest {

    public static void main(String[] args) throws NoSuchFieldException {
        Person p = new Person("aa","bbb","ccc");
        Class clazz = p.getClass();

        Field[] fields = clazz.getFields();

        Field[] declaredFields = clazz.getDeclaredFields();
        Field name = clazz.getDeclaredField("name");
//        Field name = clazz.getField("name");
        System.out.printf(name.getName());


    }

}
