package com.jirepo.core.web.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorDemoController {
    @GetMapping("/error/demo-error")
    public String handleError(HttpServletRequest request) {
        // 에러 페이지 처리 
        return "error/demo-error";
    }
}
