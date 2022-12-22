package com.allen.jvm.reflect;

public class Person {

    private String name;

    private String label;

    private String age;

    public Person(String name, String label, String age) {
        this.name = name;
        this.label = label;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
