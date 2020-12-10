package org.apache.lucene.index;

/**
 * 字段信息
 */
final class FieldInfo {
  String name;
  boolean isIndexed;
  int number;

  // true if term vector for this field should be stored
  boolean storeTermVector;

  FieldInfo(String na, boolean tk, int nu, boolean storeTermVector) {
    name = na;
    isIndexed = tk;
    number = nu;
    this.storeTermVector = storeTermVector;
  }
}
