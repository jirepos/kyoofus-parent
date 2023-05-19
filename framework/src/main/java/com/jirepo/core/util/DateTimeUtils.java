package com.jirepo.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.jirepo.core.constants.PropertyConstants;


/** 날짜 시간 유틸 */
public class DateTimeUtils {

  
    /** UTC Zone ID */
    public static final String ZONE_ID_UTC = "UTC";
    /** 표준 데이터 포맷  */
    public static final String STANDARD_DATE_FORMAT = PropertyConstants.STANDARD_DATE_FORMAT;  /// "yyyy-MM-dd HH:mm:ss";

    // current datetime secion 
    
    /**
     * UTC 기준 현재시간을 구한다. 
     * @return 현재시간 
     */
    public static LocalDateTime getUTCDateTime(){ 
        return ZonedDateTime.now(ZoneId.of(ZONE_ID_UTC)).toLocalDateTime();
    }//:

    /**
     * 현재시간을 가져온다 
     * @param zoneId 타임존
     * @return  현재시간 
     */
    public static LocalDateTime now(String zoneId) {
        return  ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime();
    }

    /**
     * appliation.yml의 server-time-zone을 기준으로 현재시간을 가져온다
     * @return 현재시간 
     */
    public static LocalDateTime systemTime() {
        return  ZonedDateTime.now( ZoneId.of(PropertyConstants.SERVER_TIME_ZONE)).toLocalDateTime();
    }

