package com.jirepo.core.util;

import java.lang.reflect.Constructor;

/**
 * Java Class를 처리하는 유틸리티 클래스이다.
 */
public class ClassUtils {
    /**
     * 클래스 인스턴스를 생성하는 메소드이다.
     * 
     * @param className 클래스 이름 예) com.jirepo.DemoBean, com.jirepo.DemoBean$Handler
     *                  (내부 클래스)
     * @return
     * @throws Exception
     */
    public static Object createInstance(String className) throws RuntimeException {
        try {
            ClassLoader classLoader = ClassUtils.class.getClassLoader();
            Class<?> aClass = classLoader.loadClass(className.trim());
            Constructor<?> constructor = aClass.getConstructor();
            return constructor.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}/// ~
