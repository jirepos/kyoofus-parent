package com.jirepo.core.web.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 쿠키를 다루는 편리한 메소들을 제공한다. 
 * 
 * max-age 속성도 expires 속성도 지정되어 있지 않은 경우 브라우저가 닫힐 때까지 유효하다.
 * 0을 입력하면 쿠키가 삭제된다. 
 * -1을 입력하면 브라우저 종료시 제거된다(기본값)
 * 
 * @author behap
 * @See https://attacomsian.com/blog/cookies-spring-boot
 */
public class CookieUtils {

	/**
	 * 모든 쿠키를 배열로 반환한다.
	 * 
	 * @param request HttpServletRequest 객체 
	 * @return
	 * 		Cookie 배열 
	 */
	public static Cookie[] readAllCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		return cookies;
	}//:
	
	
	/**
	 * 특정 쿠키의 값을 반환한다. 
	 * 
	 * @param request HttpServletRequest 객체
	 * @param cookieName 쿠키 이름
	 * @return
	 * 		쿠키 값. 없으면 null을 반환한다.  
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		
		Cookie[] cookies = readAllCookie(request);
		
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals(cookieName) ) {
					return c.getValue();
				}
			}
		}
		return null; 
	}//:
	
	
	/**
	 * 쿠키를 설정한다. 
	 * 
	 * 만료기간(expiration)이 정해지지 않은 쿠키는 세션이 만료가 되지 않는 한 지속된다. 그런 쿠키를
	 * session cookie라고 부른다. 브라우저가 종료되거나 쿠키를 clear할 때까지 유효하다. 
	 * 
	 * @param response HttpServletResponse 객체 
	 * @param cookieName 쿠키 이름 
	 * @param cookieValue 쿠키 값 
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		response.addCookie(cookie);
	}//:

	
	
	/**
	 *  쿠키를 설정한다. 만료는 초단위로 줄 수 있다.  
	 * 
	 * @param response HttpServletResponse 객체 
	 * @param cookieName 쿠키 이름
	 * @param cookieValue 쿠키 값 
	 * @param expiry 경과시간 
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int expiry) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		
		// 범위를 지정하지 않으면 브라우저에서 쿠키를 설정하는 데 사용 된 경로에 대해서만 쿠키가 서버로 전송됩니다.
		cookie.setPath("/");
		cookie.setMaxAge(expiry); // 만료시간 지정 (초단위)
		response.addCookie(cookie);
	}//:
	/**
	 * 쿠키에 저장한다. 
	 * @param cookieName 쿠키 이름
	 * @param cookieValue 쿠키 값
	 * @param expiry 		만료시간(초단위)
	 */
	public static void addCookie(String cookieName, String cookieValue, int expiry) {
		addCookie(RequestContextHolderUtils.getHttpServletResponse(), cookieName, cookieValue, expiry);
	}

	/**
	 * 쿠키를 삭제한다. MaxAge를 0으로 설정하고 그 값을 unset하면 된다. 
	 * 
	 * @param response
	 * @param cookieName
	 */
	public static void deleteCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}//:

}/// ~
