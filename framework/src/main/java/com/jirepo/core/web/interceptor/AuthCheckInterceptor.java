package com.jirepo.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 인증 여부 체크 인터셉터이다.
 */
@Slf4j
public class AuthCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                
        log.debug("AuthCheckInterceptor.preHandle()====");

        //req.getRequestDispatcher(urlAndParams[0]).forward(wrapper, response); // forward하면 브라우저의  URL이 변경되지 않는다. RequestWrapper에서 파라미터를 변경한다.

        return true;
    }// :

}/// ~
