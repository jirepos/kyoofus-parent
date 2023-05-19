package com.jirepo.core.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * BaseBizException을 발생시키는 경우 여기에 정의된 Status Code만을 사용한다. 
 */
@Getter
@AllArgsConstructor
public enum HttpStatusEnum {
    /** 성공 */
    OK(200, "OK"), 
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /** 인증이 필요함 */
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    /** 권한이 없는 자원에 접근시 */
    FORBIDDEN(403, "FORBIDDEN"),
    /** 시스템 오류일 때 */
    INTERAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    /** 비즈니스 오류일 때 */
    BAD_REQUEST(400, "BAD_REQUEST");

    /** Http Status */
    private int statusCode; 
    /** Http Status 메시지 */
    private String message; 
    
}
