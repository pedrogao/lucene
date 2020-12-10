package org.apache.lucene.index;



import junit.framework.TestCase;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.search.Similarity;

import java.util.Map;
import java.io.IOException;

public class TestFieldsReader extends TestCase {
  private RAMDirectory dir = new RAMDirectory();
  private Document testDoc = new Document();
  private FieldInfos fieldInfos = null;

  public TestFieldsReader(String s) {
    super(s);
  }

  protected void setUp() {
    fieldInfos = new FieldInfos();
    DocHelper.setupDoc(testDoc);
    fieldInfos.add(testDoc);
    DocumentWriter writer = new DocumentWriter(dir, new WhitespaceAnalyzer(),
            Similarity.getDefault(), 50);
    assertTrue(writer != null);
    try {
      writer.addDocument("test", testDoc);
    }
    catch (IOException e)
    {
      
    }
  }

  protected void tearDown() {

  }

  public void test() {
    assertTrue(dir != null);
    assertTrue(fieldInfos != null);
    try {
      FieldsReader reader = new FieldsReader(dir, "test", fieldInfos);
      assertTrue(reader != null);
      assertTrue(reader.size() == 1);
      Document doc = reader.doc(0);
      assertTrue(doc != null);
      assertTrue(doc.getField("textField1") != null);
      Field field = doc.getField("textField2");
      assertTrue(field != null);
      assertTrue(field.isTermVectorStored() == true);
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }
}
