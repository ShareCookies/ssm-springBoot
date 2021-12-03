import com.china.hcg.eas.business.EasBusinessConfiguration;
import org.springframework.boot.SpringApplication;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @autor hecaigui
 * @date 2020-1-31
 * @description
 */
public class ProxyTest {
    //生成目标对象的代理
    PersonBean getProxy(final  PersonBean personBean){
        //参数1代理对象的类加载器 2.代理对象的接口（代理对象没有实现接口了？） 3. 代理对象的调用处理器
        return (PersonBean) Proxy.newProxyInstance(personBean.getClass().getClassLoader(),
                personBean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().startsWith("get")){
                            //执行目标对象方法
                            Object returnValue = method.invoke(personBean, args);
                            return returnValue;
                        }else {
                            return null;
                        }
                    }
                });
    }
    public static void main(String[] args) {
        ProxyTest proxyTest = new ProxyTest();
        PersonBean personBean = new PersonBeanImpl();
        // personBean.set
        // 获取目标对象代理
        PersonBean proxy = proxyTest.getProxy(personBean);
        //调用代理方法，转向调用处理器
        proxy.getName();//能调用成功
        proxy.setName("11");//调用失败
    }

}
