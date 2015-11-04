package searchcode.views;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import searchcode.Activator;

/**
 * @author ProSoft
 *
 *
 * This sample class demonstrates how to plug-in a new
 * web search view. This view displays data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a content provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different controls and icons, if needed. Alternatively,
 * a single control can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class WebView extends ViewPart  {

     /**
      * The ID of the view as specified by the extension.
      */
    public static final String ID = "searchcode.views.WebView";

    /**
     *
     */
    private  TableViewer viewer;
    /**
     * Objects used to create icons for save,delete and help control.
     */
    private  Image rImg, bImg, fImg;
    /**
     * variable for creating Tag text box control.
     */
    private  Text tags;
    /**
     * Class object to invoke WebSearcher functions.
     */
    private  WebSearcher webSearcher = new WebSearcher();
    /**
     *Integer Constants.
     */
    private static final  int FIVE_VAL = 5, SIX_VAL = 6, FIFTY_VAL = 50;
    /**
     * @author ProSoft
     *
     *The content provider class is responsible for
     *providing objects to the view. It can wrap
     *existing objects in adapters or simply return
     *objects as-is. These objects may be sensitive
     *to the current input of the view, or ignore
     *it and always show the same content
     *(like Task List, for example).
     */
    class ViewContentProvider implements IStructuredContentProvider {
        public void inputChanged(final Viewer v, final Object oldInput,
            final Object newInput) {
        }
    public void dispose() {
    }
    public Object[] getElements(final Object parent) {
      return new String[] {"One", "Two", "Three"};
    }
  }
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
    public String getColumnText(final Object obj, final int index) {
      return getText(obj);
    }
    public Image getColumnImage(final Object obj, final int index) {
      return getImage(obj);
    }
    public Image getImage(final Object obj) {
      return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }
  }
    class NameSorter extends ViewerSorter {
  }

  /**
   * The constructor.
   */
  public WebView() {
         bImg = Activator.getImageDescriptor("icons/back.png").createImage();
         fImg = Activator.getImageDescriptor("icons/forward.png").createImage();
         rImg = Activator.getImageDescriptor("icons/refresh.png").createImage();
  }

  /**
   * @author ProSoft
   *
   * This is a callback that will allow us
   * to create the viewer and initialize it.
   *
   * @param controls
   */
  public final void createPartControl(final Composite controls) {


    //Layout of controls inside the plugin view
    controls.setLayout(new GridLayout(FIVE_VAL, false));

        final Cursor cursor;
        cursor = new Cursor(controls.getDisplay(), SWT.CURSOR_HAND);

    // Create the canvas for drawing
    final Label backImg = new Label(controls, SWT.None);
    backImg.setImage(bImg); //Back
    backImg.setCursor(cursor);
    Label frwdImg = new Label(controls, SWT.NONE);
    frwdImg.setImage(fImg); //Forward
    frwdImg.setCursor(cursor);
    Label refImg = new Label(controls, SWT.NONE);
    refImg.setImage(rImg); //Refresh
    refImg.setCursor(cursor);

    // Create the address entry field and set focus to it
    tags = new Text(controls, SWT.BORDER);
    tags.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    tags.setTextLimit(FIFTY_VAL);
    tags.setFocus();

    final Combo searchList = new Combo(controls, SWT.READ_ONLY);
        String[] items = {"--Search Engines--", "Google", "Stackoverflow",
                         "Krugle", "SearchCode"};
    searchList.setItems(items);
    searchList.setText(items[0]);

    // Create the web browser
    final Browser browser = new Browser(controls, SWT.NONE);

    GridData gdBrowser = new GridData(GridData.FILL_BOTH);
    gdBrowser.horizontalAlignment = GridData.FILL;
    gdBrowser.verticalAlignment = GridData.FILL;
    gdBrowser.horizontalSpan = SIX_VAL;
    gdBrowser.verticalSpan = SIX_VAL;
    browser.setLayoutData(gdBrowser);

    //Browser Back Control
    backImg.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(final MouseEvent arg0) {
        browser.back();
      }
    });

    //Browser Forward Control
    frwdImg.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(final MouseEvent arg0) {
        browser.forward();
      }
    });

    //Browser Refresh Control
    refImg.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(final MouseEvent arg0) {
        browser.refresh();
      }
    });

    //Search Engine List
    searchList.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
          webSearcher.search(browser, searchList.getText(), tags.getText());
      }
    });
  } //End of createPartControl class


  /**
   * This method is used to assign highlighted text when passed from
   * code editor through SearchWebMenu class.
   *
   * @param selectedText  - Contains the user highlighted text
   */
  public final void setSearchText(final String selectedText) {
    tags.setText(selectedText);
  } //End of Method

  /**
   * Passing the focus request to the viewer's control.
   */
  public final void setFocus() {
    viewer.getControl().setFocus();
  } //End of Method

} //End of WebView Class
