package com.jirepo.core.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


/**
 * java.util.Date를 디시리얼라이즈하기 위한 디시리얼라이저이다. 현재는 사용하지 않는다. 
 * 필요한 경우 로직을 추가하여 사용한다. 
 * 사용하지 말아야 한다. 
 */
public class JsonDateDeserializer extends JsonDeserializer<Date> {

    //private static String defaultDateFormat = "yyyy-MM-dd aaa hh:mm:ss";
    	
    /**
     * 클래스의 필드 정보를 구한다. 
     */
	private Field getField(String fieldName, Class<?> clazz) {
		Field fld = null;
		try {
			fld = clazz.getDeclaredField(fieldName);
			return fld; 
		}catch(Exception e) {
			if(clazz.getSuperclass() == null) {
				throw new RuntimeException("NoSuchFieldException");
			}else { 
				return getField(fieldName, clazz.getSuperclass());	
			}
		}
	}


    /**
     * JsonFormat Annotation이 적용된 것을 처리한다. 
     */
    private Date parseDate(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
        try { 
            //Field field = p.getCurrentValue().getClass().getDeclaredField(p.getCurrentName());
            Field field =  getField(p.getCurrentName(), p.getCurrentValue().getClass()); 
            JsonFormat anno = field.getAnnotation(JsonFormat.class);
            if( anno != null) {
                // JsonFormat는 TimeZone을 설정하거나 사용할 수 없음. 고정되어 있음
                SimpleDateFormat sdf = new SimpleDateFormat(anno.pattern(), Locale.ENGLISH);
                if(p.getText() == null || p.getText().equals("")) { 
                    return null;
                }else { 
                   return  sdf.parse(p.getText().toString());	 
                }
            }
            return null;
        }catch(Exception e) {
            // 여기서는 에러처리하지 않음
            // e.printStackTrace();
            return null; 
        }
    }
    

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        Date fstDate = parseDate(p, ctxt); 
        if(fstDate != null) {  // JsonFormat Annotation이 적용된 것을 처리한다.
            return fstDate;
        }

        if (p.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            if(p.getText() == null || p.getText().equals(""))  {
                return null;
            }
            String value = p.getText().toString();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date d = null;
            try {
                d = sf.parse(value);
            } catch (Exception e) {
                return null; 
            }
            return d; 
            
        }
        return null; 
    }
    
}
