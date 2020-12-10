package org.apache.lucene.search;


/**
 * Expert: A ScoreDoc which also contains information about
 * how to sort the referenced document.  In addition to the
 * document number and score, this object contains an array
 * of values for the document from the field(s) used to sort.
 * For example, if the sort criteria was to sort by fields
 * "a", "b" then "c", the <code>fields</code> object array
 * will have three elements, corresponding respectively to
 * the term values for the document in fields "a", "b" and "c".
 * The class of each element in the array will be either
 * Integer, Float or String depending on the type of values
 * in the terms of each field.
 *
 * <p>Created: Feb 11, 2004 1:23:38 PM
 *
 * @author Tim Jones (Nacimiento Software)
 * @version $Id: FieldDoc.java,v 1.4 2004/04/22 22:23:14 tjones Exp $
 * @see ScoreDoc
 * @see TopFieldDocs
 * @since lucene 1.4
 */
public class FieldDoc extends ScoreDoc {

  /**
   * Expert: The values which are used to sort the referenced document.
   * The order of these will match the original sort criteria given by a
   * Sort object.  Each Object will be either an Integer, Float or String,
   * depending on the type of values in the terms of the original field.
   *
   * @see Sort
   * @see Searchable#search(Query, Filter, int, Sort)
   */
  public Comparable[] fields;

  /**
   * Expert: Creates one of these objects with empty sort information.
   */
  public FieldDoc(int doc, float score) {
    super(doc, score);
  }

  /**
   * Expert: Creates one of these objects with the given sort information.
   */
  public FieldDoc(int doc, float score, Comparable[] fields) {
    super(doc, score);
    this.fields = fields;
  }
}
