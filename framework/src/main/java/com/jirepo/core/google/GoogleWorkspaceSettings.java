package com.jirepo.core.google;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;




@ConfigurationProperties(prefix="google.workspace")
@Getter
@Setter
public class GoogleWorkspaceSettings {

    /** 워크스페이스 관리자 아이디(이메일) */
    private String adminUserId; 
    /** 워크 스페이스 도메인 */
    private String domain;
    /** 서비스 어카운트 이메일 */
    private String serviceAccountEmail;
    /** 서비스 키 파일 경로 */
    private String serviceKeyFilePath; 
    
}
