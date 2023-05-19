package com.jirepo.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jirepo.core.web.filter.RequestWrapperUtils;


public class ForwardInterceptor implements HandlerInterceptor {


    @Autowired
    private DemoInjectionService demoInjectionService; 


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

                System.out.println("forwardInterceptor");
        // System.out.println(request.getContextPath());
        // System.out.println(request.getRequestURI());

        // System.out.println(request.getServletPath());
        String servletPath = request.getServletPath();
        
        if(servletPath.contains("/shared/")) {
            String[] splited = servletPath.split("/");
            String path = splited[splited.length-1];
            System.out.println(path);
            String url = request.getContextPath() + "/shorturl/" + path; 

            demoInjectionService.getDemo(); // DI 테스트
            request.getRequestDispatcher(url).forward(request, response); // forward하면 브라우저의  URL이 변경되지 않는다. RequestWrapper에서 파라미터를 변경한다.
        }

        // String q = request.getParameter("q");
        // String[] urlAndParams = RequestWrapperUtils.splitUrlAndParams(q);
        // 여기서 Short URL에 대한 정보를 DB로 부터 가져온다. 
        // @Override
        // public void addInterceptors(InterceptorRegistry registry) {
        //     registry.addInterceptor(new CustomInterceptor()).addPathPatterns("/**");
        // }
        // 위와 같이 처음에 만들었던 Interceptor를 addInterceptors를 Override하여 등록해주는 과정이다. 
        // 그런데 이 과정에 문제가 있었다. 위와 같이 new()를 통해 Interceptor 객체를 만들어서 
        // // 등록하면 Spring Container에서 이 Interceptor를 관리하지 못한다고 한다.
        // @Bean
        // public CustomInterceptor customInterceptor() {
        //     return new CustomInterceptor();
        // }
        // 위와 같이 @Bean을 생성하면 Spring에서 이 Bean을 실행하면서 만들고 관리하게 된다. 이것을 통해 Interceptor를 등록하게 되면 정상적으로 Service Layer를 @Resource를 통해 주입받고 작동하게 된다.
        //request.getRequestDispatcher(urlAndParams[0]).forward(request, response); // forward하면 브라우저의  URL이 변경되지 않는다. RequestWrapper에서 파라미터를 변경한다.
        return true;
    }// :
}
