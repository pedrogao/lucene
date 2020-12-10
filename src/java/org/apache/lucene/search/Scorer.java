package org.apache.lucene.search;

import java.io.IOException;

/**
 * Expert: Implements scoring for a class of queries.
 */
public abstract class Scorer {
  private Similarity similarity;

  /**
   * Constructs a Scorer.
   */
  protected Scorer(Similarity similarity) {
    this.similarity = similarity;
  }

  /**
   * Returns the Similarity implementation used by this scorer.
   */
  public Similarity getSimilarity() {
    return this.similarity;
  }

  /**
   * Scores all documents and passes them to a collector.
   */
  public void score(HitCollector hc) throws IOException {
    while (next()) {
      hc.collect(doc(), score());
    }
  }

  /**
   * Advance to the next document matching the query.  Returns true iff there
   * is another match.
   */
  public abstract boolean next() throws IOException;

  /**
   * Returns the current document number.  Initially invalid, until {@link
   * #next()} is called the first time.
   */
  public abstract int doc();

  /**
   * Returns the score of the current document.  Initially invalid, until
   * {@link #next()} is called the first time.
   */
  public abstract float score() throws IOException;

  /**
   * Skips to the first match beyond the current whose document number is
   * greater than or equal to <i>target</i>. <p>Returns true iff there is such
   * a match.  <p>Behaves as if written: <pre>
   *   boolean skipTo(int target) {
   *     do {
   *       if (!next())
   * 	     return false;
   *     } while (target > doc());
   *     return true;
   *   }
   * </pre>
   * Most implementations are considerably more efficient than that.
   */
  public abstract boolean skipTo(int target) throws IOException;

  /**
   * Returns an explanation of the score for <code>doc</code>.
   */
  public abstract Explanation explain(int doc) throws IOException;

}
