package com.jGod.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jGod.reggie.common.BaseContext;
import com.jGod.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * 用户是否已登录的过滤器
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =  (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURL = request.getRequestURI();
        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
                "/backend/**", //静态资源
                "/front/**", //静态资源
                "/common/**",
                "/user/sendMsg",
                "/user/login",
        };
        if(checkUrl(urls,requestURL)){
            filterChain.doFilter(request,response); //放行
            return;
        }
        //后台登录
        if(request.getSession().getAttribute("employee")!=null){
            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response); //放行
            return;
        }
        //移动端登录
        if(request.getSession().getAttribute("user")!=null){
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response); //放行
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    public boolean checkUrl(String[] strings,String requestUrl){
        for (String string :strings) {
            if(PATH_MATCHER.match(string,requestUrl)){
                return true;
            }
        }
        return false;
    }
}
