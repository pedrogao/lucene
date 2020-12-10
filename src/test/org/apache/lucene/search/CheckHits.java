package org.apache.lucene.search;



/* 20 May 2004:   Factored out of spans tests. Please leave this comment
                  until this class is evt. also used by tests in search package.
 */

import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class CheckHits {
  public static void checkHits(
        Query query,
        String defaultFieldName,
        Searcher searcher,
        int[] results,
        TestCase testCase)
          throws IOException {
    Hits hits = searcher.search(query);

    Set correct = new TreeSet();
    for (int i = 0; i < results.length; i++) {
      correct.add(new Integer(results[i]));
    }

    Set actual = new TreeSet();
    for (int i = 0; i < hits.length(); i++) {
      actual.add(new Integer(hits.id(i)));
    }

    testCase.assertEquals(query.toString(defaultFieldName), correct, actual);
  }

  public static void printDocNrs(Hits hits) throws IOException {
    System.out.print("new int[] {");
    for (int i = 0; i < hits.length(); i++) {
      System.out.print(hits.id(i));
      if (i != hits.length()-1)
        System.out.print(", ");
    }
    System.out.println("}");
  }
}

