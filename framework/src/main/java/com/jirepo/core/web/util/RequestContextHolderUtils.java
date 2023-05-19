package com.jirepo.core.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/** 
 * ReqeustContextHolder로부터 HttpServletReqeust와 HttpServletResponse를 구한다. 
 */
public class RequestContextHolderUtils {

    private static ServletRequestAttributes getServletRequestAttributes() {
        // currentAttributes()   구하려는 RequestAttributes가 없으면 예외를 반환  
        // getRequestAttributes() 구하려는 RequestAttributes가 없으면 null을 반환 
        // RequestContextHolder는 Spring 컨텍스트에서 HttpServletRequest 에 직접 접근할 수 있다. 
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    /** HttpServletRequst를 구한다.  */
    public static HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }
    /** HttpServletResponse를 구한다.  */
    public static HttpServletResponse getHttpServletResponse(){ 
        return getServletRequestAttributes().getResponse();
    }
    
}///~
