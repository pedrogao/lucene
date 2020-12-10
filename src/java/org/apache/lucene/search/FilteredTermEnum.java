package org.apache.lucene.search;


import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

/**
 * Abstract class for enumerating a subset of all terms.
 *
 * <p>Term enumerations are always ordered by Term.compareTo().  Each term in
 * the enumeration is greater than all that precede it.
 */
public abstract class FilteredTermEnum extends TermEnum {
  private Term currentTerm = null;
  private TermEnum actualEnum = null;

  public FilteredTermEnum() throws IOException {
  }

  /**
   * Equality compare on the term
   */
  protected abstract boolean termCompare(Term term);

  /**
   * Equality measure on the term
   */
  protected abstract float difference();

  /**
   * Indiciates the end of the enumeration has been reached
   */
  protected abstract boolean endEnum();

  protected void setEnum(TermEnum actualEnum) throws IOException {
    this.actualEnum = actualEnum;
    // Find the first term that matches
    Term term = actualEnum.term();
    if (term != null && termCompare(term))
      currentTerm = term;
    else next();
  }

  /**
   * Returns the docFreq of the current Term in the enumeration.
   * Initially invalid, valid after next() called for the first time.
   */
  public int docFreq() {
    if (actualEnum == null) return -1;
    return actualEnum.docFreq();
  }

  /**
   * Increments the enumeration to the next element.  True if one exists.
   */
  public boolean next() throws IOException {
    if (actualEnum == null) return false; // the actual enumerator is not initialized!
    currentTerm = null;
    while (currentTerm == null) {
      if (endEnum()) return false;
      if (actualEnum.next()) {
        Term term = actualEnum.term();
        if (termCompare(term)) {
          currentTerm = term;
          return true;
        }
      } else return false;
    }
    currentTerm = null;
    return false;
  }

  /**
   * Returns the current Term in the enumeration.
   * Initially invalid, valid after next() called for the first time.
   */
  public Term term() {
    return currentTerm;
  }

  /**
   * Closes the enumeration to further activity, freeing resources.
   */
  public void close() throws IOException {
    actualEnum.close();
    currentTerm = null;
    actualEnum = null;
  }
}
