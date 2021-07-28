package com.allen.redisson.annotation;

import java.lang.annotation.*;

/**
 * @author xuguocai on 2021/7/26 16:43
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodLimit {

    /**
     * 资源的key
     *
     * @return
     */
    String key() default "";

    /**
     * Key的prefix
     *
     * @return
     */
    String prefix() default "";

    /**
     * 给定的时间段
     * 单位秒
     *
     * @return
     */
    int period();

    /**
     * 最多的访问限制次数
     *
     * @return
     */
    int count();

}
