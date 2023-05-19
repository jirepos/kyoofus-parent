package com.jirepo.core.web.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 필터를 등록한다. 
 */
@Configuration
public class FilterConfig {

    @Autowired 
    private ApplicationContext ApplicationContext;
    /** AllRequestFilter 필터 등록 */
    @Bean
    public FilterRegistrationBean<AllRequestFilter> allRequestFilter() {
        FilterRegistrationBean<AllRequestFilter> registrationBean = new FilterRegistrationBean<>();
        AllRequestFilter aFilter = new AllRequestFilter();
        // application.yml 파일의 속성을 읽기 위해 ApplicationContext.getEnvironment()를 사용하여 
        // Environment를 설정한다. 
        aFilter.setEnvironment(ApplicationContext.getEnvironment());
        registrationBean.setFilter(aFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
    //
    // @Bean
    // public FilterRegistrationBean<SecondFilter> seconfFilter(){
    // FilterRegistrationBean<SecondFilter> registrationBean = new
    // FilterRegistrationBean<>();
    // registrationBean.setFilter(new SecondFilter());
    // registrationBean.addUrlPatterns("/*");
    // return registrationBean;
    // }
}
