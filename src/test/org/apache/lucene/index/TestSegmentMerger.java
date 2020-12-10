package org.apache.lucene.index;



import junit.framework.TestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Collection;

public class TestSegmentMerger extends TestCase {
  //The variables for the new merged segment
  private Directory mergedDir = new RAMDirectory();
  private String mergedSegment = "test";
  //First segment to be merged
  private Directory merge1Dir = new RAMDirectory();
  private Document doc1 = new Document();
  private String merge1Segment = "test-1";
  private SegmentReader reader1 = null;
  //Second Segment to be merged
  private Directory merge2Dir = new RAMDirectory();
  private Document doc2 = new Document();
  private String merge2Segment = "test-2";
  private SegmentReader reader2 = null;
  

  public TestSegmentMerger(String s) {
    super(s);
  }

  protected void setUp() {
    DocHelper.setupDoc(doc1);
    DocHelper.writeDoc(merge1Dir, merge1Segment, doc1);
    DocHelper.setupDoc(doc2);
    DocHelper.writeDoc(merge2Dir, merge2Segment, doc2);
    try {
      reader1 = new SegmentReader(new SegmentInfo(merge1Segment, 1, merge1Dir));
      reader2 = new SegmentReader(new SegmentInfo(merge2Segment, 1, merge2Dir));
    } catch (IOException e) {
      e.printStackTrace();                                                      
    }

  }

  protected void tearDown() {

  }

  public void test() {
    assertTrue(mergedDir != null);
    assertTrue(merge1Dir != null);
    assertTrue(merge2Dir != null);
    assertTrue(reader1 != null);
    assertTrue(reader2 != null);
  }
  
  public void testMerge() {                             
    //System.out.println("----------------TestMerge------------------");
    SegmentMerger merger = new SegmentMerger(mergedDir, mergedSegment, false);
    merger.add(reader1);
    merger.add(reader2);
    try {
      int docsMerged = merger.merge();
      merger.closeReaders();
      assertTrue(docsMerged == 2);
      //Should be able to open a new SegmentReader against the new directory
      SegmentReader mergedReader = new SegmentReader(new SegmentInfo(mergedSegment, docsMerged, mergedDir));
      assertTrue(mergedReader != null);
      assertTrue(mergedReader.numDocs() == 2);
      Document newDoc1 = mergedReader.document(0);
      assertTrue(newDoc1 != null);
      //There are 2 unstored fields on the document
      assertTrue(DocHelper.numFields(newDoc1) == DocHelper.numFields(doc1) - 2);
      Document newDoc2 = mergedReader.document(1);
      assertTrue(newDoc2 != null);
      assertTrue(DocHelper.numFields(newDoc2) == DocHelper.numFields(doc2) - 2);
      
      TermDocs termDocs = mergedReader.termDocs(new Term(DocHelper.TEXT_FIELD_2_KEY, "field"));
      assertTrue(termDocs != null);
      assertTrue(termDocs.next() == true);
      
      Collection stored = mergedReader.getIndexedFieldNames(true);
      assertTrue(stored != null);
      //System.out.println("stored size: " + stored.size());
      assertTrue(stored.size() == 2);
      
      TermFreqVector vector = mergedReader.getTermFreqVector(0, DocHelper.TEXT_FIELD_2_KEY);
      assertTrue(vector != null);
      String [] terms = vector.getTerms();
      assertTrue(terms != null);
      //System.out.println("Terms size: " + terms.length);
      assertTrue(terms.length == 3);
      int [] freqs = vector.getTermFrequencies();
      assertTrue(freqs != null);
      //System.out.println("Freqs size: " + freqs.length);
      
      for (int i = 0; i < terms.length; i++) {
        String term = terms[i];
        int freq = freqs[i];
        //System.out.println("Term: " + term + " Freq: " + freq);
        assertTrue(DocHelper.FIELD_2_TEXT.indexOf(term) != -1);
        assertTrue(DocHelper.FIELD_2_FREQS[i] == freq);
      }                                                
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    //System.out.println("---------------------end TestMerge-------------------");
  }    
}