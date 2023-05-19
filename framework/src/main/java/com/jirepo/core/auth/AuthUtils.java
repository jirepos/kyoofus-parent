package com.jirepo.core.auth;

import java.util.Map;

import com.jirepo.core.constants.PropertyConstants;
import com.jirepo.core.jwt.JwtUtils;
import com.jirepo.core.web.util.session.SessionUtils;


/**
 * 인증 처리를 위한 유틸리티 클래스이다.
 */
public class AuthUtils {

    public static String createJws(Map<String, String> claims){ 
        String encodedKey = PropertyConstants.JWT_KEY;  // 토큰 암호화 키 
        String subject = "user";     // 주제 
        String issuer = "jirepos";   // 발행자 
        String audience = "all";     // 대상 
        long expiration = 60 * 60 * 1000;  // 유효기간 하루
        return  JwtUtils.createJws(encodedKey, subject, issuer, audience, expiration, claims); // JWS 생성 
    }//


}///~
