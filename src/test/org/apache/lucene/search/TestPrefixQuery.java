package org.apache.lucene.search;



import junit.framework.TestCase;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Tests {@link PrefixQuery} class.
 *
 * @author Erik Hatcher
 */
public class TestPrefixQuery extends TestCase {
  public void testPrefixQuery() throws Exception {
    RAMDirectory directory = new RAMDirectory();

    String[] categories = new String[] {"/Computers",
                                        "/Computers/Mac",
                                        "/Computers/Windows"};
    IndexWriter writer = new IndexWriter(directory, new WhitespaceAnalyzer(), true);
    for (int i = 0; i < categories.length; i++) {
      Document doc = new Document();
      doc.add(Field.Keyword("category", categories[i]));
      writer.addDocument(doc);
    }
    writer.close();

    PrefixQuery query = new PrefixQuery(new Term("category", "/Computers"));
    IndexSearcher searcher = new IndexSearcher(directory);
    Hits hits = searcher.search(query);
    assertEquals("All documents in /Computers category and below", 3, hits.length());

    query = new PrefixQuery(new Term("category", "/Computers/Mac"));
    hits = searcher.search(query);
    assertEquals("One in /Computers/Mac", 1, hits.length());
  }
}
