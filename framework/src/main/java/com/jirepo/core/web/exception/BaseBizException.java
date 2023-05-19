package com.jirepo.core.web.exception;

import lombok.Getter;


/** 기본 비즈니스 익셉션 클래스이다. 새로운 비지니스 예외를 정의하려면 이 클래스를 상속 받아 구현한다.  */
@Getter
public class BaseBizException extends RuntimeException {
    /** 비지니스 에러 코드 및 메시지 */
    private ErrorCode errorCode;
    
    /**
     * 기본 생성자
     * @param errorCode 에러코드 
     */
    public BaseBizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * 예외 로그를 생성하기 위한 생성자 
     * @param errorCode 에러 코드 
     * @param message  에러 메시지 
     * @param cause  에러 
     */
    public BaseBizException(ErrorCode errorCode,  Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}