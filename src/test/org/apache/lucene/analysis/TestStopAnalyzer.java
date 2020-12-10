package org.apache.lucene.analysis;



import junit.framework.TestCase;

import java.io.StringReader;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

public class TestStopAnalyzer extends TestCase {
  private StopAnalyzer stop = new StopAnalyzer();

  private Set inValidTokens = new HashSet();
  public TestStopAnalyzer(String s) {
    super(s);
  }

  protected void setUp() {
    for (int i = 0; i < StopAnalyzer.ENGLISH_STOP_WORDS.length; i++) {
      inValidTokens.add(StopAnalyzer.ENGLISH_STOP_WORDS[i]);
    }
  }

  public void testDefaults() {
    assertTrue(stop != null);
    StringReader reader = new StringReader("This is a test of the english stop analyzer");
    TokenStream stream = stop.tokenStream("test", reader);
    assertTrue(stream != null);
    Token token = null;
    try {
      while ((token = stream.next()) != null)
      {
        assertTrue(inValidTokens.contains(token.termText()) == false);
      }
    } catch (IOException e) {
      assertTrue(false);
    }
  }

  public void testStopList() {
    Set stopWordsSet = new HashSet();
    stopWordsSet.add("good");
    stopWordsSet.add("test");
    stopWordsSet.add("analyzer");
    StopAnalyzer newStop = new StopAnalyzer((String[])stopWordsSet.toArray(new String[3]));
    StringReader reader = new StringReader("This is a good test of the english stop analyzer");
    TokenStream stream = newStop.tokenStream("test", reader);
    assertTrue(stream != null);
    Token token = null;
    try {
      while ((token = stream.next()) != null)
      {
        String text = token.termText();
        assertTrue(stopWordsSet.contains(text) == false);
      }
    } catch (IOException e) {
      assertTrue(false);
    }
  }
}
