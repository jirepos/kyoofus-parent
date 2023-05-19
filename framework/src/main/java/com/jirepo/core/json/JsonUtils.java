package com.jirepo.core.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 프로그램적으로 JSON을 처리할 때 사용하기 위한 Utility 클래스이다.
 */
public class JsonUtils {

	/** ObjectMapper */
	private static final ObjectMapper mapper = JsonObjectMapperBuilder.build();

	/**
	 * 객체를 JSON으로 변환한다.
	 * 
	 * @param object
	 * @return
	 *         JSON 문자열
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static String toJSON(Object object)
			throws JsonGenerationException, JsonMappingException, JsonProcessingException {
		String jsonInString = mapper.writeValueAsString(object);
		return jsonInString;
	}// :

	/**
	 * JSON을 객체로 변환한다.
	 * 
	 * @param jsonString JSON 문장졀
	 * @param clazz      Class Type
	 * @return
	 *         Type으로 전달된 객체를 반환한다.
	 */
	public static <T> Object toObject(String jsonString, Class<T> clazz) {
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}// :

	/**
	 * JSON을 객체로 변환한다.
	 * 
	 * @param <T>  반환할 객체의 타입
	 * @param json String JSON 문자열
	 * @param t    반환할 객체의 타입
	 * @return 반환할 객체
	 */
	public static <T> List<T> toList(String json, Class<T> t) {
		try {
			JavaType jType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, t);
			// TypeReference tr = new TypeReference<ObjectArrayForJSON<T>>() {};
			return mapper.readValue(json, jType);
		} catch (Exception e) {
			// Ignores exception
			// e.printStackTrace();
			return new ArrayList<T>();
		}
	}// :

}/// ~
