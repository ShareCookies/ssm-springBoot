package com.test.controller.aop;

import com.test.controller.RjUserController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @autor hecaigui
 * @date 2022-2-22
 * @description 用户中心方法调用过于耗时日志打印切面
 */
@Aspect
@Component
public class TimeConsumeAop {
    private static Logger logger = LoggerFactory.getLogger(TimeConsumeAop.class);
    // 使用了@Around来定义环绕通知通知。
    // （）中为切点表达式。该切点表达式设置了当RjUserController任意方法执行时，会触发该通知的调用
    @Around("execution(* com.test.controller.RjUserController.*(..))")
    public Object loggingAround(ProceedingJoinPoint joinPoint) throws Exception,Throwable{
        long startTime = System.currentTimeMillis();
        // 定义返回对象、得到方法需要的参数
        Object resultData = null;
        Object[] args = joinPoint.getArgs();
        Signature methodSignature = joinPoint.getSignature();
        try {
            //继续调用业务层方法（切入点方法）
            resultData = joinPoint.proceed(args);
            long endTime = System.currentTimeMillis();
            long consumeTime = endTime - startTime;

            if (consumeTime > 200){
                logger.info("======>请求"+methodSignature+"接口完成 过于耗时日志打印,耗时:{},返回:{}", consumeTime, resultData);
            }

        } catch (Throwable e) {
            // 抛出异常信息
            long endTime = System.currentTimeMillis();
            long consumeTime = endTime - startTime;
            logger.info("======>请求"+methodSignature+"接口完成 过于耗时日志打印,耗时:{},返回:{}", consumeTime, resultData);
            if (consumeTime > 200){
                logger.info("======>请求"+methodSignature+"接口完成 过于耗时日志打印,耗时:{},返回:{}", consumeTime, resultData);
            }
            throw e;
        }
        return resultData;
    }

}
