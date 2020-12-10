package analyze;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class Tokenizer {

  public static void main(String[] args) throws IOException {
    WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
    StringReader reader = new StringReader("pedro gao is world");
    TokenStream tokenStream = analyzer.tokenStream("field", reader);
    for (Token token = tokenStream.next(); token != null; token = tokenStream.next()) {
      System.out.printf("%s-%s\n", token.type(), token.termText());
    }
  }
}
