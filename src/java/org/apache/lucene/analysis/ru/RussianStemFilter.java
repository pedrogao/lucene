package org.apache.lucene.analysis.ru;



import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import java.io.IOException;

/**
 * A filter that stems Russian words. The implementation was inspired by GermanStemFilter.
 * The input should be filtered by RussianLowerCaseFilter before passing it to RussianStemFilter ,
 * because RussianStemFilter only works  with lowercase part of any "russian" charset.
 *
 * @author    Boris Okner, b.okner@rogers.com
 * @version   $Id: RussianStemFilter.java,v 1.5 2004/03/29 22:48:01 cutting Exp $
 */
public final class RussianStemFilter extends TokenFilter
{
    /**
     * The actual token in the input stream.
     */
    private Token token = null;
    private RussianStemmer stemmer = null;

    public RussianStemFilter(TokenStream in, char[] charset)
    {
        super(in);
        stemmer = new RussianStemmer(charset);
    }

    /**
     * @return  Returns the next token in the stream, or null at EOS
     */
    public final Token next() throws IOException
    {
        if ((token = input.next()) == null)
        {
            return null;
        }
        else
        {
            String s = stemmer.stem(token.termText());
            if (!s.equals(token.termText()))
            {
                return new Token(s, token.startOffset(), token.endOffset(),
                    token.type());
            }
            return token;
        }
    }

    /**
     * Set a alternative/custom RussianStemmer for this filter.
     */
    public void setStemmer(RussianStemmer stemmer)
    {
        if (stemmer != null)
        {
            this.stemmer = stemmer;
        }
    }
}
