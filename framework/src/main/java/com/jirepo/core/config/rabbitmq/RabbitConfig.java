package com.jirepo.core.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
// import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
// import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** RabbitMQ Java Cconfiguration 클래스이다. */
@Configuration
public class RabbitConfig {

    // RabbitMQ 에서
    // Publisher는 메시지 발행자이고
    // Exchange는 Queue와 바인딩을 한다.
    // Consumer는 Queue를 구독하고 , Queue에 전달된 메시지를 수신한다.
    // 하나의 Exchange는 여러개의 Queue와 바인딩 될 수 잇다.
    // 즉, 하나의 exchange에 메시지를 전달하면 바인딩된 하나 이상의 큐들에 메시지가 전달된다.
    // exchange와 queue를 바인딩할 때에는 routing key라는 문자열을 설정하여 바인딩 한다.

    // https://velog.io/@devsh/Spring-Boot-%EB%A1%9C-RabbitMQ-Topic-Exchange-%EC%A0%81%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0

    /**
     * http://localhost:15672/
     * 접속하여 guest/guest로 로그인
     * 
     * RabbitMQ 서버에서 workierOne 이라는 큐를 생성한다.
     */
    @Bean
    Queue workerOneQueue() {
        return new Queue("workerOne", false);
    }

    /**
     * RabbitMQ 서버에서 workerTwo라는 큐를 생성한다.
     * 
     * @return
     */
    @Bean
    Queue workerTwoQueue() {
        return new Queue("workerTwo", false);
    }

    /** Topic Exchange. RabbitMQ 서버에서 task-exchange라는 exchagne를 생성한다. */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("task-exchange");
    }

    /** Exchange와 큐를 바인딩을 만든다. */
    @Bean
    Binding workerOneBinding(Queue workerOneQueue, TopicExchange topicExchange) {
        // RabbitMQ 서버에서
        // 'worker.one' 이라는 Rounting Key를 사용하여
        // workerOne이라는 큐에 바인딩을 했다.
        return BindingBuilder.bind(workerOneQueue).to(topicExchange).with("worker.one");
    }

    /** Exchange와 큐를 바인딩을 만든다. */
    @Bean
    Binding workerTwoBinding(Queue workerTwoQueue, TopicExchange topicExchange) {
        // 'worker.two'라는 Rounting Key를 사용하여
        // workerTwo이라는 큐에 바인딩을 했다.
        return BindingBuilder.bind(workerTwoQueue).to(topicExchange).with("worker.two");
    }

    // /**
    //  * Template을 커스텀하여 생성한다. 생성하지 않으면 Spring Boot에서 제공하는 디폴트 템플릿을 사용한다.
    //  * 
    //  * @param connectionFactory
    //  * @param messageConverter
    //  * @return
    //  */
    // @Bean
    // RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    //     // connectionFactory를 의존성 주입받아 생성
    //     RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    //     // 메시지 컨버터 등록
    //     rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    //     rabbitTemplate.setReplyTimeout(50000); // 50초, 타임아웃 지정
    //     return rabbitTemplate;
    // }



    // ConnectionFactory를 구성하지 않으면 SpringBoot에서 자동 구성해준다. 
    // 해당 설정을 커스텀하려면 ConnectionFactory 빈을 재정의한다. 

    // @Bean
    // public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
    //         ConnectionFactory connectionFactory) {
    //     final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    //     factory.setConnectionFactory(connectionFactory);
    //     factory.setDefaultRequeueRejected(false);
    //     factory.setMessageConverter(queueMessageConverter());
    //     factory.setChannelTransacted(true);
    //     factory.setAdviceChain(RetryInterceptorBuilder
    //             .stateless()
    //             .maxAttempts(MAX_TRY_COUNT)
    //             .recoverer(new RabbitMqExceptionHandler())
    //             .backOffOptions(INITIAL_INTERVAL, MULTIPLIER, MAX_INTERVAL)
    //             .build());
    //     return factory;
    // }


    // https://docs.spring.io/spring-amqp/reference/html/#receiving-messages
    // 비동기적으로 메시지를 수신하는 가장 쉬운 방법은 
    // @RabbitListener 어노테이션을 사용하는 것이다.
    // Rabbit listener endpoint로써 관리되는 빈의 method를 노출시키도록 한다. 
    // annotated endpoint infrasctructure는  RabbitListenerContainerFactory를 사용하여
    //  message listener container를 생성한다. 
    // 
    // Enable Listener Endpoint Annotations
    // To enable support for @RabbitListener annotations, you can add @EnableRabbit to one of your @Configuration classes. The following example shows how to do so:
    // @RabbitListener를를 지원하기 위해서는 
    // @Configuration 클래스의 하나에 @EnableRabbit를 추가해야 한다. 
    //
    // default로 infrastructure는 message listener container를 생성하기 위해 
    // 사용할 factory source로써 rabbitListenerContainerFactory라는 이름의 bean을 찾는다. 
    // https://docs.spring.io/spring-amqp/reference/html/#listener-concurrency
    // By default, the listener container starts a single consumer that receives messages from the queues.
    // 디폴트로, 리스너 컨테이너는 큐에서 메시지를 받는 하나의 컨슈머를 시작한다.
    // 
    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory factory)
    {
        SimpleRabbitListenerContainerFactory simpleFactory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(simpleFactory, factory);
        simpleFactory.setConcurrentConsumers(2); // 동시에 처리할 수 있는 consumer 수
        // 동적으로 업무량에 근거하여 동시성(concurrency)를 조절한다. 
        // https://docs.spring.io/spring-amqp/reference/html/#listener-concurrency
        // 각각의 queue에 대한 각각의 consumer는 분리된 채널을 사용하고 
        // concurrency는 rabbit client library에 의해 통제된다. 
        // 디폴트로,  DEFAULT_NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2 threads을 사용한다. 
        simpleFactory.setMaxConcurrentConsumers(2); // 최대 consumer 수
        // 아래 설정값에서 주의사항은 하나의 컨슈머가 5개를 받아서 처리할 때까지 다른 컨슈머가 메시지를 받지 않는다는 것이다.
        simpleFactory.setPrefetchCount(5);  // n개씩 가져온다. 이 설정 값은 consumer가 가져올 수 있는 최대 메시지 수를 의미한다. 
        // simpleFactory.setContainerCustomizer(container -> /* customize the container */);
        return simpleFactory;
    }



    // @Bean
    // MessageConverter messageConverter() {
    //   return new Jackson2JsonMessageConverter();
    // }

    // @Bean
    // public ConnectionFactory connectionFactory() {
    //     CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    //     connectionFactory.setAddresses(address);
    //     connectionFactory.setUsername(username);
    //     connectionFactory.setPassword(password);
    //     return connectionFactory;
    // }


}/// ~
