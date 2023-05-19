package com.jirepo.core.config.util;



import org.springframework.context.ApplicationContext;

/**
 * ApplicationContext를 이용하여 application.yml 파일의 설정 값을 읽는다. 
 */
public class PropertyUtil {
  
  /**
   * application.yml 파일에서 설정값을 읽는다. 설정값이 없으면 null을 반환한다. 
   * @param propertyName 설정키 
   * @return  설정값 
   */
  public static String getProperty(String propertyName) {
    return getProperty(propertyName, null);
  }

  /**
   * application.yml 파일에서 설정값을 읽는다. 설정값이 없으면 기본값을 반환한다. 
   * @param propertyName 설정키
   * @param defaultValue 설정값이 없을 경우 반환될 기본값
   * @return  설정값 
   */
  public static String getProperty(String propertyName, String defaultValue) {
    ApplicationContext applicationContext = ApplicationContextHolder.geApplicationContext();
    String value = applicationContext.getEnvironment().getProperty(propertyName);
    return (value == null)? defaultValue : value; 
  }
}
