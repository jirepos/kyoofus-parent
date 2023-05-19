package com.jirepo.core.web.interceptor;

import lombok.Getter;
import lombok.Setter;

/** 인터셉터 설정 */
@Getter
@Setter
public class InterceptorSetting {
    /** interceptor 이름 */
    private String name;
    /** 인터셉터 설명 */
    private String description;
    /** 인터셉터가 처리할 패턴 */
    private String[] includes;
    /** 인터셉터가 처리하지 않을 패턴 */
    private String[] excludes;
  }