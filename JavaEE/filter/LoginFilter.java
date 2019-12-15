package com.heicaigui.EAS.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hecaigui.EAS.utils.StringUtils;
//https://blog.csdn.net/u012188794/article/details/41682559
@WebFilter(filterName="myfilter",urlPatterns={"*.action","/websocket"})
public class LoginFilter implements Filter{

	//初始化方法
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	//过滤处理方法
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
        System.out.println("----过滤开始----");
        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        HttpServletResponse httpServletresponse = (HttpServletResponse) response;
        HttpSession httpSession=httpServletRequest.getSession();//request中获取session
        String entityId=(String) httpSession.getAttribute("id");
        String uri=httpServletRequest.getRequestURI();// /ssm/ser.do 
        //StringBuffer url_buffer = httpServletRequest.getRequestURL();//http://localhost:8080/ssm/ser.do  
        // 拦截跳到首页
        if (entityId==null&&!StringUtils.isContain(uri, "user")) {
        	String url=httpServletRequest.getContextPath()+"/index.jsp";
        	httpServletresponse.sendRedirect(url);//重定向
        	return;
        }
        // 执行目标资源，放行。或传递给下一个过滤器
        filterChain.doFilter(request, response); 
        System.out.println("----过滤结束----");
	}
	//销毁方法
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
