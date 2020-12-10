package ch1;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class CreateIndexTutorial {

  public static void main(String[] args) throws IOException {
    FSDirectory dir = FSDirectory.getDirectory("/Users/pedro/Desktop/apache/lucene-1.4.3/data", true);
    IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(), true);
    writer.setUseCompoundFile(false);
    Document document = new Document();
    Field username = Field.Text("username", "pedro");
    Field desc = Field.Text("desc", "a man who is love coding");
    document.add(username);
    document.add(desc);
    writer.addDocument(document);
    // writer.optimize();
    writer.close();
  }
}
