package com.allen.jvm.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Student {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        map.put("aaa",100);
        map.put("bbb",50);
        map.put("ccc",80);

        Student student = new Student();
        student.setName("张三");
        student.setAge(566);


        //添加参数age--->29
        Object obj = DynamicBeanUtils.getTarget(student, map);
        //打印结果
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        for(Method method:declaredMethods){
            if(method.getName().startsWith("get")){
                Object o=method.invoke(obj);
                System.out.println(method.getName()+ "属性值get方法->"+o);
            }
        }

    }

}
