package com.jirepo.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;




/**
 * String이 null인 경우에 ""로 시리얼라이즈하기 위한 시리얼라지저이다. 
 * 사용하지 말아야 한다. 
 */
public class JsonStringSerializer  {
//public class JsonStringSerializer extends JsonSerializer<Object> {
	/**
	 * Searialzer 인스턴스 
	 */
    public static final JsonSerializer<Object> EMPTY_STRING_SERIALIZER_INSTANCE = new EmptyStringSerializer();

    // /**
    //  * Constructor 
    //  */
    // public JsonStringSerializer() {}

    // @Override
    // public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
    //     jsonGenerator.writeString("");
    // }
    
    private static class EmptyStringSerializer extends JsonSerializer<Object> {
        public EmptyStringSerializer() {}

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeString("");
        }
    }

}///~