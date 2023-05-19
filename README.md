# Framework 프로젝트 

스프링부트 2.7 기반의 프로젝트로 다른 프로젝트의 상위 프로젝트입니다. 


```shell
📁java/com/jirepo/core 
├── 📁config 
│   ├── 📁async
│   │   ├── 📄AsyncConfig.java  # Spring 에서 Async 설정 
│   ├── 📁cache
│   │   ├── 📄CacheConfigBase.java  # Spring 에서 Cache 설정
│   ├── 📄AppConfig.java # Application에서 사용할 Bean을 생성
│   ├── 📄DispatcherServletconfig.java  # WebMvcConfigurer를 구현 
📄pom.xml # 프로젝트 최상위 pom 파일
```


