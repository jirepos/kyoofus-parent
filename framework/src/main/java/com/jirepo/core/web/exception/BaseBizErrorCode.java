package com.jirepo.core.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 전역에서 사용할 에러 코드 */
@Getter
@AllArgsConstructor
public enum BaseBizErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatusEnum.INTERNAL_SERVER_ERROR, "S0001", "Internal Server Error"),
    FORBIDDEN(HttpStatusEnum.FORBIDDEN, "A0001", "접근 권한이 없습니다."),
    LOGIN_REQUIRED(HttpStatusEnum.UNAUTHORIZED, "A0002", "로그인이 필요합니다."),

    USER_NOT_FOUND(HttpStatusEnum.BAD_REQUEST, "C1000", "로그인 정보가 부정확합니다."),
    PASSWORD_NOT_MATCH(HttpStatusEnum.BAD_REQUEST, "C1001", "비밀번호가 부정확합니다."),
    OTHER_ERROR(HttpStatusEnum.BAD_REQUEST, "O1000", "기타 오류입니다.");

    /** Http Staus */
    private final HttpStatusEnum status;
    /** 에러 코드 */
    private final String code;
    /** 에러 메시지 */
    private final String message;

    @Override
    public String getCode() {
        return this.code; 
    }

    @Override
    public String getMessage() {
        return this.message;
    }
    // @Override 
    // public String getMessage(String message) {
    //     return Optional.ofNullable(message)  // null이 아니면 파라미터 반환 
    //            .filter(Predicate.not(String::isBlank)) // 비어 있는지 확인 
    //            .orElse(this.message);   // null이거나 비었으면 default 오류 
    // }

    @Override
    public HttpStatusEnum getStatus() {
        return this.status;
    }

}/// ~