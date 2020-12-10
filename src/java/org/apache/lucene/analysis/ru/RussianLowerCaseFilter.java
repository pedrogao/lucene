package org.apache.lucene.analysis.ru;



import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 * Normalizes token text to lower case, analyzing given ("russian") charset.
 *
 * @author  Boris Okner, b.okner@rogers.com
 * @version $Id: RussianLowerCaseFilter.java,v 1.4 2004/03/29 22:48:01 cutting Exp $
 */
public final class RussianLowerCaseFilter extends TokenFilter
{
    char[] charset;

    public RussianLowerCaseFilter(TokenStream in, char[] charset)
    {
        super(in);
        this.charset = charset;
    }

    public final Token next() throws java.io.IOException
    {
        Token t = input.next();

        if (t == null)
            return null;

        String txt = t.termText();

        char[] chArray = txt.toCharArray();
        for (int i = 0; i < chArray.length; i++)
        {
            chArray[i] = RussianCharsets.toLowerCase(chArray[i], charset);
        }

        String newTxt = new String(chArray);
        // create new token
        Token newToken = new Token(newTxt, t.startOffset(), t.endOffset());

        return newToken;
    }
}
