package com.jirepo.core.web.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.util.Base64Utils;


/**
 * RequestWrapper에서 사용하는 유틸리티 클래스이다. 
 */
public class RequestWrapperUtils {
    
    /**
     * encrypted된 문자열을 decrypt한다. 
     * @param s 암호화된 문자열 
     * @return  복호화된 문자열 
     */
    public static String decode(String s) {
        return  new String(Base64Utils.decodeFromString(s), StandardCharsets.UTF_8);
    }

    /**
     * encrypted 된 url과 쿼리스트링을 분리한다.
     * @param q encrypted된 url과 쿼리 스트링 예) /demo/filter/get-json?name=홍길동&age=20&job=사원&job=팀원
     * @return  [0]decoded url, [1]decoded query string
     */
    public static String[] splitUrlAndParams(String q){ 
        String qString = RequestWrapperUtils.decode(q); 
        System.out.println(qString);
        return  qString.split("\\?");
    }
}
