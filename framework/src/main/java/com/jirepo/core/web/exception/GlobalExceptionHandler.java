package com.jirepo.core.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;



/**
 * 공통 전역 에러 처리 클래스이다. 시스템 Exception은 처리하지 않는다. CustomExeption 만 처리한다. 
 * 예외가 발생하면 {@literal @}ExceptionHandler에서 잡아서 처리한다. Controller에서는 try-catch 블럭을 사용하지 않는다. 
 * 
 * {@literal @}ControllerAdvice는 모든 Controller에서 발생하는 예외를 처리한다. 
 * Controller의 메서드에 적용된 {@literal @}ExceptionHandler는 그 컨트롤러에서 밠생하는 예외만 처리한다.
 * ExceptionHandler는 Controller에서 발생하는 에러를 잡아서 처리한다. Service, Repository에서 발생하는 예외는 제외한다. 
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * BaseBizException 처리. 
     * @param e  예외 객체
     * @return  ResponseEntity 객체
     */
    @ExceptionHandler({BaseBizException.class  })
    protected ResponseEntity<ErrorResponse>  handleBaseBizException(HttpServletRequest request, HttpServletResponse response, BaseBizException e) throws Exception {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        boolean flag = false;  // 테스트할 때만 사용한다. 
        if(flag) {
            // 응답 형태가 JSON 형식이 아닌 경우에는 페이지로 표시한다. 
            response.setStatus(HttpStatus.SC_SERVICE_UNAVAILABLE); // 여기서 상태값을 설정
            // getReqeustDispatcher(url).forward()하면 
            // 해당 컨트롤러에서 처리한다. 
            // Controller에서 @RequestMapping("/error/demo-error")으로 URL을 매핑한다. 
            // forward할 때 context path는 넣지 않아야  한다. redirect할 때는 필요하다. 
            // SpringBoot 2.7을 사용하고 있고  이코드가 작동한다. 
            request.getRequestDispatcher("/error/demo-error").forward(request, response); 
            return null;  // forward 한 다음에 null을 반환한다. 
        }else {
            return ErrorResponse.toResponseEntity(e.getErrorCode());
        }
    }//:

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public String handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return "error/404";
    }//:



    /**
     * DispatcherServlet은  mappedHandler가 존재하지 않은 때 
     * throwExceptionIfNoHandlerFound에 값에 따라, Exception을 throw하는 지의 여부가 결정된다. 
     * 기본설정은 false이기 때문에 프론트 404의 응답만을 보낸다. 
     * <pre>
     *  mvc:
     *   # 아래 설정을 하였지만 동작하지 않음 
     *   throw-exception-if-no-handler-found: true     # NoHandlerFoundException을 발생시키고 handlerExcpetion에서 잡을 수 있도록
     *   dispatch-options-request: false
     *  web:
     *  resources:
     *    add-mappings: false # 적절한 handler를 찾지 못한 경우 , 정적리소스로 생각해 classpath로 던진다. 그런 예외를 막기위한 설정 
     * </pre>
     * 일단, ErrorController를 사용하기로 햐였다. 
     * @param e
     * @return
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleNoHandlerFoundException(NoHandlerFoundException e) {
        return "error/404";
    }//:

    /**
     * Exception이나 RuntimeException이 발생한 경우, Accept가 application/json인 경우 
     * JSON으로 에러를 보낸다. 
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler({Exception.class, RuntimeException.class  })
    protected ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) throws Exception {
        if(request.getHeader("Accept").contains("application/json")) {
            // Ajax 요청이면 
            return ErrorResponse.toResponseEntity(BaseBizErrorCode.INTERNAL_SERVER_ERROR);
        } 
        // Ajax 요청이 아니면 오류페이지 보이도록 
        throw  e;
    }//:

}///~
