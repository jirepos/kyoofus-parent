package com.jirepo.core.json;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.jirepo.core.constants.PropertyConstants;

public class JsonObjectMapperBuilder {

    // /** java.util.Date를 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd" */
    // private static final String dateFormat = "yyyy-MM-dd"; 
    // /** java.time.LocalDateTime을 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd HH:mm:ss" */
    // // private static final String localDateTimeFormat =  "yyyy HH:mm:ss";  //
    // private static final String localDateTimeFormat =  "yyyy-MM-dd HH:mm:ss";  //
    // /** java.time.LocalDate를 시리얼라이즈하기 위한 포맷. 예) "yyyy-MM-dd" */
    // private static final String localDateFormat = "yyyy-MM-dd";
    
    /**
     * JavaTimeModule을 생성한다. 
     */
    private static JavaTimeModule createJavaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        // SimpleModule module = new SimpleModule(); 과 같이 SimpleModule을 사용해도 오류나지 않는다. 
        // 아래와 같이 Jackson이 제공하는 LocalDateTimeSerializer를 사용하면 동작한다. 
        // 시리얼라이즈할 때  Bean에 @JsonFormat이  있다면 그것이 우선한다.  
        // LocalDateTimeSerializer localTimeSz = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimeFormat));
        // 
        // LocalDateTimeSerializer를 상속하여 serialize() 메소드를 오버라이딩하여 사용하면 동작하지 않는다.
        // 원인은 파악되지 않았다. 
        // JsonSerializer를 상속하여 구현하면 동작한다. 이 경우 @JsonFormat이 무시된다. 
        // @JsonSerialize(as = LocalDateTime.class)를 사용하면 동작한다.
        JsonSerializer<LocalDateTime> localTimeSz = new JsonLocalDateTimeSerializer();
        LocalDateSerializer localDateSz   = new LocalDateSerializer(DateTimeFormatter.ofPattern(PropertyConstants.LOCAL_DATE_FORMAT));
        module.addSerializer(LocalDateTime.class, localTimeSz);
        module.addSerializer(LocalDate.class, localDateSz);
        return module; 
    }//:

    public static ObjectMapper build() {
        // Jackson 2.10 부터 ObjectMapper 생성 방식 변경됨
        // Jackson 2.10이상 또는 3.0에서는 JsonMapper의 builder패턴으로 모듈을 추가하고, 2.9 이하버전에서는 ObjectMapper를 
        // 생성하고 registerModules로 모듈을 추가한다. 
        // ObjectMapper mapper = JsonMapper.builder().addModule(module).build();
        // jackson 2까지는 jackson-module-parameter-names, jackson-datatype-jdk8, jackson-datatype-jsr310을 추가해주어야 하는데
        // Jackson 3부터는 Java 8이 필수라서 기본지원이된다. 
        ObjectMapper mapper = JsonMapper.builder()
                  //.addHandler(handler)
                  //.addSerializer(serializer)
                  //.addDeserializer(deserializer)
                  .addModule(createJavaTimeModule())
                  .serializationInclusion(JsonInclude.Include.ALWAYS) // 모든 값을 출력한다 , default
                  //.serializationInclusion(JsonInclude.Include.NON_NULL) // NULL인 필드는 출력하지 않는다.
                  // 시간을 timestamp 숫자가 아닌, 문자열로 포맷팅한다. 기본 ISO 포맷
                  .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                  // 객체의 필드가 모두 private이어서 getter가 없을 경우, 에러가 발생하지 않도록 한다.
                  .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                  .configure(SerializationFeature.INDENT_OUTPUT, true) // pretty print
                  .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                  // 모르는 property에 대해 무시하고 넘어간다
                  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)  
                  // ENUM 값이 존재하지 않으면 null로 설정한다. Enum 항목이 추가되어도 무시하고 넘어가게 할 때 필요하다.
                  .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                  // null 값 허용 여부. 알 수 없는 속성 디시리얼라이즈 시 예외 발생 안함
                  .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                  //.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) // single quote 허용 
                  //.configure(JsonParser.Feature.ALLOW_COMMENTS, true) // 주석 허용 
                  //.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true) // YAML 주석 허용
                  // 아래 세 개는 deprecated 되었다. 
                //   .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                //   .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                //   .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
                  // null인경우 렌더링을 하지 않는다. 
                
                  .build();
        // simpleDateFormat(), dateFormat()을 설정하면 ObjectMapper가 non-thread-safe 하게 돼 버린다. 
        // 하지말고, java.util.Date도 사용하지 말 것.
        // java.util.Date 타입을 시리얼라이즈하기 위한 포맷 설정
        // time zone을을 설정하려면 SimpleDateFormat에 설정
        // @JsonFormat을 사용하면 생성시 설정된 것은 무시된다. 
        mapper.setDateFormat(new SimpleDateFormat(PropertyConstants.DATE_FORMAT)); 
        // this.setSerializerProvider(new JsonSerializerProvider()); // String, List 을 "", []로 
        return mapper;
    }// :

}///~
