package org.apache.lucene.index;



import junit.framework.TestCase;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class TestMultiReader extends TestCase {
  private Directory dir = new RAMDirectory();
  private Document doc1 = new Document();
  private Document doc2 = new Document();
  private SegmentReader reader1;
  private SegmentReader reader2;
  private SegmentReader [] readers = new SegmentReader[2];
  private SegmentInfos sis = new SegmentInfos();
  
  public TestMultiReader(String s) {
    super(s);
  }

  protected void setUp() {
    DocHelper.setupDoc(doc1);
    DocHelper.setupDoc(doc2);
    DocHelper.writeDoc(dir, "seg-1", doc1);
    DocHelper.writeDoc(dir, "seg-2", doc2);
    
    try {
      sis.write(dir);
      reader1 = new SegmentReader(new SegmentInfo("seg-1", 1, dir));
      reader2 = new SegmentReader(new SegmentInfo("seg-2", 1, dir));
      readers[0] = reader1;
      readers[1] = reader2;      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
/*IndexWriter writer  = new IndexWriter(dir, new WhitespaceAnalyzer(), true);
      writer.addDocument(doc1);
      writer.addDocument(doc2);
      writer.close();*/
  protected void tearDown() {

  }
  
  public void test() {
    assertTrue(dir != null);
    assertTrue(reader1 != null);
    assertTrue(reader2 != null);
    assertTrue(sis != null);
  }    

  public void testDocument() {
    try {    
      sis.read(dir);
      MultiReader reader = new MultiReader(dir, sis, false, readers);
      assertTrue(reader != null);
      Document newDoc1 = reader.document(0);
      assertTrue(newDoc1 != null);
      assertTrue(DocHelper.numFields(newDoc1) == DocHelper.numFields(doc1) - 2);
      Document newDoc2 = reader.document(1);
      assertTrue(newDoc2 != null);
      assertTrue(DocHelper.numFields(newDoc2) == DocHelper.numFields(doc2) - 2);
      TermFreqVector vector = reader.getTermFreqVector(0, DocHelper.TEXT_FIELD_2_KEY);
      assertTrue(vector != null);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }
  
  public void testTermVectors() {
    try {
      MultiReader reader = new MultiReader(dir, sis, false, readers);
      assertTrue(reader != null);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }    
}
