package org.apache.lucene.search;


import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

/**
 * A {@link Query} that matches documents containing a subset of terms provided
 * by a {@link FilteredTermEnum} enumeration.
 * <p>
 * <code>MultiTermQuery</code> is not designed to be used by itself.
 * <BR>
 * The reason being that it is not intialized with a {@link FilteredTermEnum}
 * enumeration. A {@link FilteredTermEnum} enumeration needs to be provided.
 * <p>
 * For example, {@link WildcardQuery} and {@link FuzzyQuery} extend
 * <code>MultiTermQuery</code> to provide {@link WildcardTermEnum} and
 * {@link FuzzyTermEnum}, respectively.
 */
@SuppressWarnings("Duplicates")
public abstract class MultiTermQuery extends Query {
  private Term term;

  /**
   * Constructs a query for terms matching <code>term</code>.
   */
  public MultiTermQuery(Term term) {
    this.term = term;
  }

  /**
   * Returns the pattern term.
   */
  public Term getTerm() {
    return term;
  }

  /**
   * Construct the enumeration to be used, expanding the pattern term.
   */
  protected abstract FilteredTermEnum getEnum(IndexReader reader)
    throws IOException;

  public Query rewrite(IndexReader reader) throws IOException {
    FilteredTermEnum enumerator = getEnum(reader);
    BooleanQuery query = new BooleanQuery();
    try {
      do {
        Term t = enumerator.term();
        if (t != null) {
          TermQuery tq = new TermQuery(t);      // found a match
          tq.setBoost(getBoost() * enumerator.difference()); // set the boost
          query.add(tq, false, false);          // add to query
        }
      } while (enumerator.next());
    } finally {
      enumerator.close();
    }
    return query;
  }

  public Query combine(Query[] queries) {
    return Query.mergeBooleanQueries(queries);
  }


  /**
   * Prints a user-readable version of this query.
   */
  public String toString(String field) {
    StringBuffer buffer = new StringBuffer();
    if (!term.field().equals(field)) {
      buffer.append(term.field());
      buffer.append(":");
    }
    buffer.append(term.text());
    if (getBoost() != 1.0f) {
      buffer.append("^");
      buffer.append(Float.toString(getBoost()));
    }
    return buffer.toString();
  }
}
