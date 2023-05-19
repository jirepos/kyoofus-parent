package com.jirepo.core.google;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="google.oauth")
@Getter
@Setter
public class GoogleApiSettings {

    /** 앱이름 */
    private String applicationName; 
    /** 구글 클라이언트 아이디  */
    private String clientId; 
    /** 구글 클라이언트 시크릿 */
    private String clientSecret;
    /** 토근 저장할 경로 */
    private String tokenPath;
    /** OAuth 콜백 URL  */
    private String callbackUrl;  
    /** 요청할 권한 범위 */
    private List<String> scopes;
    /** 구글 드라이브 파일 저장 경로 */
    private String saveFolder; 
    
    
}
