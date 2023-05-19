package com.jirepo.core.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    // @Value("${spring.redis.host}")
    // private String host;
    // @Value("${spring.redis.port}")
    // private int port;

    // // redis.windows-service.conf 파일의 
    // // requirepass에 패스워드가 설정되어 있음 
    // @Value("${spring.redis.password}")
    // private String password; 
    // @Value("${spring.redis.timeout}")
    // private long timeout;

    // application.yml 설정
    // spring:
    //   redis:
    //   host: 127.0.0.1
    //   port: 6379
    //   timeout: 3000 #ms connection timeout
    // //autoConfiguration에 의해 restTemplate과 redisConnectionFactory가 자동 생성되기 때문에 이렇게만 해도 redis연동이 끝난다.
    // @EnableAutoConfiguration(exclude={RedisReactiveAutoConfiguration.class})로 reactive redis template은 자동 설정을 disable시킬 수 있습니다.

    // pring-boot parent 2.2이상 버전에서는 자동 설정에 의해서 redis template 빈이 4개가 생성됩니다.
    // @Autowired RedisTemplate redisTemplate;
    // @Autowired StringRedisTemplate stringRedisTemplate;
    // @Autowired ReactiveRedisTemplate reactiveRedisTemplate;
    // @Autowired ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    // redisTemplate와 stringRedisTemplate의 차이는 Serializer(직렬화)입니다.
    // redisTemplate의 key, value serializer는 JdkSerializationRedisSerializer이고 
    // stringRedisTemplate의 serializer StringRedisSerializer입니다.
    // 직렬화 방식이 다르기 때문에 혼용해서 사용하는 건 안 됩니다.



    // RedisConnectionFactory는 AutoConfiguration에서 자동 생성되므로 Bean을 직접 생성하지 않아도 된다. 
    // @Bean
    // public RedisConnectionFactory redisConnectionFactory() {
    //     LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
    //     factory.setPassword(password);
    //     factory.setTimeout(timeout);
    //     return factory;

    //     // lettuceConnectionFactory.setHost("192.168.0.78") 
    //     // .setPassword(“password”);를 이용해 설정할 수 있으나 
    //     // application.properties나 .yml 파일에 설정하는것을 권장한다.
    // }



    // 만약 다른 직렬화 방식을 사용한다면 RedisTemplate bean을 생성해야 합니다. (RedisConnectionFactory은 자동 설정에 의해 생성된 빈을 주입받아 사용)
    // bean 네임을 "redisTemplate"로 하면 자동 설정(RedisAutoConfiguration)이 redisTempate은 생성하지 않는다.
    // 기본 redisTemplate의 직렬화 방식을 바꾸고 싶다면 redisTemplate 빈 생성
    @Bean
    @ConditionalOnMissingBean(
        name = {"redisTemplate"}
    )
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        //RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(RedisSerializer.string()); 
        // spring-devtools 때문에 역질력화할 때 오류 발생 , pom.xml에서 spring-devtools을 주석처리하면 오류 나지 않음 
        //redisTemplate.setValueSerializer(RedisSerializer.java());  
        // JSON 문자열로 넣기 위해서 Serializer를 다음과 같이 변경 
        redisTemplate.setValueSerializer(RedisSerializer.string());  

        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        return redisTemplate;
    }



    /**
     * 구독을 위해 리스너 등록 
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        // pub/sub은 항상 redis에 발행 된 데이터가 있는지 확인하고 있어야 하기 떄문에 Listener를 등록하여야 한다.
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }//:


}
