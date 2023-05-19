package com.jirepo.core.web.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 
 * ErrorController는 end point가 존재하지 않을 때  발생하는 오류를 처리한다. 
 * end point가 존재하는 것은 {@literal @}Exceptionhandelr를 사용한다. 
 */
@Controller
public class BaseErrorController implements ErrorController {

    /**
     * end point가 존재하지 않을 때  발생하는 오류를 처리한다.
     * @param request   요청정보
     * @return
     *   404 또는 400 에러 페이지 
     */
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else {
                return "error/500";
            }
        }

        return "error/500";
    }
    
}
