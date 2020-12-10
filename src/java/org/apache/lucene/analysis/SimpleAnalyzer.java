package org.apache.lucene.analysis;

import java.io.Reader;

/**
 * An Analyzer that filters LetterTokenizer with LowerCaseFilter.
 * 简单的文本分析器，返回大小写分词器
 */
public final class SimpleAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new LowerCaseTokenizer(reader);
  }
}
