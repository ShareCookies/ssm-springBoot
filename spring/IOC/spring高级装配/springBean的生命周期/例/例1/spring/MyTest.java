package com.china.hcg.spring;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MyTest {

    @Test
    public void test() {
        //定义容器并初始化
        //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        AbstractApplicationContext applicationContext = new FileSystemXmlApplicationContext("src/main/java/com/china/hcg/spring/"+"applicationContext.xml");
        applicationContext.getBean(UserDao.class);
        //只有关闭容器时，才会调用destroy方法
        applicationContext.registerShutdownHook();
    }
}