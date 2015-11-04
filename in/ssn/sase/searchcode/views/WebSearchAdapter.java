package searchcode.views;

import org.eclipse.swt.browser.Browser;


/**
 * @author ProSoft
 *
 * This class is used to maintain the web search engines that probably
 * allow users to view different search results while selecting different
 * search engines while searching for code snippets in the web.This class 
 * is modifiable, that is a developer can add/remove the search engine 
 * based on his preference.
 *
 * Class Module:-
 *
 * searchWeb()   - Perform the operation of redirecting the user to the
 *                          respective search engine based on the selected choice 
 *                			 from the browser control.
 */
public class WebSearchAdapter {

  /**
   * Holds the Notification Message Type like Error -0 ,Warning - 3.
   */
  private static final int MSG_TYPE = 1;

  /**
   * @param browser       		- Holds the object of the browser control
   * @param searchEngine  	- Holds an integer value that points to a
   *                        					  particular search engine
   * @param query         		- Holds the user search query text as provided
   *                        					   in the Text Box of Web View.
   */
   public final void searchWeb(final Browser browser, final String searchEngine, final String query) {

      final ShowNotification notifyUser = new ShowNotification();

      switch(searchEngine) {
          case "Google":
            browser.setUrl("www.google.com/search?q=" + query);
            break;
          case "Stackoverflow":
            browser.setUrl("www.stackoverflow.com/search?q=" + query);
            break;
          case "Krugle":
            browser.setUrl("www.opensearch.krugle.org/document/search/#query=" + query);
            break;
          case "SearchCode":
            browser.setUrl("www.searchcode.com/?q=" + query);
            break;
          default:
            notifyUser.showMessage("Please select one of the search engine form the list.",MSG_TYPE);
            break;
          } //End of switch

  } //End of method
} //End of WebSearchAdapter class

