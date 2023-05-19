package com.jirepo.core.google;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jirepo.core.config.util.ApplicationContextHolder;
import com.jirepo.core.util.ClassPathFileUtil;


/**
 * 구글 OAuth 2 인증을 위한 Utility class이다.
 */
public class GoogleOauthUtil {


    /**
     * 
     * 사용자의 리프레시 토큰이 존재하는지 체그한다.
     * 
     * @param gmailId 사용자의 gmail ID
     * @return
     *         Refresh Token이 존재하면 true, 아니면 false
     */
    public static boolean checkRefreshToken(HttpServletRequest request, HttpServletResponse response, String gmailId) {
        String refreshTokenString = GoogleOauthUtil.getRefreshTokenFromFile(gmailId);
        if (refreshTokenString == null || refreshTokenString.length() == 0) {
            // if a refresh token doesn't exist, this method will redirect to a consent
            // screen
            return false;
        } else {
            // if a refresh token exists
            // execute synchronization
            return true;
        }
    }// :

    /**
     * Google OAuth 연동 시 콜백으로 돌아온 응답 값을 체크한 다음에 Access Token을 가져오고 Refresh Token을
     * 저장한다.
     * 
     * @param request  웹요청 객체
     * @param response 웹응답 객체
     * @param gmailId  Gmail ID
     * @return
     *         에러가 없으면 true를 린턴 아니면 false를 리턴
     * 
     * @throws Exception
     */
    public static boolean googleResponseCheck(HttpServletRequest request, HttpServletResponse response, String gmailId)
            throws Exception {
        String error = request.getParameter("error");
        if (error != null) {
            System.out.println("Error:" + error);
            return false;
        }
        String code = request.getParameter("code");
        System.out.println("code: " + code);
        if (code == null) {
            return false;
        }
        getAccessTokenAndSave(code, gmailId);
        return true;
    }// :

    /**
     * 저장된 토큰을 읽어온다. 토큰이 없으면 null을 반환한다.
     * 
     * @param userId 사용자 아이디
     * @return
     *         저장된 토큰 or 토큰이 없으면 null
     */
    public static String getRefreshTokenFromFile(String userId) {

        String tokenFileName = "tokens/" + userId + ".token";

        try {
            String refreshToken = ClassPathFileUtil.readFile(tokenFileName);
            return refreshToken;
        } catch (Exception e) {
            return null;
        }
    }// :

    /**
     * 저장된 토큰으로 Access Token을 요청한다.
     * 
     * @param refreshToken 저장된 Refresh Token
     * @return
     *         TokenResponse 객체
     * @throws Exception
     */
    public static TokenResponse getRefreshToken(String refreshToken) throws Exception {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");

        // transport - HTTP transport
        // jsonFactory - JSON factory
        // refreshToken - refresh token issued to the client
        // clientId - client identifier issued to the client during the registration
        // process
        // clientSecret - client secret
        TokenResponse tokenresponse = new GoogleRefreshTokenRequest(
                new NetHttpTransport(), new JacksonFactory(),
                refreshToken,  settings.getClientId(),  settings.getClientSecret())
                .execute();
        return tokenresponse;
    }// :

    /**
     * OAuth2 인증 화면으로 리다이렉트한다.
     * 
     * @param request  웹요청 객체
     * @param response 웹응답 객체
     * @param callbackUrl callback URL
     * @throws Exception
     */
    public static void showConsentScreen(HttpServletRequest request, HttpServletResponse response, String callbackUrl)
            throws Exception {

        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");

        AuthorizationRequestUrl authUrl = new AuthorizationRequestUrl( GoogleApiConstant.GOOGLE_OAUTH2_AUTH_URL,settings.getClientId(), Arrays.asList("code"));
        authUrl.setRedirectUri(callbackUrl);
        authUrl.set("access_type", "offline");
        // authUrl.setScopes(Arrays.asList(CalendarScopes.CALENDAR,
        // DocsScopes.DOCUMENTS_READONLY)); // 범위는 우선 Calendar만 지정함
        //authUrl.setScopes(GoogleApiConstant.GOOGLE_OAUTH2_SCOPES); // 범위는 우선 Calendar만 지정함
        authUrl.setScopes(settings.getScopes()); // 범위는 우선 Calendar만 지정함
        String redirectUri = authUrl.build();
        response.sendRedirect(redirectUri);
    }// :



    /**
     * Access Token을 구한다.
     * 
     * @param code OAuth2 로그인 후 받아온 코드값
     * @return
     *         TokenResponse 객체를 반환한다.
     * @throws IOException
     */
    public static TokenResponse getAccessToken(String code) throws IOException {

        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");

        GenericUrl rurl = new GenericUrl(GoogleApiConstant.GOOGLE_OAUTH2_TOKEN_REQ_URL);
        rurl.set("client_id", settings.getClientId());
        rurl.set("client_secret", settings.getClientSecret());

        // AuthorizationCodeTokenRequest
        // Access Token Request에서 명시된 것처럼 인증코드(authorization code)를 사용하여
        // access token에 대한 OAuth 2.0 요청
        TokenResponse tokenresponse = new AuthorizationCodeTokenRequest(
                new NetHttpTransport(), new JacksonFactory(), rurl, code)
                .setRedirectUri(settings.getCallbackUrl())
                .execute();
        return tokenresponse;
    }// :

    /**
     * Token을 클래스 패스의 경로에 저장한다.
     * 
     * @param userId       사용자 아이디
     * @param refreshToken Refresh Token
     * @throws IOException
     */
    private static void saveRefressToken(String userId, String refreshToken) throws IOException {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        // java.lang.Class.getProtectionDoamin()은 ProtectionDomain을 반환한다.
        // getLocaion()은 클래스가 있는 디렉토리 경로를 반환한다.
        URL url = GoogleOauthUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String classPathRoot = url.getFile().substring(1);

        File file = new File(classPathRoot +  settings.getTokenPath());
        // if the directory doesn't exist, create the directory
        if (!file.exists()) {
            if (!file.mkdir()) {
                System.out.println("★★★★★★ Failed to make a directory.");
            } else {
                System.out.println("★★★★★★ Succeeded to make a token directory.");
            }
        }
        String tokenFileName = classPathRoot + settings.getTokenPath()+ "/" + userId
                + ".token";
        System.out.println("■■■■■■ TokenFileName:" + tokenFileName);
        FileOutputStream fos = new FileOutputStream(new File(tokenFileName));
        fos.write(refreshToken.getBytes());
        fos.close();
    }// :

    /**
     * 토큰을 구글로부터 가져오고 디렉토리에 저장한다.
     * 
     * @param code   구글로부터 전달된 응답 코드
     * @param userId 사용자 아이디
     * @return
     *         TokenReponse 객체
     * @throws Exception
     */
    public static TokenResponse getAccessTokenAndSave(String code, String userId) throws Exception {
        TokenResponse token = null;
        token = getAccessToken(code);
        saveRefressToken(userId, token.getRefreshToken());
        return token;
    }// :

}
