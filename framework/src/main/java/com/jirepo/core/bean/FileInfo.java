package com.jirepo.core.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 파일 정보를 담고 있다.
 */
@Getter
@Setter 
public class FileInfo {
  /** 파일 절대 전체 경로를 포함한 파일 이름 */
  private String absolutePath;
  /** 확장자를 포함한 파일명 */
  private String name; 
}
