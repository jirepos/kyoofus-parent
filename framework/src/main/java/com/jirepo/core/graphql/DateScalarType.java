package com.jirepo.core.graphql;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.jirepo.core.util.DateTimeUtils;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;



/**
 * public interface Cosercing<I, O>
 *  - Input type : 클라이언트에서 받는 타입
 *  - Ouptut Type : 서버에서 반환하는 타입 
 */
@Component
public class DateScalarType extends GraphQLScalarType {
// public class DateScalarType  {
       
    public DateScalarType() {
        super("Date", "Date scalar type", new Coercing<Object, Object>() {
            // serialize : dataFetcherResult의 결과로 return되는 java object를 scalar type 에 맞게 변환하기 위한 메소드
            @Override
            public Object serialize(Object input) {
                if(input instanceof LocalDateTime) {
                    return DateTimeUtils.format((LocalDateTime)input);
                }else {
                    return input.toString();
                }
            }

            // parseValue : query에서 값을 parsing 할 때 사용되는 메소드
            @Override
            public Object parseValue(Object input) {
                return input;
            }

            // parseLiteral : 요청받은 query를 validation 할 때 사용되는 메소드
            @Override
            public Object parseLiteral(Object input) {
                if(input == null) {
                    return null;
                }
                return input.toString();
            }
        });
    }
    
}
