package com.allen.jvm.dynamic;


import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * @Author allen
 * @Description TODO
 * @Date 2023/2/1 21:42
 */
public class DynamicBeanUtils {

    public static Object getTarget(Object dest, Map<String, Object> addProperties) {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        //得到原对象的属性
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class<?>> propertyMap = Maps.newHashMap();
        for (PropertyDescriptor d : descriptors) {
            if (!"class".equalsIgnoreCase(d.getName())) {
                propertyMap.put(d.getName(), d.getPropertyType());
            }
        }
        addProperties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
        //构建新的对象
        DynamicBean dynamicBean = new DynamicBean(dest.getClass(), propertyMap);
        for (Map.Entry<String, Class<?>> entry : propertyMap.entrySet()) {
            try {
                if (!addProperties.containsKey(entry.getKey())) {//原来的值
                dynamicBean.setValue(entry.getKey(), propertyUtilsBean.getNestedProperty(dest, entry.getKey()));
            }else {//新增的值
                 dynamicBean.setValue(entry.getKey(), addProperties.get(entry.getKey()));
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dynamicBean.getObject();
}

}
