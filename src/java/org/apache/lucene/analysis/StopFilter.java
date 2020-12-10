package org.apache.lucene.analysis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Removes stop words from a token stream.
 * 停用词过滤器
 */
public final class StopFilter extends TokenFilter {

  private Set stopWords;

  /**
   * Constructs a filter which removes words from the input
   * TokenStream that are named in the array of words.
   */
  public StopFilter(TokenStream in, String[] stopWords) {
    super(in);
    this.stopWords = makeStopSet(stopWords);
  }

  /**
   * Constructs a filter which removes words from the input
   * TokenStream that are named in the Hashtable.
   *
   * @deprecated Use {@link #StopFilter(TokenStream, Set)} instead
   */
  public StopFilter(TokenStream in, Hashtable stopTable) {
    super(in);
    stopWords = new HashSet(stopTable.keySet());
  }

  /**
   * Constructs a filter which removes words from the input
   * TokenStream that are named in the Set.
   * It is crucial that an efficient Set implementation is used
   * for maximum performance.
   *
   * @see #makeStopSet(java.lang.String[])
   */
  public StopFilter(TokenStream in, Set stopWords) {
    super(in);
    this.stopWords = stopWords;
  }

  /**
   * Builds a Hashtable from an array of stop words,
   * appropriate for passing into the StopFilter constructor.
   * This permits this table construction to be cached once when
   * an Analyzer is constructed.
   *
   * @deprecated Use {@link #makeStopSet(String[])} instead.
   */
  public static final Hashtable makeStopTable(String[] stopWords) {
    Hashtable stopTable = new Hashtable(stopWords.length);
    for (int i = 0; i < stopWords.length; i++)
      stopTable.put(stopWords[i], stopWords[i]);
    return stopTable;
  }

  /**
   * Builds a Set from an array of stop words,
   * appropriate for passing into the StopFilter constructor.
   * This permits this stopWords construction to be cached once when
   * an Analyzer is constructed.
   */
  public static final Set makeStopSet(String[] stopWords) {
    HashSet stopTable = new HashSet(stopWords.length);
    for (int i = 0; i < stopWords.length; i++)
      stopTable.add(stopWords[i]);
    return stopTable;
  }

  /**
   * Returns the next input Token whose termText() is not a stop word.
   * 过滤掉停用词
   */
  public final Token next() throws IOException {
    // return the first non-stop word found
    for (Token token = input.next(); token != null; token = input.next())
      if (!stopWords.contains(token.termText))
        return token;
    // reached EOS -- return null
    return null;
  }
}
