package org.apache.lucene.analysis;

import java.io.*;

import junit.framework.*;

import org.apache.lucene.*;
import org.apache.lucene.analysis.*;

public class TestAnalyzers extends TestCase {

  public TestAnalyzers(String name) {
    super(name);
  }

  public void assertAnalyzesTo(Analyzer a,
                               String input,
                               String[] output) throws Exception {
    TokenStream ts = a.tokenStream("dummy", new StringReader(input));
    for (int i = 0; i < output.length; i++) {
      Token t = ts.next();
      assertNotNull(t);
      assertEquals(t.termText(), output[i]);
    }
    assertNull(ts.next());
    ts.close();
  }

  public void testSimple() throws Exception {
    Analyzer a = new SimpleAnalyzer();
    assertAnalyzesTo(a, "foo bar FOO BAR",
      new String[]{"foo", "bar", "foo", "bar"});
    assertAnalyzesTo(a, "foo      bar .  FOO <> BAR",
      new String[]{"foo", "bar", "foo", "bar"});
    assertAnalyzesTo(a, "foo.bar.FOO.BAR",
      new String[]{"foo", "bar", "foo", "bar"});
    assertAnalyzesTo(a, "U.S.A.",
      new String[]{"u", "s", "a"});
    assertAnalyzesTo(a, "C++",
      new String[]{"c"});
    assertAnalyzesTo(a, "B2B",
      new String[]{"b", "b"});
    assertAnalyzesTo(a, "2B",
      new String[]{"b"});
    assertAnalyzesTo(a, "\"QUOTED\" word",
      new String[]{"quoted", "word"});
  }

  public void testNull() throws Exception {
    Analyzer a = new WhitespaceAnalyzer();
    assertAnalyzesTo(a, "foo bar FOO BAR",
      new String[]{"foo", "bar", "FOO", "BAR"});
    assertAnalyzesTo(a, "foo      bar .  FOO <> BAR",
      new String[]{"foo", "bar", ".", "FOO", "<>", "BAR"});
    assertAnalyzesTo(a, "foo.bar.FOO.BAR",
      new String[]{"foo.bar.FOO.BAR"});
    assertAnalyzesTo(a, "U.S.A.",
      new String[]{"U.S.A."});
    assertAnalyzesTo(a, "C++",
      new String[]{"C++"});
    assertAnalyzesTo(a, "B2B",
      new String[]{"B2B"});
    assertAnalyzesTo(a, "2B",
      new String[]{"2B"});
    assertAnalyzesTo(a, "\"QUOTED\" word",
      new String[]{"\"QUOTED\"", "word"});
  }

  public void testStop() throws Exception {
    Analyzer a = new StopAnalyzer();
    assertAnalyzesTo(a, "foo bar FOO BAR",
      new String[]{"foo", "bar", "foo", "bar"});
    assertAnalyzesTo(a, "foo a bar such FOO THESE BAR",
      new String[]{"foo", "bar", "foo", "bar"});
  }
}

