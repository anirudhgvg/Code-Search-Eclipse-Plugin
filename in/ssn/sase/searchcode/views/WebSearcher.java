package searchcode.views;

import org.eclipse.swt.browser.Browser;

/**
 * @author ProSoft
 *
 * This class is used to perform the operation of
 * invoking the searchWeb method present in the
 * WebSearchAdapter class.
 *
 * Class Modules:-
 *
 * search()  - used to invoked the search method and have 3 arguments.
 */
public class WebSearcher {

   /**
    * Class object to invoke the methods of WebSearchAdapter class.
    */
   private final WebSearchAdapter adapter = new WebSearchAdapter();

    /**
     * @param browser     		  - Holds the browser control object
     * @param searchEngine  	  - Holds an integer value that points to a
     *                        						particular search engine
     * @param query         		  - Holds the user search query text as provided
     *                        						in the Text Box of Web View.
     */
    public final void search(final Browser browser, final String searchEngine,
        final String query) {

        adapter.searchWeb(browser, searchEngine, query);
    } //End of Method

} //End of WebSearcher Class

