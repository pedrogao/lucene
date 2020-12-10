package org.apache.lucene.search;



import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Term position unit test.
 *
 * @author Doug Cutting
 * @version $Revision: 1.4 $
 */
public class TestPositionIncrement extends TestCase {

  public void testSetPosition() throws Exception {
    Analyzer analyzer = new Analyzer() {
      public TokenStream tokenStream(String fieldName, Reader reader) {
        return new TokenStream() {
          private final String[] TOKENS = {"1", "2", "3", "4", "5"};
          private final int[] INCREMENTS = {1, 2, 1, 0, 1};
          private int i = 0;

          public Token next() throws IOException {
            if (i == TOKENS.length)
              return null;
            Token t = new Token(TOKENS[i], i, i);
            t.setPositionIncrement(INCREMENTS[i]);
            i++;
            return t;
          }
        };
      }
    };
    RAMDirectory store = new RAMDirectory();
    IndexWriter writer = new IndexWriter(store, analyzer, true);
    Document d = new Document();
    d.add(Field.Text("field", "bogus"));
    writer.addDocument(d);
    writer.optimize();
    writer.close();

    IndexSearcher searcher = new IndexSearcher(store);
    PhraseQuery q;
    Hits hits;

    q = new PhraseQuery();
    q.add(new Term("field", "1"));
    q.add(new Term("field", "2"));
    hits = searcher.search(q);
    assertEquals(0, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "2"));
    q.add(new Term("field", "3"));
    hits = searcher.search(q);
    assertEquals(1, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "3"));
    q.add(new Term("field", "4"));
    hits = searcher.search(q);
    assertEquals(0, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "2"));
    q.add(new Term("field", "4"));
    hits = searcher.search(q);
    assertEquals(1, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "3"));
    q.add(new Term("field", "5"));
    hits = searcher.search(q);
    assertEquals(1, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "4"));
    q.add(new Term("field", "5"));
    hits = searcher.search(q);
    assertEquals(1, hits.length());

    q = new PhraseQuery();
    q.add(new Term("field", "2"));
    q.add(new Term("field", "5"));
    hits = searcher.search(q);
    assertEquals(0, hits.length());
  }

  /**
   * Basic analyzer behavior should be to keep sequential terms in one
   * increment from one another.
   */
  public void testIncrementingPositions() throws Exception {
    Analyzer analyzer = new WhitespaceAnalyzer();
    TokenStream ts = analyzer.tokenStream("field",
                                new StringReader("one two three four five"));

    while (true) {
      Token token = ts.next();
      if (token == null) break;
      assertEquals(token.termText(), 1, token.getPositionIncrement());
    }
  }
}
