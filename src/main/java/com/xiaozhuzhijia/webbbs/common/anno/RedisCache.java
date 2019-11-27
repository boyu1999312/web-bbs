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
    CachePre value();
    CacheType type() default CacheType.SELECT;
    boolean isList() default true;
    Class cla();
}
