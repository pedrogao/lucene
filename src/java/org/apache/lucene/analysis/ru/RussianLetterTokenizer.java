package org.apache.lucene.analysis.ru;



import java.io.Reader;
import org.apache.lucene.analysis.CharTokenizer;

/**
 * A RussianLetterTokenizer is a tokenizer that extends LetterTokenizer by additionally looking up letters
 * in a given "russian charset". The problem with LeterTokenizer is that it uses Character.isLetter() method,
 * which doesn't know how to detect letters in encodings like CP1252 and KOI8
 * (well-known problems with 0xD7 and 0xF7 chars)
 *
 * @author  Boris Okner, b.okner@rogers.com
 * @version $Id: RussianLetterTokenizer.java,v 1.3 2004/03/29 22:48:01 cutting Exp $
 */

public class RussianLetterTokenizer extends CharTokenizer
{
    /** Construct a new LetterTokenizer. */
    private char[] charset;

    public RussianLetterTokenizer(Reader in, char[] charset)
    {
        super(in);
        this.charset = charset;
    }

    /**
     * Collects only characters which satisfy
     * {@link Character#isLetter(char)}.
     */
    protected boolean isTokenChar(char c)
    {
        if (Character.isLetter(c))
            return true;
        for (int i = 0; i < charset.length; i++)
        {
            if (c == charset[i])
                return true;
        }
        return false;
    }
}
