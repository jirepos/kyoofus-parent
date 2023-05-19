package com.jirepo.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 국제 시간을 다루기 위한 유틸리티 클래스이다. 
 */
public class GlobalTimeUtils {

    /** UTC Zone ID */
    public static final String ZONE_ID_UTC = "UTC";
    /** 표준 데이터 포맷  */
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * UTC 기준 현재시간을 구한다. 
     * @return 현재시간 
     */
    public static LocalDateTime getCurrentDateTime(){ 
        return ZonedDateTime.now(ZoneId.of(ZONE_ID_UTC)).toLocalDateTime();
    }//:
    

    /**
     * LocalDateTime에 시간대 정보를 추가한다.
     * @param ldt 로컬데이트타임
     * @param zoneId  존아이디
     * @return 시간대가 적용된 LocalDateTime 인스턴스 
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime ldt, String zoneId) {
        return ZonedDateTime.of(ldt, ZoneId.of(zoneId));
    }//:
    /**
     * 주어진 시간대로 존을 변경한다. 
     * @param zdt 변경할 시간
     * @param zoneId  변경할 존 아이디 
     * @return  존이 변경된 ZonedDateTime 인스턴스 
     */
    public static ZonedDateTime changeZone(ZonedDateTime zdt, String zoneId) {
        return zdt.withZoneSameInstant(ZoneId.of(zoneId));
    }//:

    /**
     * 날짜시간 문자열을 java.util.Date로 변환한다. 
     * @param dateStr 날짜시간 문자열
     * @param pattern 날짜 패턴 예) yyyy-MM-dd HH:mm:ss
     * @return java.util.Date 인스턴스
     * @throws ParseException
     */
    public static Date parseToDate(String dateStr, String pattern) throws ParseException{
        //String pattern = "yyyy-MM-dd HH:mm:ss"; 
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(dateStr);
    }//: 

    /**
     * 날짜시간 문자열을 java.time.LocalDateTime로 변환한다.
     * @param dateStr   날짜시간 문자열
     * @param pattern   날짜 패턴 예) yyyy-MM-dd HH:mm:ss 
     * @return  java.time.LocalDateTime 인스턴스
     */
    public static LocalDateTime parseToLocalDateTime(String dateStr, String pattern)  {
        return  LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }//:
    
}///~
