package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.*;

final class ExactPhraseScorer extends PhraseScorer {

  ExactPhraseScorer(Weight weight, TermPositions[] tps, int[] positions, Similarity similarity,
                    byte[] norms) throws IOException {
    super(weight, tps, positions, similarity, norms);
  }

  protected final float phraseFreq() throws IOException {
    // sort list with pq
    for (PhrasePositions pp = first; pp != null; pp = pp.next) {
      pp.firstPosition();
      pq.put(pp);          // build pq from list
    }
    pqToList();            // rebuild list from pq

    int freq = 0;
    do {            // find position w/ all terms
      while (first.position < last.position) {    // scan forward in first
        do {
          if (!first.nextPosition())
            return (float) freq;
        } while (first.position < last.position);
        firstToLast();
      }
      freq++;            // all equal: a match
    } while (last.nextPosition());

    return (float) freq;
  }
}
