package org.apache.lucene.demo;



import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

class DeleteFiles {
  public static void main(String[] args) {
    try {
      Directory directory = FSDirectory.getDirectory("demo index", false);
      IndexReader reader = IndexReader.open(directory);

//       Term term = new Term("path", "pizza");
//       int deleted = reader.delete(term);

//       System.out.println("deleted " + deleted +
// 			 " documents containing " + term);

      for (int i = 0; i < reader.maxDoc(); i++)
	reader.delete(i);

      reader.close();
      directory.close();

    } catch (Exception e) {
      System.out.println(" caught a " + e.getClass() +
			 "\n with message: " + e.getMessage());
    }
  }
}
