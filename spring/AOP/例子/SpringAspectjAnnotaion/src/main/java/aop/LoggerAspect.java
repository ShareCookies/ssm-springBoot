package aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @autor hecaigui
 * @date 2020-8-22
 * @description
 */
@Component
// 标明这是一个切面类。
@Aspect
public class LoggerAspect {
    // 使用了@Before来定义前置通知。
    // （）中为切点表达式。该切点表达式设置了当Target的m1()方法执行时触发通知的调用
    @Before("execution(* aop.Target.m1(..))")
    public void loggerBeforeIntercept(){
        System.err.println("日志打印之前置通知");
    }
    // 切点表达式中的args ()限定符，表明传递给m2方法的int类型参数也会传递到通知中去。。
    @Before("execution(* aop.Target.m2(int , int )) && args (num1,num2)")
    public void loggerBeforeIntercept2(int num1,int num2){
        System.err.println("日志打印之前置通知"+num1+num2);
    }
}
