package spittr.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration //标明这是一个配置类。一般DispatcherServlet会加载该配置类作为spring应用的上下文。该配置类包含Web组件的bean，如控制器、视图解析器以及处理器映射。
@EnableWebMvc //启用springMvc，那是启用了一些关于mvc的注解吗？
@ComponentScan("spittr.web") //启用组件扫描。//作用：扫描根目录下指定包类，如果该类使用了构造型注解则把该类注册为bean
public class WebConfig extends WebMvcConfigurerAdapter {

  /**
   *配置视图解析器
   * 介绍：
   * 	添加了一个ViewResolver bean。
   *	它会在视图名称上加一个特定的前缀和后缀，去查找对应的JSP文件。
   */
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();//配置jsp视图解析器
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
  }
  /**
   *配置静态资源的处理？
   * 扩展了WebMvcConfigurerAdapter？...?要求DispatcherServlet将对静态资源的请求转发到Servlet容 器中默认的Servlet上，而不是使用DispatcherServlet本身来处理 此类请求。
   */
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // TODO Auto-generated method stub
    super.addResourceHandlers(registry);
  }

}
