package org.apache.lucene.search;

/**
 * 布尔查询分句
 * A clause in a BooleanQuery.
 */
public class BooleanClause implements java.io.Serializable {
  /**
   * The query whose matching documents are combined by the boolean query.
   */
  public Query query;
  /**
   * If true, documents documents which <i>do not</i>
   * match this sub-query will <i>not</i> match the boolean query.
   */
  public boolean required = false;
  /**
   * If true, documents documents which <i>do</i>
   * match this sub-query will <i>not</i> match the boolean query.
   * 禁止
   */
  public boolean prohibited = false;

  /**
   * Constructs a BooleanClause with query <code>q</code>, required
   * <code>r</code> and prohibited <code>p</code>.
   */
  public BooleanClause(Query q, boolean r, boolean p) {
    query = q;
    required = r;
    prohibited = p;
  }

  /**
   * Returns true iff <code>o</code> is equal to this.
   */
  public boolean equals(Object o) {
    if (!(o instanceof BooleanClause))
      return false;
    BooleanClause other = (BooleanClause) o;
    return this.query.equals(other.query)
      && (this.required == other.required)
      && (this.prohibited == other.prohibited);
  }

  /**
   * Returns a hash code value for this object.
   */
  public int hashCode() {
    return query.hashCode() ^ (this.required ? 1 : 0) ^ (this.prohibited ? 2 : 0);
  }

}
