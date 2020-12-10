package org.apache.lucene.analysis;


import java.io.IOException;

/**
 * A TokenFilter is a TokenStream whose input is another token stream.
 * <p>
 * This is an abstract class.
 * 分词过滤器
 */
public abstract class TokenFilter extends TokenStream {
  /**
   * The source of tokens for this filter.
   */
  protected TokenStream input;

  /**
   * Call TokenFilter(TokenStream) instead.
   *
   * @deprecated
   */
  protected TokenFilter() {
  }

  /**
   * Construct a token stream filtering the given input.
   */
  protected TokenFilter(TokenStream input) {
    this.input = input;
  }

  /**
   * Close the input TokenStream.
   */
  public void close() throws IOException {
    input.close();
  }

}

