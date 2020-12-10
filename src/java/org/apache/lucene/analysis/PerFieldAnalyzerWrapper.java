package org.apache.lucene.analysis;

import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

/**
 * This analyzer is used to facilitate scenarios where different
 * fields require different analysis techniques.  Use {@link #addAnalyzer}
 * to add a non-default analyzer on a field name basis.
 * See TestPerFieldAnalzyerWrapper.java for example usage.
 * <p>
 * 不同字段可以有不同的分析器
 */
public class PerFieldAnalyzerWrapper extends Analyzer {
  // 字段默认解析器
  private Analyzer defaultAnalyzer;
  // 解析器map
  private Map analyzerMap = new HashMap();


  /**
   * Constructs with default analyzer.
   *
   * @param defaultAnalyzer Any fields not specifically
   *                        defined to use a different analyzer will use the one provided here.
   */
  public PerFieldAnalyzerWrapper(Analyzer defaultAnalyzer) {
    this.defaultAnalyzer = defaultAnalyzer;
  }

  /**
   * Defines an analyzer to use for the specified field.
   *
   * @param fieldName field name requiring a non-default analyzer.
   * @param analyzer  non-default analyzer to use for field
   */
  public void addAnalyzer(String fieldName, Analyzer analyzer) {
    analyzerMap.put(fieldName, analyzer);
  }

  public TokenStream tokenStream(String fieldName, Reader reader) {
    Analyzer analyzer = (Analyzer) analyzerMap.get(fieldName);
    if (analyzer == null) {
      analyzer = defaultAnalyzer;
    }

    return analyzer.tokenStream(fieldName, reader);
  }
}
