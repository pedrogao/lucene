package org.apache.lucene.search;



import java.io.IOException;

import org.apache.lucene.index.IndexReader;

/** Expert: Calculate query weights and build query scorers.
 *
 * <p>A Weight is constructed by a query, given a Searcher ({@link
 * Query#createWeight(Searcher)}).  The {@link #sumOfSquaredWeights()} method
 * is then called on the top-level query to compute the query normalization
 * factor (@link Similarity#queryNorm(float)}).  This factor is then passed to
 * {@link #normalize(float)}.  At this point the weighting is complete and a
 * scorer may be constructed by calling {@link #scorer(IndexReader)}.
 */
public interface Weight extends java.io.Serializable {
  /** The query that this concerns. */
  Query getQuery();

  /** The weight for this query. */
  float getValue();

  /** The sum of squared weights of contained query clauses. */
  float sumOfSquaredWeights() throws IOException;

  /** Assigns the query normalization factor to this. */
  void normalize(float norm);

  /** Constructs a scorer for this. */
  Scorer scorer(IndexReader reader) throws IOException;

  /** An explanation of the score computation for the named document. */
  Explanation explain(IndexReader reader, int doc) throws IOException;
}
