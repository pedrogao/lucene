package org.apache.lucene.analysis.standard;

import org.apache.lucene.analysis.*;

import java.io.Reader;
import java.util.Set;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}.
 * 标准分析器
 *
 * @version $Id: StandardAnalyzer.java,v 1.8 2004/03/29 22:48:01 cutting Exp $
 */
public class StandardAnalyzer extends Analyzer {
  private Set stopSet;

  /**
   * An array containing some common English words that are usually not
   * useful for searching.
   */
  public static final String[] STOP_WORDS = StopAnalyzer.ENGLISH_STOP_WORDS;

  /**
   * Builds an analyzer.
   */
  public StandardAnalyzer() {
    this(STOP_WORDS);
  }

  /**
   * Builds an analyzer with the given stop words.
   */
  public StandardAnalyzer(String[] stopWords) {
    stopSet = StopFilter.makeStopSet(stopWords);
  }

  /**
   * Constructs a {@link StandardTokenizer} filtered by a {@link
   * StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}.
   */
  public TokenStream tokenStream(String fieldName, Reader reader) {
    // 标准分词器分词
    TokenStream result = new StandardTokenizer(reader);
    // 标准过滤器过滤
    result = new StandardFilter(result);
    // 大小写过滤器
    result = new LowerCaseFilter(result);
    // 停用词过滤器
    result = new StopFilter(result, stopSet);
    return result;
  }
}
