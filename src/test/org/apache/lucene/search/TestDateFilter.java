package org.apache.lucene.search;



import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateField;

import java.io.IOException;

import junit.framework.TestCase;

 /**
  * DateFilter JUnit tests.
  *
  * @author Otis Gospodnetic
  * @version $Revision: 1.5 $
  */
public class TestDateFilter
    extends TestCase
{
    public TestDateFilter(String name)
    {
	super(name);
    }

    /**
     *
     */
    public static void testBefore()
	throws IOException
    {
	// create an index
        RAMDirectory indexStore = new RAMDirectory();
        IndexWriter writer = new IndexWriter(indexStore, new SimpleAnalyzer(), true);

 	long now = System.currentTimeMillis();

 	Document doc = new Document();
 	// add time that is in the past
 	doc.add(Field.Keyword("datefield", DateField.timeToString(now - 1000)));
 	doc.add(Field.Text("body", "Today is a very sunny day in New York City"));
  	writer.addDocument(doc);
 	writer.optimize();
	writer.close();

	IndexSearcher searcher = new IndexSearcher(indexStore);

	// filter that should preserve matches
	DateFilter df1 = DateFilter.Before("datefield", now);

	// filter that should discard matches
	DateFilter df2 = DateFilter.Before("datefield", now - 999999);

	// search something that doesn't exist with DateFilter
	Query query1 = new TermQuery(new Term("body", "NoMatchForThis"));

	// search for something that does exists
	Query query2 = new TermQuery(new Term("body", "sunny"));

	Hits result;

	// ensure that queries return expected results without DateFilter first
	result = searcher.search(query1);
	assertEquals(0, result.length());

	result = searcher.search(query2);
	assertEquals(1, result.length());


	// run queries with DateFilter
	result = searcher.search(query1, df1);
	assertEquals(0, result.length());

	result = searcher.search(query1, df2);
	assertEquals(0, result.length());

 	result = searcher.search(query2, df1);
 	assertEquals(1, result.length());

	result = searcher.search(query2, df2);
	assertEquals(0, result.length());
    }

    /**
     *
     */
    public static void testAfter()
	throws IOException
    {
	// create an index
        RAMDirectory indexStore = new RAMDirectory();
        IndexWriter writer = new IndexWriter(indexStore, new SimpleAnalyzer(), true);

 	long now = System.currentTimeMillis();

 	Document doc = new Document();
 	// add time that is in the future
 	doc.add(Field.Keyword("datefield", DateField.timeToString(now + 888888)));
 	doc.add(Field.Text("body", "Today is a very sunny day in New York City"));
  	writer.addDocument(doc);
 	writer.optimize();
	writer.close();

	IndexSearcher searcher = new IndexSearcher(indexStore);

	// filter that should preserve matches
	DateFilter df1 = DateFilter.After("datefield", now);

	// filter that should discard matches
	DateFilter df2 = DateFilter.After("datefield", now + 999999);

	// search something that doesn't exist with DateFilter
	Query query1 = new TermQuery(new Term("body", "NoMatchForThis"));

	// search for something that does exists
	Query query2 = new TermQuery(new Term("body", "sunny"));

	Hits result;

	// ensure that queries return expected results without DateFilter first
	result = searcher.search(query1);
	assertEquals(0, result.length());

	result = searcher.search(query2);
	assertEquals(1, result.length());


	// run queries with DateFilter
	result = searcher.search(query1, df1);
	assertEquals(0, result.length());

	result = searcher.search(query1, df2);
	assertEquals(0, result.length());

 	result = searcher.search(query2, df1);
 	assertEquals(1, result.length());

	result = searcher.search(query2, df2);
	assertEquals(0, result.length());
    }
}
