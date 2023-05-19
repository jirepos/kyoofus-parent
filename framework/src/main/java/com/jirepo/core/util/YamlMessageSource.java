package com.jirepo.core.util;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.context.support.ResourceBundleMessageSource;

import dev.akkinoc.util.YamlResourceBundle;

// import net.rakugakibox.util.YamlResourceBundle;


/**
 * 다국어 메시지를 처리하기 위한 MessageSource 구현체이다. 이 클래스는 Yaml 파일을 
 * 다국어 파일로 사용한다. 
 * @see https://github.com/akkinoc/yaml-resource-bundle
 */
public class YamlMessageSource extends ResourceBundleMessageSource {
  @Override
  protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
    // 2.4 버전 사용 시 , IntelliJ에서는 12 버전 사용 
    // IntelliJ에서는  YamlResourceBundle.Control.INSTANCE 이 오류 발생 
    // 원인은 모름. 
    return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
  }
}/// ~
