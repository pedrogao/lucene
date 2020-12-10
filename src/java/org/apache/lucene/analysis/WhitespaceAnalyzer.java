package org.apache.lucene.analysis;

import java.io.Reader;

/**
 * An Analyzer that uses WhitespaceTokenizer.
 * 空格分析器
 */
public final class WhitespaceAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new WhitespaceTokenizer(reader);
  }
}
