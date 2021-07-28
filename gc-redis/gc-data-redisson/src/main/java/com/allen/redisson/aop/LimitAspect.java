package com.allen.redisson.aop;

import com.alibaba.fastjson.JSONObject;
import com.allen.redisson.annotation.MethodLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author xuguocai on 2021/7/26 16:37  限流处理  限流器（RateLimiter）
 */
@Aspect
@Component
public class LimitAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String REDISKEY = "wl_qy_service_";

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.allen.redisson.annotation.MethodLimit)")
    public void ratePointCut() {

    }

    @Around("ratePointCut()")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        MethodLimit limitAnno = method.getAnnotation(MethodLimit.class);

        String key = REDISKEY+method.getName();
        // 过期时间
        int limitPeriod = limitAnno.period();
        // 限制次数
        int limitCount = limitAnno.count();

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        // 2、 设置速率，10 秒中产生100 个令牌
        rateLimiter.trySetRate(RateType.OVERALL, limitCount, limitPeriod, RateIntervalUnit.SECONDS);

        if (rateLimiter.tryAcquire()) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
                throw new RuntimeException("server exception");
            }

        } else {
            logger.info("接口：{},超过令牌数:{} ,request acquire fail",method.getName(),limitCount);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errcode",420);
            jsonObject.put("errmsg","被限流，请稍后再试");
            return jsonObject.toJSONString();
        }

    }

}
