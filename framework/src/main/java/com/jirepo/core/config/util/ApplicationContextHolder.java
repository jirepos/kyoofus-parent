package com.jirepo.core.config.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/* Spring Load 시 ApplicationContext를 보관한다. 이 컴포넌트가 P6spyConfig 구성보다 먼저 인스턴스가 생성되어야 한다.  */    
@Component 
public class ApplicationContextHolder implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(org.springframework.context.ApplicationContext arg0) throws BeansException {
    applicationContext = arg0; 
  } 
  /** ApplicationContext를 반환한다. */
  public static ApplicationContext geApplicationContext() {
    return applicationContext; 
  }
  public static Object getBean(String beanName) {
    return geApplicationContext().getBean(beanName);
  }
}///~
    