    // to secion 

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
     * Date를 LocalDateTime으로 변환한다.
     * @param date  변환할 Date
     * @param pattern   변환할 패턴
     * @return  변환된 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date, String pattern)  {
        return  LocalDateTime.parse(format(date, pattern), DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT));
    }//:


    /**
     * LocalDateTime을 Date로 변환한다.
     * @param d 변환할 LocalDateTime
     * @param pattern   변환할 패턴
     * @return  변환된 Date
     * @throws ParseException
     */
    public static Date toDate(LocalDateTime d, String pattern) throws ParseException {
        String formatted = format(d, pattern);
        return parseToDate(formatted, pattern);
    }//:



    // change secion 


    /**
     * 주어진 시간대로 존을 변경한다. 
     * @param zdt 변경할 시간
     * @param zoneId  변경할 존 아이디 
     * @return  존이 변경된 ZonedDateTime 인스턴스 
     */
    public static ZonedDateTime changeZone(ZonedDateTime zdt, String zoneId) {
        return zdt.withZoneSameInstant(ZoneId.of(zoneId));
    }//:


    // parse section 


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

        /**
     * 날짜 문자열을 LocalDate로 변환한다. 
     * @param dateString 날짜 문자열
     * @param pattern   포맷 예)"yyyy-MM-dd"
     * @return LocalDate 인스턴스
     */
    public static LocalDate parseToLocalDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }//:


    // format section 
    
    /**
     * java.util.Date를 문자열로 포맷한다. 
     * @param d  변환할 날짜 
     * @param pattern  변환할 패턴
     * @return  변환된 문자열 
     */
    public static String format(Date d, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(d);
    }//:

    /**
     * java.time.LocalDateTime를 문자열로 포맷한다.
     * @param d 변환할 날짜
     * @param pattern   변환할 패턴
     * @return  변환된 문자열
     */
    public static String format(LocalDateTime d, String pattern) {
        return d.format(DateTimeFormatter.ofPattern(pattern));
    }//:

    /**
     * Date를 기준 포맷 문자열로 포맷한다. 
     * @param d 변환할 Date
     * @return  변환된 문자열 
     */
    public static String format(Date d) {
        return format(d, STANDARD_DATE_FORMAT);
    }
    /**
     * LocalDateTime 객체를 기준 포맷 문자열로 변환한다.
     * @param d LocalDateTime 인스턴스
     * @return 문자열. 형식) "yyyy-MM-dd HH:mm:ss"
     */
    public static String format(LocalDateTime d) {
        return  format(d, STANDARD_DATE_FORMAT);
    }//:


    /**
     * LocalDate를 기준 포맷 문자열로 변환한다.
     * @param d LocalDate 인스턴스
     * @param format   변환할 포맷
     * @return 문자열. 형식) "yyyy-MM-dd"
     */
    public static String format(LocalDate d, String format) { 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return  d.format(formatter);
    }//:




    // /**
    //  * 시간대를 사용하여 현재 날시 시간을 구한다.
    //  * @param zoneIdString 시간대
    //  * @return  ZonedDateTime 인스턴스
    //  *  
    //  */
    // public static ZonedDateTime nowToZonedDateTime(String zoneIdString) {
    //     Instant today = Instant.now();
    //     return ZonedDateTime.ofInstant(today, ZoneId.of(zoneIdString));
    // }//:

    
    // /**
    //  * 시간대를 사용하여 현재 날시 시간을 구한다.
    //  * @param zoneIdString 시간대
    //  * @return  LocalDateTime 인스턴스
    //  */
    // public static LocalDateTime nowToLocalDateTime(String zoneIdString) {
    //     Instant today = Instant.now();
    //     return LocalDateTime.ofInstant(today, ZoneId.of(zoneIdString));
    // }//:


    // /**
    //  * 사용자의 시간대를 UTC로 변한한다. 
    //  * @param userZonedDateTime 사용자 시간대의 ZonedDateTime instance 
    //  * @return  ZonedDateTime 인스턴스 
    //  */
    // public static ZonedDateTime toUTCZonedDateTime(ZonedDateTime userZonedDateTime) {
    //     return  userZonedDateTime.withZoneSameInstant(ZoneId.of("UTC")); 
    // }//:
    // /**
    //  * UTC 시간대를 사용자의 시간대로 변환한다.
    //  * @param utcZOnedDateTime  UTC 시간 
    //  * @param zonedIdString 사용자 시간대
    //  * @return  ZonedDateTime 인스턴스
    //  */
    // public static ZonedDateTime toZonedDateTime(ZonedDateTime utcZOnedDateTime, String zonedIdString) {
    //     return  utcZOnedDateTime.withZoneSameInstant(ZoneId.of(zonedIdString)); 
    // }//:
    
    
    // /**
    //  * ZonedDateTime을 localDateTime으로 변환한다.
    //  * @param zdt ZonedDateTime 인스턴스
    //  * @return  LocalDateTime 인스턴스
    //  */
    // public static LocalDateTime toLocalDateTime(ZonedDateTime zdt) {
    //     return zdt.toLocalDateTime();
    // }//:



    /**
     * 주어진 날짜를 기준으로 한 주의 시작일(월요일)과 종료일(일요일)을 구한다. 
     * @param dateOfWeek
     * @return
     */
    public static LocalDate[] getStartAndEndDate(LocalDate dateOfWeek) {
        LocalDate monday = dateOfWeek;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        // Go forward to get Sunday
        LocalDate sunday = dateOfWeek;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return new LocalDate[] { monday, sunday };
    }//:

    
    // /**
    //  * 주어진 날짜를 "yyyMMdd" 형식의 문자열로 변환한다. 
    //  * @param dateTime 주어진 날짜
    //  * @return "yyyyMMdd" 형식의 문자열 
    //  */
    // public static String plainFormat(LocalDateTime dateTime) {
    //     return DateTimeFormatter.ofPattern("yyyyMMdd").format(dateTime);
    // }//:

    // /**
    //  * LocalDate를 주어진 포맷으로 변환한다.
    //  * @param date LocalDate 인스턴스
    //  * @param pattern   포맷. 예) "yyyy-MM-dd"
    //  * @return  String 인스턴스
    //  */
    // public static String formatDate(LocalDate date, String pattern) {
    //     return date.format(DateTimeFormatter.ofPattern(pattern));
    // }//:
        
       


    // /**
    //  * 시스템 날짜를 문자열로 변환한다. 
    //  * @return 문자열. 형식) "yyyy-MM-dd HH:mm:ss"
    //  */
    // public static String systemDateToStdFormat(){
    //     LocalDateTime time = LocalDateTime.now();
    //     return  time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    // }//:


}///~
