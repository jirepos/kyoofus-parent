package com.jirepo.core.lucene;


import java.util.List;

import org.apache.lucene.document.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * 검색결과 
 */
@Setter
@Getter
public class SearchResult {
  /** 전체 검색 결과 건수  */
  private long totalHits; 
  /** 검색된 결과  */
  private List<Document> documents;
}
