package org.apache.lucene.search;



import junit.framework.TestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class TestCachingWrapperFilter extends TestCase {
  public void testCachingWorks() throws Exception {
    Directory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new StandardAnalyzer(), true);
    writer.close();

    IndexReader reader = IndexReader.open(dir);

    MockFilter filter = new MockFilter();
    CachingWrapperFilter cacher = new CachingWrapperFilter(filter);

    // first time, nested filter is called
    cacher.bits(reader);
    assertTrue("first time", filter.wasCalled());

    // second time, nested filter should not be called
    filter.clear();
    cacher.bits(reader);
    assertFalse("second time", filter.wasCalled());

    reader.close();
 }
}
