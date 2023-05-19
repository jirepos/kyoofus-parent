package com.jirepo.core.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonAlterLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    public JsonAlterLocalDateTimeSerializer() {
        super();
    }

    @Override
	public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException{
        System.out.println(">>>>>>> JsonLocalDateTimeSerializer.serialize");
		// jgen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        jgen.writeString(value.format(DateTimeFormatter.ofPattern("yyy-MM HH:mm:ss")));
	}
    
}
