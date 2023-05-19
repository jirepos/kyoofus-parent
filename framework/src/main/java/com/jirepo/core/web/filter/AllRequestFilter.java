package com.jirepo.core.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

/** URL 파라미터를 암호화 하는 경우 포워드(forward)한다. */
@Order(1)
@Slf4j
public class AllRequestFilter extends GenericFilterBean  {
  // GenericFilterBean 
  // Filter를 구현한 것과 동일하고 getFilterConfig()나 getEnvironment를 제공해 준다. 

  /** 펄터 처리한다. */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    
    boolean isParameterEncryption = Boolean.parseBoolean(getEnvironment().getProperty("settings.parameter-encryption", "false"));
    // 파라미터 암호화 여부
    if (isParameterEncryption) {
      // String uri = req.getRequestURI();
      // System.out.println(uri);
      String q = req.getParameter("q");
      if(q != null) {  // demo/filter/get-json?name=홍길동&age=20&job=사원&job=팀원
        String[] urlAndParams = RequestWrapperUtils.splitUrlAndParams(q);
        RequestWrapper wrapper = new RequestWrapper(req);
        req.getRequestDispatcher(urlAndParams[0]).forward(wrapper, response); // forward하면 브라우저의  URL이 변경되지 않는다. RequestWrapper에서 파라미터를 변경한다.
      }else {
        // filter 처리하지 않을 거면 doFilter를 호출한다.
        chain.doFilter(request, response); 
      }
    } else {
       chain.doFilter(request, response);
    }

  }// :

}/// ~
