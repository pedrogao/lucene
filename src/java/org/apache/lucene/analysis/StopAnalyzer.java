package org.apache.lucene.analysis;

import java.io.Reader;
import java.util.Set;

/**
 * Filters LetterTokenizer with LowerCaseFilter and StopFilter.
 * 停用词分词器
 */
public final class StopAnalyzer extends Analyzer {

  private Set stopWords;

  /**
   * An array containing some common English words that are not usually useful
   * for searching.
   */
  public static final String[] ENGLISH_STOP_WORDS = {
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "s", "such",
    "t", "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
  };

  /**
   * Builds an analyzer which removes words in ENGLISH_STOP_WORDS.
   */
  public StopAnalyzer() {
    stopWords = StopFilter.makeStopSet(ENGLISH_STOP_WORDS);
  }

  /**
   * Builds an analyzer which removes words in the provided array.
   */
  public StopAnalyzer(String[] stopWords) {
    this.stopWords = StopFilter.makeStopSet(stopWords);
  }

  /**
   * Filters LowerCaseTokenizer with StopFilter.
   */
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new StopFilter(new LowerCaseTokenizer(reader), stopWords);
  }
}

