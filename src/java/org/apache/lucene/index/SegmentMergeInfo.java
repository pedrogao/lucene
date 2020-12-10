package org.apache.lucene.index;

import java.io.IOException;

final class SegmentMergeInfo {
  Term term;
  int base;
  TermEnum termEnum;
  IndexReader reader;
  TermPositions postings;
  int[] docMap = null;          // maps around deleted docs

  SegmentMergeInfo(int b, TermEnum te, IndexReader r)
    throws IOException {
    base = b;
    reader = r;
    termEnum = te;
    term = te.term();
    postings = reader.termPositions();

    // build array which maps document numbers around deletions 
    if (reader.hasDeletions()) {
      int maxDoc = reader.maxDoc();
      docMap = new int[maxDoc];
      int j = 0;
      for (int i = 0; i < maxDoc; i++) {
        if (reader.isDeleted(i))
          docMap[i] = -1;
        else
          docMap[i] = j++;
      }
    }
  }

  final boolean next() throws IOException {
    if (termEnum.next()) {
      term = termEnum.term();
      return true;
    } else {
      term = null;
      return false;
    }
  }

  final void close() throws IOException {
    termEnum.close();
    postings.close();
  }
}

