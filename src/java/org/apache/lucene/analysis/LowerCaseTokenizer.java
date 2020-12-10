package org.apache.lucene.analysis;

import java.io.Reader;

/**
 * LowerCaseTokenizer performs the function of LetterTokenizer
 * and LowerCaseFilter together.  It divides text at non-letters and converts
 * them to lower case.  While it is functionally equivalent to the combination
 * of LetterTokenizer and LowerCaseFilter, there is a performance advantage
 * to doing the two tasks at once, hence this (redundant) implementation.
 * <p>
 * Note: this does a decent job for most European languages, but does a terrible
 * job for some Asian languages, where words are not separated by spaces.
 * <p>
 * 大小写分词器，将大写字母一律小写
 */
public final class LowerCaseTokenizer extends LetterTokenizer {
  /**
   * Construct a new LowerCaseTokenizer.
   */
  public LowerCaseTokenizer(Reader in) {
    super(in);
  }

  /**
   * Collects only characters which satisfy
   * {@link Character#isLetter(char)}.
   */
  protected char normalize(char c) {
    return Character.toLowerCase(c);
  }
}
