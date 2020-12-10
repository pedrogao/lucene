package org.apache.lucene.analysis;

import java.io.Reader;

/**
 * An Analyzer builds TokenStreams, which analyze text.  It thus represents a
 * policy for extracting index terms from text.
 * <p>
 * Typical implementations first build a Tokenizer, which breaks the stream of
 * characters from the Reader into raw Tokens.  One or more TokenFilters may
 * then be applied to the output of the Tokenizer.
 * <p>
 * WARNING: You must override one of the methods defined by this class in your
 * subclass or the Analyzer will enter an infinite loop.
 * <p>
 * 文本分析器
 */
public abstract class Analyzer {
  /**
   * Creates a TokenStream which tokenizes all the text in the provided
   * Reader.  Default implementation forwards to tokenStream(Reader) for
   * compatibility with older version.  Override to allow Analyzer to choose
   * strategy based on document and/or field.  Must be able to handle null
   * field name for backward compatibility.
   */
  public TokenStream tokenStream(String fieldName, Reader reader) {
    // implemented for backward compatibility
    return tokenStream(reader);
  }

  /**
   * Creates a TokenStream which tokenizes all the text in the provided
   * Reader.  Provided for backward compatibility only.
   *
   * @see #tokenStream(String, Reader)
   * @deprecated use tokenStream(String, Reader) instead.
   */
  public TokenStream tokenStream(Reader reader) {
    return tokenStream(null, reader);
  }
}

