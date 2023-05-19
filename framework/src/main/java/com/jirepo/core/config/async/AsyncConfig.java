package com.jirepo.core.config.async;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * AsyncConfig 를 통해 Spring 에서 Async 설정을 어떻게 할지 알려줍니다.
 * AsyncConfig 에 @EnableAsync 를 선언함으로써 관련 설정을 수행합니다.
 * getAsyncExecutor 에 Bean 명을 선언한 이유는 또 다른 AsyncExecutor 를 만들수도 있을 것 같아서 설정했습니다.
 * AsyncConfigurer 를 implement 하거나 아니면 Bean 으로 설정해서 하거나 맞는 방식으로 쓰면 됩니다. 둘다 사용 가능합니다.
 * @Async("Bean명") 을 선언하면 됩니다
 */
@Configuration
@EnableAsync
public class AsyncConfig  implements AsyncConfigurer {

    
    // https://velog.io/@gillog/Spring-Async-Annotation%EB%B9%84%EB%8F%99%EA%B8%B0-%EB%A9%94%EC%86%8C%EB%93%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
    // @EnableAsync : Spring method에서 비동기 기능을 사용가능하게 활성화 한다.
    // private method는 사용 불가, public method만 사용 가능
    // self-invocation(자가 호출) 불가, 즉 inner method는 사용 불가
    // QueueCapacity 초과 요청에 대한 비동기 method 호출시 방어 코드 작성
    @Bean(name = "asyncTestExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  // 기본 실행 대기하는 Thread의 수**
        executor.setMaxPoolSize(2);   // 동시 동작하는 최대 Thread의 수
        executor.setQueueCapacity(10);  //MaxPoolSize 초과 요청에서 Thread 생성 요청시, 해당 요청을 Queue에 저장하는데 이때 최대 수용 가능한 Queue의 수,
        executor.setBeanName("asyncTestExecutor"); // bean 이름
        executor.setThreadNamePrefix("DDAJA-ASYNC-"); //  생성되는 Thread 접두사 지정
        executor.initialize();
        return executor;
    }
    
}///~
