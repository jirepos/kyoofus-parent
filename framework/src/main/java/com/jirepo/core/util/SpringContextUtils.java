package com.jirepo.core.util;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Spring에서 제공하는 클래스를 사용하는 유틸리티 클래스이다. 
 */
public class SpringContextUtils {
  
  /**
   * 클래스패스 또는 파일 경로에 리소스(파일들)을 읽기 Resource를 반환한다. 
   * classpath:mappers/primary/{@literal **}/{@literal *}.xml
   * 
   * @param applicationContext  applicationContext 인스턴스 
   * @param paths  리소스 파일 경로 목록  예) classpath:mappers/primary/{@literal **}/{@literal *}.xml
   * @return  Resources 배열 
   * @throws IOException
   */
  public static Resource[] getResources(ApplicationContext applicationContext, String[] paths) throws IOException{
    if(paths == null) return null;
    List<Resource> resourceList = new ArrayList<>();

    for(String path: paths) {
      Resource[] resources = applicationContext.getResources(path);
      List<Resource> inList = Arrays.asList(resources);
      resourceList.addAll(inList);
    }
    org.springframework.core.io.Resource[] resourceToReturn = new org.springframework.core.io.Resource[resourceList.size()];
    resourceToReturn = resourceList.toArray(resourceToReturn);
    return resourceToReturn;
  }//:



  /**
   * 주어진 빈이름으로 클래스를 인스턴스화 하여 빈을 등록한다. 
   * @param applicationContext applicationContext 인스턴스
   * @param beanName  빈 이름
   * @param className  빈 클래스 이름 예) com.jirepo.core.service.UserService
   * @return
   * @throws Exception
   */
  public static AutowireCapableBeanFactory registerBean(ApplicationContext applicationContext, String beanName,  String className) throws Exception {
   
    // Spring의 Bean 등로할 수 있는 객체를 얻는다. 
    AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
    BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) factory;
    if(beanRegistry.containsBeanDefinition(beanName)) { // Bean 이름으로 존재하는지 검사 
      beanRegistry.removeBeanDefinition(beanName);  // 같은 이름이 있으면 제거 
    }
    // 등록하려고 하는 클래스를 로드한다.
    ClassLoader classLoader = SpringContextUtils.class.getClassLoader();
    Class<?> aClass = classLoader.loadClass(className.trim()); // clasSName 예:) "java.util.Date"
    
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition(); // 생성자와 인자값 및 property를 지정가능 
    beanDefinition.setBeanClass(aClass);
    beanDefinition.setAutowireCandidate(true);

    beanRegistry.registerBeanDefinition(beanName /* 빈 이름*/, beanDefinition); // 빈을 등록, 이제부터 Bean을 꺼낼 수 있다. 
    
    return factory;
  }//:
 

  /**
   * 주어진 빈이름으로 클래스를 인스턴스화 하여 빈을 등록하고 Autowire 처리한다. 
   * @param applicationContext  applicationContext 인스턴스
   * @param beanName 빈 이름
   * @param className   빈 클래스 이름 예) com.jirepo.core.service.UserService
   * @throws Exception
   */
  public static void registerBeanAndAutowire(ApplicationContext applicationContext, String beanName,  String className) throws Exception {
    // Spring의 Bean 등로할 수 있는 객체를 얻는다. 
    AutowireCapableBeanFactory factory = registerBean(applicationContext, beanName, className);
    Object o = applicationContext.getBean(beanName); // 빈을 꺼낸다. 
    //Autowire the bean properties of the given bean instance by name or type
    //빈을 주입한다.
    factory.autowireBeanProperties(o, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
  }//:

  
  /**
   * 주어진 클래스이름을 인스턴스화하여 필터를 등록한다. 
   * @param context applicationContext 인스턴스
   * @param className   필터 클래스 이름 예) com.jirepo.core.service.UserService
   * @throws Exception
   */
  public static void registerFilter(GenericWebApplicationContext context, String className) throws Exception {
    // 등록하려고 하는 클래스를 로드한다.
    ClassLoader classLoader = SpringContextUtils.class.getClassLoader();
    Class<?> aClass = classLoader.loadClass(className.trim());
    Constructor<?> constructor = aClass.getConstructor();
    Object obj = constructor.newInstance();
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter((Filter)obj);
    registrationBean.addUrlPatterns("/*");

    context.registerBean(aClass.getSimpleName(), registrationBean.getClass(), registrationBean);
    
    // context.registerBean(aClass.getSimpleName(), registrationBean.getClass(), () -> {
    //   FilterRegistrationBean<Filter> registrationBean2 = new FilterRegistrationBean<>();
    //   registrationBean2.setFilter((Filter)obj);
    //   registrationBean2.addUrlPatterns("/*");
    // });
  }
}///~

