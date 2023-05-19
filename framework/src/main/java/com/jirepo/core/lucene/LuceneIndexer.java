package com.jirepo.core.lucene;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;


/**
 * 인덱스를 생성하는 기능을 제공하고 삭제 및 갱신을 제공한다. 
 */
public class LuceneIndexer {


  private NgramAnalyzer analyzer; 
  private IndexWriter indexWriter;
  private FSDirectory dir;
  private OpenMode openMode;

  /**
   * 생성자 
   * @param openMode  IndexWriter OpenMode 
   * @throws Exception
   */
  public LuceneIndexer(OpenMode openMode) throws Exception {
    this.openMode = openMode; 
    this.analyzer = new NgramAnalyzer();
    createIndexWriter();
  }


  /**
   * IndexWriter 인스턴스 생성
   * @throws Exception
   */
  private void createIndexWriter() throws Exception {
    dir = FSDirectory.open(Paths.get(LuceneConstant.INDEX_PATH));
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    // OpenMode.CREATE - 색인시마다 기존 색인 삭제 후 재 색인
    // OpenMode.CREATE_OR_APPEND - 기존 색인이 없으면 만들고, 있으면 append 함. 
    // OpenMode.APPEND - 기존 색인에 추가
    config.setOpenMode(this.openMode);
    //config.setOpenMode(OpenMode.CREATE);
    //TieredMergePolicy mergePolicy = new TieredMergePolicy();
    //config.setMergePolicy(mergePolicy);
    //checkLock();
    this.indexWriter = new IndexWriter(dir, config);
  }//:


  /**
   * 인덱스를 갱신한다. 
   * @param document  갱신할 문서 
   * @throws Exception
   */
  public void update(Document document) throws Exception {
    String postId = document.get("id");
    Term term = new Term("id", postId); 
    this.indexWriter.updateDocument(term, document); 
  }//:

  /**
   * 인덱스에서 문서 삭제 
   * @param postId 삭제할 아이디 
   * @throws Exception
   */
  public void delete(String id) throws Exception  { 
    this.indexWriter.deleteDocuments(new Term("id", id));
    // 삭제 대상이 있는지 확인 
    this.indexWriter.hasDeletions();
    //this.indexWriter.commit();
  }


  /**
   * 인덱싱할 문서 추가 
   * @param d Document 인스턴스 
   * @throws IOException
   */
  public void add(Document d) throws IOException {
    this.indexWriter.addDocument(d);
  }


  /**
   * 변경사항을 커밋한다. 
   * @throws Exception
   */
  public void commit() throws Exception  {
    this.indexWriter.commit();
  }
  /**
   * IndexWriter를 닫는다. 
   * @throws IOException
   */
  public void close() throws IOException{ 
    this.indexWriter.close();
  }


  /**
   * write.lock 파일이 있는지 체크. 8.9 버전에서는 의미가 없는 것 같으나 
   * 정확히 파악되지 않음. 
   * @throws IOException
   * @throws InterruptedException
   */
  private void checkLock() throws IOException, InterruptedException {
    //IndexWriter.WRITE_LOCK_NAME - 실제 index 시 directory에 보면 xxx.lock 파일이 존재하게 되는데 
    //존재 할 경우는 lock으로 판단. 
    File file = new File(LuceneConstant.INDEX_PATH + "/" + IndexWriter.WRITE_LOCK_NAME );
    //while(dir.fileExists(IndexWriter.WRITE_LOCK_NAME)){
    while(file.exists()){
      System.out.println("write.lock file exsists.");
    // dir.clearLock(name);
        Thread.sleep(10);
    }
  }
}///~
