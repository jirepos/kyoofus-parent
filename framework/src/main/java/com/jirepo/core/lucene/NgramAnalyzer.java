package com.jirepo.core.lucene;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


/**
 * Tokenizer는 StandardTokenizer를 사용하고 NGramTokenFilter를 사용하는 커스텀 Analyzer이다. 
 */
public class NgramAnalyzer extends Analyzer {
  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    //StopFilter –> LowerCaseFilter –> KoreanFilter  –> KoreanTokenizer
      //NGramTokenizer tokenizer = new NGramTokenizer(2,20);
      StandardTokenizer tokenizer = new StandardTokenizer();
      //KoreanTokenizer tokenizer = new KoreanTokenizer();
      TokenStream tokenStream = new LowerCaseFilter(tokenizer);
      //List<String> stopWords = Arrays.asList( "<", "/>");
      //final CharArraySet stopSet = new CharArraySet(stopWords, true);
      //tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(stopWords));
      //tokenStream = new StopFilter(tokenStream, stopSet);
      tokenStream = new NGramTokenFilter(tokenStream, 2, 24, false);
      //tokenStream = new KoreanNumberFilter(tokenStream);
      return new TokenStreamComponents(tokenizer, tokenStream);
  }
}///~
