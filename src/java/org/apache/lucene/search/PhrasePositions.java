package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.*;

final class PhrasePositions {
  int doc;            // current doc
  int position;            // position in doc
  int count;            // remaining pos in this doc
  int offset;            // position in phrase
  TermPositions tp;          // stream of positions
  PhrasePositions next;          // used to make lists

  PhrasePositions(TermPositions t, int o) {
    tp = t;
    offset = o;
  }

  final boolean next() throws IOException {    // increments to next doc
    if (!tp.next()) {
      tp.close();          // close stream
      doc = Integer.MAX_VALUE;        // sentinel value
      return false;
    }
    doc = tp.doc();
    position = 0;
    return true;
  }

  final boolean skipTo(int target) throws IOException {
    if (!tp.skipTo(target)) {
      tp.close();          // close stream
      doc = Integer.MAX_VALUE;        // sentinel value
      return false;
    }
    doc = tp.doc();
    position = 0;
    return true;
  }


  final void firstPosition() throws IOException {
    count = tp.freq();          // read first pos
    nextPosition();
  }

  final boolean nextPosition() throws IOException {
    if (count-- > 0) {          // read subsequent pos's
      position = tp.nextPosition() - offset;
      return true;
    } else
      return false;
  }
}
