package com.baldyoung.vita.section;

import com.baldyoung.vita.common.pojo.exception.systemException.UtilityException;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.baldyoung.vita.common.utility.CommonMethod.getMerchantUserIdFromSession;

@Order(1)
@WebFilter(filterName = "merchantRequestFilter",
        urlPatterns = {"/vita/merchant/*",
                "/mBill/*",
                "/mDiningRoom/*",
                "/mMenu/*",
                "/mUser/*",
                "/mOrder/*",
                "/mProductAttribute/*",
                "/mProduct/*",
                "/mProductType/*",
                "/mSystem/*"} ,
        initParams = {@WebInitParam(name = "name", value = "baldyoung")})
public class MerchantRequestFilter implements Filter {
    private String url;
    /**
     * 可以初始化Filter在web.xml里面配置的初始化参数
     * filter对象只会创建一次，init方法也只会执行一次。
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("商家端拦截器初始化");
        // this.url = filterConfig.getInitParameter("URL");
        // System.out.println("我是过滤器的初始化方法！URL=" + this.url +  "，生活开始.........");
    }

    /**
     * 主要的业务代码编写方法
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // System.out.println("我是过滤器的执行方法，客户端向Servlet发送的请求被我拦截到了");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpSession httpSession = httpServletRequest.getSession();
        String url = httpServletRequest.getRequestURI();
        // System.out.println("##"+url);
        if (url.indexOf("Login") != -1 ||
                url.indexOf("login") != -1) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            Integer merchantUserId = getMerchantUserIdFromSession(httpSession);
        } catch (UtilityException e) {
            httpServletResponse.sendRedirect("/noLogin");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        // System.out.println("我是过滤器的执行方法，Servlet向客户端发送的响应被我拦截到了");
    }

    /**
     * 在销毁Filter时自动调用。
     */
    @Override
    public void destroy() {
        // System.out.println("我是过滤器的被销毁时调用的方法！，活不下去了................" );
    }
}
