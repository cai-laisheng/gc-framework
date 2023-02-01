package com.allen.jvm.dynamic;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author allen
 * @Description TODO
 * @Date 2023/2/1 20:23
 */
public class DynamicBean {
    private Object object = null; //动态生成的类
    private BeanMap beanMap = null; //存放属性名称以及属性的类型
    public DynamicBean() {
        super();
    }
    public DynamicBean(Map propertyMap) {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    public DynamicBean(Class<?> superclass, Map<String, Class<?>> propertyMap) {
        this.object = generateBean(superclass, propertyMap);
        this.beanMap = BeanMap.create(this.object);
}
    /**
     * @param propertyMap
     * @return
     */
    private Object generateBean(Map propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        for(Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            generator.addProperty(key, (Class) propertyMap.get(key));
        }

        return generator.create();
    }

    /**
     * 根据属性生成对象
     *
     * @param superclass
     * @param propertyMap
     * @return
     */
    private Object generateBean(Class<?> superclass, Map<String, Class<?>> propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        if (null != superclass) {
            generator.setSuperclass(superclass);
        }
        BeanGenerator.addProperties(generator, propertyMap);
        return generator.create();
    }

    /**
     * 给bean属性赋值
     * @param property 属性名
     * @param value 值
     */
    public void setValue(Object property, Object value) {
        beanMap.put(property, value);
    }
    /**
     * 通过属性名得到属性值
     * @param property 属性名
     * @return 值
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }
    /**
     * 得到该实体bean对象
     * @return
     */
    public Object getObject() {
        return this.object;
    }

}
