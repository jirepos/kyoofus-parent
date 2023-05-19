package com.jirepo.core.lucene;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 쿼리 옵션을 제공한다. 
 */
@Getter
@Setter
public class SearchOption {
  /** 반드시 포함해야 하는 검색어를 담는다. */
  private List<String> must; 
  /** OR 조건으로 사용할 검색어를 담는다. */
  private List<String> should; 
  /** 폼함하지 않아야할 검색어를 담는다.  */
  private List<String> mustNot;
  /** 검색 필드를 설정한다.  */
  private List<String> searchFields; 
}
