package ch1;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class SearchIndexTutorial {

  public static void main(String[] args) throws IOException {
    FSDirectory dir = FSDirectory.getDirectory("/Users/pedro/Desktop/apache/lucene-1.4.3/data", false);
    IndexSearcher searcher = new IndexSearcher(dir);
    TermQuery query = new TermQuery(new Term("username", "pedro"));
    Hits hits = searcher.search(query);
    for (int i = 0; i < hits.length(); i++) {
      Document doc = hits.doc(i);
      System.out.println(doc.toString());
    }
  }
}
