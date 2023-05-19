package com.jirepo.core.json;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * java.util.List가 null일 경우에 [] 로 시리얼라이즈하기 위한 시리얼라지저이다. 
 */
public class JsonListSerializer {
// public class ListSerializer extends JsonSerializer<Object> {
    
	/**
	 * Serializer 인스턴스 
	 */
	public static final JsonSerializer<Object> EMPTY_LIST_SERIALIZER_INSTANCE = new EmptyListSerializers();

    
    // public ListSerializer() {}

    // @Override
    // public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    //         throws IOException, JsonProcessingException {
    //     jsonGenerator.writeString("");
    // }

    private static class EmptyListSerializers extends JsonSerializer<Object> {
        public EmptyListSerializers() {}

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException, JsonProcessingException {
            jsonGenerator.writeObject(new ArrayList<String>());
        }
    }
}