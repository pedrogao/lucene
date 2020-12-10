package org.apache.lucene.analysis;

import java.io.IOException;

/**
 * Normalizes token text to lower case.
 *
 * @version $Id: LowerCaseFilter.java,v 1.4 2004/03/29 22:48:00 cutting Exp $
 */
public final class LowerCaseFilter extends TokenFilter {
  public LowerCaseFilter(TokenStream in) {
    super(in);
  }

  public final Token next() throws IOException {
    Token t = input.next();

    if (t == null)
      return null;

    t.termText = t.termText.toLowerCase();

    return t;
  }
}
