package org.apache.lucene.demo.html;



import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public final class Tags {

  /**
   * contains all tags for which whitespaces have to be inserted for proper tokenization
   */
  public static final Set WS_ELEMS = Collections.synchronizedSet(new HashSet());

  static{
    WS_ELEMS.add("<hr");
    WS_ELEMS.add("<hr/");  // note that "<hr />" does not need to be listed explicitly
    WS_ELEMS.add("<br");
    WS_ELEMS.add("<br/");
    WS_ELEMS.add("<p");
    WS_ELEMS.add("</p");
    WS_ELEMS.add("<div");
    WS_ELEMS.add("</div");
    WS_ELEMS.add("<td");
    WS_ELEMS.add("</td");
    WS_ELEMS.add("<li");
    WS_ELEMS.add("</li");
    WS_ELEMS.add("<q");
    WS_ELEMS.add("</q");
    WS_ELEMS.add("<blockquote");
    WS_ELEMS.add("</blockquote");
    WS_ELEMS.add("<dt");
    WS_ELEMS.add("</dt");
    WS_ELEMS.add("<h1");
    WS_ELEMS.add("</h1");
    WS_ELEMS.add("<h2");
    WS_ELEMS.add("</h2");
    WS_ELEMS.add("<h3");
    WS_ELEMS.add("</h3");
    WS_ELEMS.add("<h4");
    WS_ELEMS.add("</h4");
    WS_ELEMS.add("<h5");
    WS_ELEMS.add("</h5");
    WS_ELEMS.add("<h6");
    WS_ELEMS.add("</h6");
  }
}
