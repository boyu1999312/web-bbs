package com.xiaozhuzhijia.webbbs.web.aspect;

import com.xiaozhuzhijia.webbbs.common.anno.RedisCache;
import com.xiaozhuzhijia.webbbs.common.util.JsonMapper;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



@Aspect
@Component
public class AspectCache {

    private Log log = LogFactory.getLog(AspectCache.class);

    @Autowired
    private StringRedisTemplate redis;

    @Pointcut("@annotation(com.xiaozhuzhijia.webbbs.common.anno.RedisCache)")
    public void before(){};
    // @Pointcut("@annotation(com.xiaozhuzhijia.webbbs.common.anno.ReidsUpdate)")
    // public void update(){};

    /**
     * 查询缓存
     * @param joinPoint
     * @return
     */
    @Around(value = "before()")
    public Object redisCache(ProceedingJoinPoint joinPoint){

        log.info("缓存的目标方法名：" + joinPoint.getSignature().getName());

        if(getType(joinPoint)){
            return select(joinPoint);
        }
        return update(joinPoint);
    }

    /**
     * 判断缓存类型
     * @param joinPoint
     * @return
     */
    private boolean getType(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RedisCache redisCache = signature.getMethod().getAnnotation(RedisCache.class);
        return redisCache.type().isSelect();
    }


    /**
     * 获取注解value
     * @param joinPoint
     * @return
     */
    private String getAnnoValue(ProceedingJoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RedisCache redisCache = signature.getMethod().getAnnotation(RedisCache.class);
        return redisCache.value().getName();
    }


    /**
     * 缓存查询
     * @return
     */
    private Object select(ProceedingJoinPoint joinPoint){

        String value = getAnnoValue(joinPoint);
        UserVo userVo = LocalUser.get();
        String key = userVo.getUserName() + "," +value;

        try {
            String json = (String) redis.opsForHash().get(key, value);
            if(!StringUtils.isEmpty(json)){
                log.info("拿到了缓存数据");
                Object o = JsonMapper.toObject(json, Object.class);
                return Result.ok(o);
            }
            log.info("没拿到缓存数据");
            Result result = (Result) joinPoint.proceed();
            if(result.getCode() != 200){
                return result;
            }
            Object data = result.getData();
            String toJson = JsonMapper.toJson(data);
            redis.opsForHash().put(key, value, toJson);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    /**
     * 清除缓存
     * @param joinPoint
     * @return
     */
    private Object update(ProceedingJoinPoint joinPoint){
        String value = getAnnoValue(joinPoint);
        UserVo userVo = LocalUser.get();
        String key = userVo.getUserName() + "," +value;

        try {
            String json = (String) redis.opsForHash().get(key, value);
            if(!StringUtils.isEmpty(json)){
                log.info("清除了缓存数据");
                redis.opsForHash().delete(key, value);
            }
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
