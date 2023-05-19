package com.jirepo.core.types;

/**
 * 코드값 정의를 위한 Enum 클래스에서 구현할 인터페이스이다. 
 */
public interface CodeEnumType<T> { 
  /** 코드값 반환 */
  T getCode();
  /** 상수의 한글 이름 반환 */
  String getKorName();
  /** 코드값 설명  */
  String getDesc();
}