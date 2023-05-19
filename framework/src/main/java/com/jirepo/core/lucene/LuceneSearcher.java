package com.jirepo.core.lucene;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/**
 * 인덱스를 검색하는 기능을 제공한다. 
 */
public class LuceneSearcher {

  /**
   * 인덱스 검색 
   * @param options 검색 조건 
   * @param page  페이지 번호 
   * @param n 페이지당 가져올 갯 수 
   * @return
   *   검색된 문서 목록 
   * @throws Exception
   */
  public static SearchResult search(SearchOption options, int page, int n ) throws Exception {
    // StandardAnalyzer analyzer = new StandardAnalyzer();
    //NgramAnalyzer analyzer = new NgramAnalyzer();
    Directory indexDirectory = FSDirectory.open(Paths.get(LuceneConstant.INDEX_PATH));
    IndexReader indexReader = DirectoryReader.open(indexDirectory);
    IndexSearcher searcher = new IndexSearcher(indexReader);
    //Query query = new QueryParser(inField, analyzer).parse(queryString);

    BooleanQuery.Builder builder = new BooleanQuery.Builder();
    Optional<List<String>> should = Optional.ofNullable(options.getShould());
    if(should.isPresent()) {
      // OR 검색 조건 
      for(String m : should.get() ) {
        BooleanQuery.Builder builder2 = new BooleanQuery.Builder();
        Optional<List<String>> searchFields = Optional.of(options.getSearchFields());
        for(String field : searchFields.get()) {
          Query q = new TermQuery(new Term(field, m));  
          builder2.add(q, BooleanClause.Occur.SHOULD);
        }
        builder.add(builder2.build(), BooleanClause.Occur.SHOULD);
      }
    }
    
    Optional<List<String>> must = Optional.ofNullable(options.getMust());
    if(must.isPresent()) {
      // AND 검색 조건 
      for(String m : must.get() ) {
        BooleanQuery.Builder builder2 = new BooleanQuery.Builder();
        Optional<List<String>> searchFields = Optional.of(options.getSearchFields());
        for(String field : searchFields.get()) {
          Query q = new TermQuery(new Term(field, m));  
          builder2.add(q, BooleanClause.Occur.SHOULD);
        }
        builder.add(builder2.build(), BooleanClause.Occur.MUST);
      }
    }
    
    if(n <= 0) { n = 10;}
    //TopDocs topDocs = searcher.search(builder.build(), n);
    TopDocs topDocs = searchPaging(builder.build(), searcher, page, n); 
    //searcher.searchAfter(after, query, n, sort);
    System.out.println("총 갯수:" + topDocs.totalHits); 
    // List<ScoreDoc> list = Arrays.asList(topDocs.scoreDocs);
    List<Document> list = new ArrayList<Document>();
    for (ScoreDoc sdoc : topDocs.scoreDocs) {
      Document d = searcher.doc(sdoc.doc);
      list.add(d);
    }

    indexReader.close();
    SearchResult result = new SearchResult();
    result.setTotalHits(topDocs.totalHits.value);
    result.setDocuments(list);
    return result; 
    // return topDocs.scoreDocs.stream()
    // .map(scoreDoc -> searcher.doc(scoreDoc.doc))
    // .collect(Collectors.toList());
  }//:



  /**
   * Paging 처리 
   * @param query Query 인스턴스 
   * @param searcher IndexSearcher 인스턴스
   * @param page  페이지 번호 
   * @param n 가져올 갯 수 
   * @return
   * @throws Exception
   */
  private static TopDocs searchPaging(Query query, IndexSearcher searcher, int page, int n) throws Exception  {
    // https://stackoverflow.com/questions/963781/how-to-achieve-pagination-in-lucene/963828
    if(n <= 0) { n = 10;}
    int startIndex = (page -1) * n; 
    // Sort sort = new Sort(
    //   new SortField("updDate", SortField.Type.STRING, true /* reverse */)
    // );
    TopScoreDocCollector collector = TopScoreDocCollector.create(9999, 1); // 최대 9999개 
    searcher.search(query, collector);
    TopDocs topDocs = collector.topDocs(startIndex, n);  // startIndex부터 n 개 
    return topDocs; 
  }//:

  
}///~
