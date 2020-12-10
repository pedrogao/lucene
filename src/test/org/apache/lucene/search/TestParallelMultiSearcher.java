package org.apache.lucene.search;


 
import java.io.IOException;

/**
 * Unit tests for the ParallelMultiSearcher 
 */
public class TestParallelMultiSearcher extends TestMultiSearcher {

	public TestParallelMultiSearcher(String name) {
		super(name);
	}

	protected MultiSearcher getMultiSearcherInstance(Searcher[] searchers)
		throws IOException {
		return new ParallelMultiSearcher(searchers);
	}

}
