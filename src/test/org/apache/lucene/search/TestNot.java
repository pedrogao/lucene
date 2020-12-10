package org.apache.lucene.search;



import junit.framework.TestCase;

import java.util.Vector;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/** Similarity unit test.
 *
 * @author Doug Cutting
 * @version $Revision: 1.3 $
 */
public class TestNot extends TestCase {
  public TestNot(String name) {
    super(name);
  }

  public void testNot() throws Exception {
    RAMDirectory store = new RAMDirectory();
    IndexWriter writer = new IndexWriter(store, new SimpleAnalyzer(), true);

    Document d1 = new Document();
    d1.add(Field.Text("field", "a b"));

    writer.addDocument(d1);
    writer.optimize();
    writer.close();

    Searcher searcher = new IndexSearcher(store);
    Query query = QueryParser.parse("a NOT b", "field", new SimpleAnalyzer());
    //System.out.println(query);
    Hits hits = searcher.search(query);
    assertEquals(0, hits.length());
  }
}
