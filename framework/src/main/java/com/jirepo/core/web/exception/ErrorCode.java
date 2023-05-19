package com.jirepo.core.web.exception;

/** 
 * 모든 에러 코드는 enum으로 정의하는데 이 인터페이스를 구현한다. 
 */
public interface ErrorCode {
    /** 에러의 Http Status. */
    HttpStatusEnum getStatus();
    /** 공통 표준으로 정한 에러 코드값 */
    String getCode();
    /* 공통 표준으로 정한 에러메시지 */
    String getMessage();
    
}
