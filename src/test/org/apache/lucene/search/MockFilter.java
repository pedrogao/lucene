package org.apache.lucene.search;



import org.apache.lucene.index.IndexReader;
import java.util.BitSet;

public class MockFilter extends Filter {
  private boolean wasCalled;

  public BitSet bits(IndexReader reader) {
    wasCalled = true;
    return new BitSet();
  }

  public void clear() {
    wasCalled = false;
  }

  public boolean wasCalled() {
    return wasCalled;
  }
}
