package org.apache.lucene.search;


/**
 * Expert: Returned by low-level search implementations.
 *
 * @see Searcher#search(Query, Filter, int)
 */
public class TopDocs implements java.io.Serializable {
  /**
   * Expert: The total number of hits for the query.
   *
   * @see Hits#length()
   */
  public int totalHits;
  /**
   * Expert: The top hits for the query.
   */
  public ScoreDoc[] scoreDocs;

  /**
   * Expert: Constructs a TopDocs.
   */
  TopDocs(int totalHits, ScoreDoc[] scoreDocs) {
    this.totalHits = totalHits;
    this.scoreDocs = scoreDocs;
  }
}
