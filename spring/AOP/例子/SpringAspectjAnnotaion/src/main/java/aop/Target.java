package aop;

import org.springframework.stereotype.Component;

/**
 * @autor hecaigui
 * @date 2020-8-22
 * @description
 */
@Component
public class Target {
    public void m1(){
        System.err.println("m1开始执行");
    }
    public void m2(int i1 , int i2){
        System.err.println("m2开始执行"+i1+i2);
    }
}
