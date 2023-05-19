package com.jirepo.core.config.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


/** 
 * applicaiton.yml 파일의 settings 설정을 읽기 위한 것 
 */
@ConfigurationProperties(prefix="settings")
@Getter
@Setter
public class AppSettings {
    // 브라우져에서 파라미터를 암호화해서 전달하고 
    // Filter에서 파라미터를 복호화한다음 포워딩 할 때 사용 
    /** 파라미터 암호화 여부 */
    private boolean parameterEncryption;
}
