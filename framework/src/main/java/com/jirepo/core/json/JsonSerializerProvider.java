package com.jirepo.core.json;

import java.util.List;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;



/***
 * Null인 경우에 클래스에 맞는 시리얼라이저를 공급한다. 
 */
public class JsonSerializerProvider extends DefaultSerializerProvider {

    public JsonSerializerProvider() {
        super();
    }

    public JsonSerializerProvider(JsonSerializerProvider provider, SerializationConfig config,
            SerializerFactory jsf) {
        super(provider, config, jsf);
    }

    @Override
    public JsonSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new JsonSerializerProvider(this, config, jsf);
    }

    @Override
    public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
        if (property.getType().getRawClass().equals(String.class)) {
            return JsonStringSerializer.EMPTY_STRING_SERIALIZER_INSTANCE;
        } else if (property.getType().getRawClass().equals(List.class)) {
            return JsonListSerializer.EMPTY_LIST_SERIALIZER_INSTANCE;
        } else {
            return super.findNullValueSerializer(property);
        }
    }
}
