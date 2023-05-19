package com.jirepo.core.web.util.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jirepo.core.web.util.RequestContextHolderUtils;

public class SessionUtils {


    /**
     * HttpSession에 로그인한 사용자의 정보를 넣고 뺄 때 사용하는 키
     */
    public static final String SESSION_SIGNIN_USER_KEY = "SESSION_SIGNIN_USER_KEY";
    public static final String SESSION_RSA_KEY = "SESSION_RSA_KEY";
    public static final String JWT_KEY = "PYXKdsG+zPOF6zxGBBbUT7+S4XLcgOZcYMDDuoGrrsg="; 


    /**
     * 0(zero) 또는 음수인 경우에는 session이 timeout되지 않음
     */
    public static final int MAX_INACTIVE_INTERVAL = (60 * 60);

    /**
     * 세션을 반환한다. 세션이 없으면 새로 생성한다.
     *
     * @param request request 객체
     * @return HttpSession
     */
    public static HttpSession getSession(HttpServletRequest request) {
        return getSession(request, MAX_INACTIVE_INTERVAL);
    }//:

    public static HttpSession getSession(HttpServletRequest request, int sessionTimeout) {
        // true : 세션이 없다면 생성한다.
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(sessionTimeout);
        return session;
    }//:

    /**
     * 세션 아이디를 반환한다.
     *
     * @param request HttpServletRequst
     * @return 세션아이디
     */
    public static String getSessionID(HttpServletRequest request) {
        HttpSession sess = getSession(request);
        return sess.getId();
    }

    /**
     * 세션을 무효화 시킨다. 로그아웃 처리할 때 사용
     *
     * @param request
     */
    public static void invalidate(HttpServletRequest request) {
        getSession(request).invalidate();
    }//:

    /**
     * 세션에 객체를 저장한다.
     *
     * @param request HttpServletRequest
     * @param key     세션에 담을 데이터의 키
     * @param data    세션에 담을 데이터
     */
    public static void setObject(HttpServletRequest request, String key, Object data) {
        HttpSession session = getSession(request);
        synchronized (session) {
            session.setAttribute(key, data);
        }
    }//:

    /**
     * 세션에 객체를 저장한다. 
     * @param key 세션에 담을 데이터의 키
     * @param data 세션에 담을 데이터
     */
    public static void setObject(String key, Object data) {
        setObject(RequestContextHolderUtils.getHttpServletRequest(), key, data);
    }

    /**
     * 세션에 저장된 객체를 가져온다. 
     * @param request HttpServletRequest
     * @param key   세션에 담을 데이터의 키
     * @return 세션에 담긴 데이터
     */
    public static Object getObject(HttpServletRequest request, String key) {
        Object obj = null;
        HttpSession session = getSession(request);
        synchronized (session) {
            obj = session.getAttribute(key);
        }
        return obj;
    }//:

    /**
     * 세션에 저장된 객체를 가져온다.
     * @param key 세션에 담을 데이터의 키
     * @return 세션에 담긴 데이터
     */
    public static Object getObject(String key) {
        return getObject(RequestContextHolderUtils.getHttpServletRequest(), key);
    }

    /**
     * 로그인 사용자의 정보를 세션에 넣는다.
     *
     * @param signedUser 사용자 정보
     */
    public static void setSignedUser(Object signedUser) {
        HttpServletRequest request =  RequestContextHolderUtils.getHttpServletRequest();
        setObject(request, SESSION_SIGNIN_USER_KEY, signedUser);
    }//:

    /**
     * 로그인 사용자 객체를 반환한다.
     *
     * @return
     */
    public static Object getSignedUser() {
        HttpServletRequest request = RequestContextHolderUtils.getHttpServletRequest();
        return getObject(request, SESSION_SIGNIN_USER_KEY);
    }//:

    /**
     * 로그인 여부를 판단한다.
     *
     * @param request HttpServletRequest
     * @return 세션에 로그인된 사용자 정보가 있은 true, 아니면 false를 반환한다.
     */
    public static boolean isSigned() {
        HttpServletRequest request = RequestContextHolderUtils.getHttpServletRequest();
        Object o = getObject(request, SESSION_SIGNIN_USER_KEY);
        return (o == null) ? false : true;
    }//:


}

