package com.jirepo.core.web.exception;


import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;





/** 에러가 발생하는 경우 클라이언트에 전달할 에러 메시지 객체. Spring Validation은 고려되지 않음 */
@Getter
@Builder
public class ErrorResponse {

    /** 메시지 생성 시간. 이거 필요한지...  */
    //private final LocalDateTime timestamp = LocalDateTime.now();
    /** 에러 코드. {@link BaseBizErrorCode} enum 의 이름 */
    private final String code;
    /** 사용자에게 보여질 메시지 */
    private final String message;
    
    /**
     * 에러코드는 ResponseEntity에 담아 전달한다. 클라이언트에는 JSON으로 변환하여 전달한다.
     * @param errorCode 에러코드
     * @return  ResponseEntity 객체
     */
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        // 메시지 다국어 처리는 여기서 한다.아직 구현하지 않음 
        return ResponseEntity
                .status(errorCode.getStatus().getStatusCode())  //에러 코드에 정의된 Http Status 
                .body(ErrorResponse.builder()
                        .code(errorCode.getCode())        // 오류 코드 
                        .message(errorCode.getMessage())  // 오류 메시지
                        .build()
                );
    }
}///~