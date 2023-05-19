package com.jirepo.core.util;

/**
 * ID 생성기 클래스이다. 
 */
public class IdGenerator {

    /**
     * UUID를 생성하여 문자열로 반환한다. 
     * @return  UUID 문자열
     */
    public static String randomUUID() {
        return java.util.UUID.randomUUID().toString();
    }//:
    /**
     * UUID를 생성하여 "-"를 제가하여 문자열로 반환한다. 
     * @return  UUID 문자열
     */
    public static String randomUUID2() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }//:
    
}///~
