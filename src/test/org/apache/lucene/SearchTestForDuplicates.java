package org.apache.lucene;



import java.io.IOException;

import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

class SearchTestForDuplicates {

  static final String PRIORITY_FIELD ="priority";
  static final String ID_FIELD ="id";
  static final String HIGH_PRIORITY ="high";
  static final String MED_PRIORITY ="medium";
  static final String LOW_PRIORITY ="low";

  public static void main(String[] args) {
    try {
      Directory directory = new RAMDirectory();
      Analyzer analyzer = new SimpleAnalyzer();
      IndexWriter writer = new IndexWriter(directory, analyzer, true);

      final int MAX_DOCS = 225;

      for (int j = 0; j < MAX_DOCS; j++) {
        Document d = new Document();
        d.add(Field.Text(PRIORITY_FIELD, HIGH_PRIORITY));
        d.add(Field.Text(ID_FIELD, Integer.toString(j)));
        writer.addDocument(d);
      }
      writer.close();

      // try a search without OR
      Searcher searcher = new IndexSearcher(directory);
      Hits hits = null;

      QueryParser parser = new QueryParser(PRIORITY_FIELD, analyzer);

      Query query = parser.parse(HIGH_PRIORITY);
      System.out.println("Query: " + query.toString(PRIORITY_FIELD));

      hits = searcher.search(query);
      printHits(hits);

      searcher.close();

      // try a new search with OR
      searcher = new IndexSearcher(directory);
      hits = null;

      parser = new QueryParser(PRIORITY_FIELD, analyzer);

      query = parser.parse(HIGH_PRIORITY + " OR " + MED_PRIORITY);
      System.out.println("Query: " + query.toString(PRIORITY_FIELD));

      hits = searcher.search(query);
      printHits(hits);

      searcher.close();

    } catch (Exception e) {
      System.out.println(" caught a " + e.getClass() +
                         "\n with message: " + e.getMessage());
    }
  }

  private static void printHits( Hits hits ) throws IOException {
    System.out.println(hits.length() + " total results\n");
    for (int i = 0 ; i < hits.length(); i++) {
      if ( i < 10 || (i > 94 && i < 105) ) {
        Document d = hits.doc(i);
        System.out.println(i + " " + d.get(ID_FIELD));
      }
    }
  }

}
