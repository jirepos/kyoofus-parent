package com.jirepo.core.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jirepo.core.config.settings.AppSettings;
import com.jirepo.core.google.GoogleApiSettings;
import com.jirepo.core.util.YamlMessageSource;

/**
 * Application에서 사용할 Bean을 생성한다.
 */
@Configuration
public class AppConfig {

    // @Bean
    // public ResourceBundleMessageSource messageSource() {
    // ResourceBundleMessageSource messageSource = new
    // ResourceBundleMessageSource();
    // messageSource.setDefaultEncoding("UTF-8");
    // messageSource.setBasename("com/sogood/i18n/message");
    // return messageSource;
    // }//:

    @Bean
    public AppSettings appSettings() {
        return new AppSettings();
    }

    @Bean
    public GoogleApiSettings googleApiSettings() {
        return new GoogleApiSettings();
    }

    /**
     * 다국어 메시지 객체인 MessageSource를 반환한다. 다국어 파일은 application.yml에 설정해야 한다.
     * 사용할 때는 @Autowired MessageSource messageSource와 같이 주입하여 사용한다.
     *
     * @param basenames application.yml의 i18n-message-files
     * @return MessageSource
     * @see YamlMessageSource
     */
    @Bean
    public MessageSource messageSource(@Value("${i18n-message-files}") List<String> basenames) {
        YamlMessageSource messageSource = new YamlMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        // String[] basenames = new String[] {
        // "com/sogood/i18n/message",
        // "com/sogood/i18n/common"
        // };
        String[] messageList = new String[basenames.size()];
        messageList = basenames.toArray(messageList);
        messageSource.setBasenames(messageList);
        return messageSource;
    }// :

  
    // @Bean
    // public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    // return builder -> {
    // builder.simpleDateFormat(dateTimeFormat);
    // builder.serializers(new
    // LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
    // builder.serializers(new
    // LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
    // };
    // }

    // @Bean
    // public JavaTimeModule javaTimeModule() {
    // JavaTimeModule module = new JavaTimeModule();
    // module.addSerializer(new
    // LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
    // return module;
    // }

    // @Bean
    // @Primary
    // public ObjectMapper objectMapper() {
    // return new JsonObjectMapper();
    // }


    // @Bean
    // MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    //     MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    //     // ObjectMapper objectMapper = new ObjectMapper();
    //     // objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
    //     mappingJackson2HttpMessageConverter.setObjectMapper(new JsonObjectMapper());
    //     return mappingJackson2HttpMessageConverter;
    // }
}
