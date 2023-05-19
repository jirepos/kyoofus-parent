package com.jirepo.core.constants;

import com.jirepo.core.config.util.PropertyUtil;

/**
 * 기본 상수 정의 클래스이다. 
 */
public class PropertyConstants {
    /** java.util.Date를 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd" */
    public static final String DATE_FORMAT = "yyyy-MM-dd"; 
    /** java.time.LocalDateTime을 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd HH:mm:ss" */
    // private static final String localDateTimeFormat =  "yyyy HH:mm:ss";  //
    public static final String LOCAL_DATE_TIME_FORMAT =  "yyyy-MM-dd HH:mm:ss";  //
    /** java.time.LocalDate를 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd" */
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    /** 대시 없는 날짜 문자열. yyyyMMdd */
    public static final String PLAIN_DATE_FORMAT = "yyyyMMdd";

    public static enum KEY { 
        JWT
    }


    /** 서버의 타입존 */
    public static final String SERVER_TIME_ZONE = PropertyUtil.getProperty("server.time.zone");
    /** 표준 데이트 포맷 */
    public static final String STANDARD_DATE_FORMAT = PropertyUtil.getProperty("standard-date-format");
    /** JWT 생성 시 사용할 JWT_KEY */
    public static final String JWT_KEY = PropertyUtil.getProperty("application-server.jwt-key");
    
}
