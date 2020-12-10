package org.apache.lucene.analysis.ru;



import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;

public class TestRussianStem extends TestCase
{
    private ArrayList words = new ArrayList();
    private ArrayList stems = new ArrayList();

    public TestRussianStem(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        //System.out.println(new java.util.Date());
        String str;
        
        File dataDir = new File(System.getProperty("dataDir"));

        // open and read words into an array list
        BufferedReader inWords =
            new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(new File(dataDir, "/org/apache/lucene/analysis/ru/wordsUnicode.txt")),
                    "Unicode"));
        while ((str = inWords.readLine()) != null)
        {
            words.add(str);
        }
        inWords.close();

        // open and read stems into an array list
        BufferedReader inStems =
            new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(new File(dataDir, "/org/apache/lucene/analysis/ru/stemsUnicode.txt")),
                    "Unicode"));
        while ((str = inStems.readLine()) != null)
        {
            stems.add(str);
        }
        inStems.close();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testStem()
    {
        for (int i = 0; i < words.size(); i++)
        {
            //if ( (i % 100) == 0 ) System.err.println(i);
            String realStem =
                RussianStemmer.stem(
                    (String) words.get(i),
                    RussianCharsets.UnicodeRussian);
            assertEquals("unicode", stems.get(i), realStem);
        }
    }

    private String printChars(String output)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < output.length(); i++)
            {
            s.append(output.charAt(i));
        }
        return s.toString();
    }
}
