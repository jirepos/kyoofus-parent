package com.jirepo.core.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.boot.web.reactive.result.view.MustacheViewResolver;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
// import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.jirepo.core.json.JsonObjectMapperBuilder;
import com.jirepo.core.json.JsonUtils;
import com.jirepo.core.util.IoUtils;
import com.jirepo.core.web.interceptor.InterceptorSetting;



/**
 * DispatcherServlet 커스텀 설정을 위한 클래스이다. 
 */
// @EnableWebMvc
@Configuration
public class DispatcherServletConfig implements WebMvcConfigurer {

  @Autowired
  private ApplicationContext applicationContext;

  // @Autowired
  // private GenericWebApplicationContext genericWebApplicationContext;

  /**
   * DispatcherServlet의 매핑이 "/"로 지정하면 JSP를 제외한 모든 요청이 DispatcherServlet으로 가기 
   * 때문에 WAS가 제공하는 Default Servlet이 *.html, *.css같은 요청을 처리할 수 없게된다. 
   * Default ServletHandler는 이런 요청들을 Default Servlet에게 전달해주는 Handler이다. 
   * Deafult ServletHandler를 활성화 하려면 다음과 같이 한다.
   * @EnableWebMvc 어노테이션을 설정 후에 Spring Boot 2.4로 업그레드한 후 더이상 시작되지 않는다. 다음과 같은 오류가 있다.
   * unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.
   * Spring 2.4 릴리스 노트에 기술된 내용에는 DefaultServlet은 임베디드 서블릿 컨테이너에 더 이상 기본적으로 등록되지 않는다. applicaion.yml 파일에 다음과 같이 등록한다.
   * 
   * server:
   *   servlet:
   *     register-default-servlet: true
   */
  public void configureDefaultServletHandling(
      org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  /**
   * src/main/resources/public, src/main/resources/static 정적 리소스 폴더를 사용할 수 있도록 설정한다. 
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // url을 지정하여 실제 리소스 경로를 설정 
    // registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/", "classpath:/static/");// .setCachePeriod(0);
    registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");// .setCachePeriod(0);
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");// .setCachePeriod(0);
  }

  // /**
  //  * CorsFilter Bean을 생성한다. 
  //  * GraphQL을 사용할 경우에는 Filter를 적용해야 한다. Profile이 local인 경우에만 적용한다.
  //  */
  // @Bean
  // @Profile("local")
  // public CorsFilter corsFilter() {
  //   final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  //   final CorsConfiguration config = new CorsConfiguration();
  //   config.setAllowCredentials(true);
  //   config.addAllowedOrigin("http://localhost:8080");
  //   config.addAllowedHeader("*");
  //   config.addAllowedMethod("*");
  //   source.registerCorsConfiguration("/graphql/**", config);
  //   return new CorsFilter(source);
  // }// :



  // @Bean
  // public ViewResolver viewResolver() {
  //   InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
  //   // viewResolver.setPrefix("/WEB-INF/");
  //   // viewResolver.setSuffix(".jsp");
  //   return viewResolver;
  // }
  



  // @Bean 
  // public FreeMarkerViewResolver freemarkerViewResolver() { 
  //     FreeMarkerViewResolver resolver = new FreeMarkerViewResolver(); 
  //     resolver.setCache(true); 
  //     resolver.setPrefix("classpath:/templates/"); // 뷰의 위치
  //     resolver.setSuffix(".ftl"); 
  //     return resolver; 
  // }


  // @Bean 
  // public MustacheViewResolver mustacheViewResolver() { 
  //   MustacheViewResolver resolver = new MustacheViewResolver();
  //   resolver.setCharset("utf-8");
	// 	resolver.setContentType("text/html;charset=utf-8");
	// 	resolver.setPrefix("classpath:/templates/"); // 뷰의 위치
	// 	resolver.setSuffix(".html");
  //     return resolver; 
  // }

  /**
   * Cors설정을 처리한다. 
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // 사용자 정의 헤더를 다른 사이트에서 호출 시 노출하려면 exposedHeaders()를 사용해야 함
    registry.addMapping("/**")
        .allowedMethods("GET", "POST") //, "PUT", "DELETE", "HEAD", "OPTIONS")
        // https://stackoverflow.com/questions/46288437/set-cookies-for-cross-origin-requests
        // CORS 요청에서 cookies를 주고 받기 위해서
        .allowCredentials(true) // Access-Control-Allow-Credentials
        // preflight 요청에 대한 응답으로 응답 헤더로 사용된다. 
        // 실제 요청 시에 사용될 수 있는 HTTP headers를 지정한다.
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Headers
        .allowedHeaders("*")  
        // 이 헤더는 Access-Control-Request-Headers 헤더를 요청이 가진 경우에만 허용 
        //.allowedOriginPatterns("*");
        //.exposedHeaders("Set-Cookie")
        .exposedHeaders("X-Message", "X-Message-Code") // .allowedHeaders("X-Message"); 이거 설정하면 헤더값 제어됨        
        .allowedOrigins("null", "http://localhost:3000");

  }// :

  /** src/main/resource 아래의 interceptor-setting.json을 읽어서 interceptor를 등록한다. */
  @Override
  public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {

    String pathAndFile = "interceptor-setting.json"; // 기본 인터셉터 설정
    String jsonString = IoUtils.readFileClasspathToString(pathAndFile, "utf-8");

    // String jsonString = new IoUtils().readFileClasspathToString2(pathAndFile,
    // "utf-8");
    // ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    List<InterceptorSetting> interceptorList = JsonUtils.toList(jsonString, InterceptorSetting.class);
    if (interceptorList.size() == 0)
      return;

    ClassLoader classLoader = this.getClass().getClassLoader();
    for (InterceptorSetting setting : interceptorList) {
      try {
        Class<?> aClass = classLoader.loadClass(setting.getName().trim()); // class name
        // Creates a interceptor
        AutowireCapableBeanFactory factory = this.applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) factory;
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(aClass);
        beanDefinition.setAutowireCandidate(true);
        beanRegistry.registerBeanDefinition(aClass.getSimpleName(), beanDefinition);
        factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        // InterceptorRegistration regis = registry
        // .addInterceptor((HandlerInterceptor) aClass.newInstance());
        InterceptorRegistration regis = registry
            .addInterceptor((HandlerInterceptor) this.applicationContext.getBean(aClass.getSimpleName()));
        if (setting.getIncludes() != null && setting.getIncludes().length > 0) {
          regis.addPathPatterns(setting.getIncludes());
        }
        if (setting.getExcludes() != null && setting.getExcludes().length > 0) {
          regis.excludePathPatterns(setting.getExcludes());
        }
        // if(logger != null) {
        // // @WebMvcTest로 테스트할 때에는 logger가 null임
        // logger.info("📝 " + setting.getName() + " has been registered.");
        // }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } // for
  }// :


  
  /**
   * 구글 검색을 해 보면 {@literal @}Bean 을 사용하여 ObjectMapper 또는 Jackson2ObjectMapperBuilderCustomizer, MappingJackson2HttpMessageConverter
   * 을 사용하여 커스터마이징하라고 되어있지만 동작하지 않았다. 이 메서드는 동작한다. 
   */
  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
      // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      // ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
      //         .json()
      //         .featuresToEnable(SerializationFeature.INDENT_OUTPUTS)
      //                     // 다음 두 줄에 걸쳐 스프링 MVC 속성을 설정  
      //         .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter))
      //         .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
      //         .build();
      converters.add(0, new MappingJackson2HttpMessageConverter(JsonObjectMapperBuilder.build()));
  }


}/// ~
