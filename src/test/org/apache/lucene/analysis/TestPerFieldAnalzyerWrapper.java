package org.apache.lucene.analysis;

import junit.framework.TestCase;

import java.io.StringReader;

public class TestPerFieldAnalzyerWrapper extends TestCase {
  public void testPerField() throws Exception {
    String text = "Qwerty";
    PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer());
    analyzer.addAnalyzer("special", new SimpleAnalyzer());

    TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(text));
    Token token = tokenStream.next();
    assertEquals("WhitespaceAnalyzer does not lowercase", "Qwerty", token.termText());

    tokenStream = analyzer.tokenStream("special", new StringReader(text));
    token = tokenStream.next();
    assertEquals("SimpleAnalyzer lowercases", "qwerty", token.termText());
  }
}
