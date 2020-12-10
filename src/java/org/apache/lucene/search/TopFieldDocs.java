package org.apache.lucene.search;

/**
 * Expert: Returned by low-level sorted search implementations.
 *
 * <p>Created: Feb 12, 2004 8:58:46 AM
 *
 * @author Tim Jones (Nacimiento Software)
 * @version $Id: TopFieldDocs.java,v 1.2 2004/02/27 12:29:31 otis Exp $
 * @see Searchable#search(Query, Filter, int, Sort)
 * @since lucene 1.4
 */
public class TopFieldDocs extends TopDocs {

  /**
   * The fields which were used to sort results by.
   */
  public SortField[] fields;

  /**
   * Creates one of these objects.
   *
   * @param totalHits Total number of hits for the query.
   * @param scoreDocs The top hits for the query.
   * @param fields    The sort criteria used to find the top hits.
   */
  TopFieldDocs(int totalHits, ScoreDoc[] scoreDocs, SortField[] fields) {
    super(totalHits, scoreDocs);
    this.fields = fields;
  }
}
