package com.jirepo.core.web.interceptor;

import org.springframework.stereotype.Service;

@Service 
public class DemoInjectionService {
    public void  getDemo() {
        System.out.println("Demo Injection");
    }
    
}
