package com.xiaozhuzhijia.webbbs.common.anno;

import com.xiaozhuzhijia.webbbs.common.enu.CachePre;
import com.xiaozhuzhijia.webbbs.common.enu.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    /** 表示后缀 */
    CachePre value();
    /** 查询或更新缓存 */
    CacheType type() default CacheType.SELECT;
    /** 数据是否为集合 */
    boolean isList() default false;
    /** 数据类型 */
    Class cla();
}
