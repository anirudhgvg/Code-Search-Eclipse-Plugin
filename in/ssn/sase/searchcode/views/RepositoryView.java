package searchcode.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import searchcode.Activator;


/**
 * 
 *  @author ProSoft
 * 
 * This RepositoryView class demonstrates how to plug-in a new repository view.
 * This view shows data obtained from the model. The sample creates a dummy
 * model on the fly, but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * This view is connected to the model using a content provider.
 * <p>
 * This view uses a content provider to define how model objects should be
 * presented in the view. Each view can present the same model objects
 * using different controls and icons, if needed. Alternatively, a single
 * control can be shared between views in order to ensure that
 * objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class RepositoryView extends ViewPart {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "searchcode.views.RepositoryView";
    private TableViewer viewer;
    private Image imageSave, imageDelete,imageHelp;

    Action addItemAction, deleteItemAction, selectAllAction;
    private Text tags;
    RepositoryAccessor repAccess = new RepositoryAccessor();

    /**
	 * This object takes 2 values:
     * 1 - Search Control
     * 2 - ShowRecent Control
     */
    private int controlSelected;

    /**
     * The content provider class is responsible for
     * providing objects to the view. It can wrap
     * existing objects in adapters or simply return
     * objects as-is. These objects may be sensitive
     * to the current input of the view, or ignore
     * it and always show the same content
     * (like Task List, for example).
     */
    class ViewContentProvider implements IStructuredContentProvider {
  public void inputChanged(Viewer v, Object oldInput, Object newInput) {
  }
  public void dispose() {
  }
  public Object[] getElements(Object parent) {
      return new String[] { "One", "Two", "Three" };
  }
    }
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
  public String getColumnText(Object obj, int index) {
      return getText(obj);
  }
  public Image getColumnImage(Object obj, int index) {
      return getImage(obj);
  }
  @Override
  public Image getImage(Object obj) {
      return PlatformUI.getWorkbench().
        getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
  }
    }
    class NameSorter extends ViewerSorter {
    }

    /**
     * Constructor of the class to initialize the class variables
     */
    public RepositoryView() {

  imageSave = Activator.getImageDescriptor("icons/save1.png").createImage();
  imageHelp = Activator.getImageDescriptor("icons/help.png").createImage();
  imageDelete = Activator.getImageDescriptor("icons/delete.png").createImage();
    }

    /**
     * This is a callback method that will allow us
     * to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite controls) {

  GridLayout grid=new GridLayout(2,false);

  //Layout of controls inside the plugin view
  controls.setLayout(grid);

  //Cursor for changing the mouse pointer on hover image
  final Cursor cursor = new Cursor(controls.getDisplay(),SWT.CURSOR_HAND);

  //For arranging controls 4 in a row in View
  GridLayout grid3=new GridLayout(4,false);

  //To create a Group control
  Group group = new Group(controls,SWT.LEFT);
  group.setLayout(grid3);
  group.setText("Search Snippets");
  group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

  Label tagLabel = new Label(group,SWT.LEFT);
  tagLabel.setText("Enter keywords");
  GridData gdTag = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
  tagLabel.setLayoutData(gdTag);
  tagLabel.setSize(60, 20);
  
  //Create the tags text box
  tags = new Text(group, SWT.BORDER);
  GridData gdTags = new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1);
  gdTags.minimumWidth= 150;
  gdTags.minimumHeight= 20;
  tags.setLayoutData(gdTags);

  //Create the search button
  Button bSearch = new Button(group, SWT.PUSH);
  bSearch.setText("Search");
  GridData gdSearch = new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1);
  gdSearch.minimumWidth=50;
  bSearch.setLayoutData(gdSearch);

  //Create the ShowRecent button
  Button bRecent = new Button(group, SWT.PUSH);
  bRecent.setText("Show Recent");
  GridData gdRecent = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
  gdRecent.minimumWidth=80;
  bRecent.setLayoutData(gdRecent);

  //To create a list to hold an array of snippets retrieved from repository
  final List snippetLinks = new List(group, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
  GridData gdLinks = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
  gdLinks.minimumWidth= 170;
  gdLinks.minimumHeight= 100;
  snippetLinks.setLayoutData(gdLinks);

  //Text box containing snippet as selected by the user from the list of tags displayed
  final Text snippet = new Text(group, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER | SWT.READ_ONLY);
  GridData gdSnippet = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
  gdSnippet.minimumWidth= 280;
  gdSnippet.minimumHeight= 100;
  snippet.setLayoutData(gdSnippet);

  //Layout for save snippet UI
  GridLayout grid2=new GridLayout(5,false);

  //Create a Group to hold controls of save repository together
  Group group3 = new Group(controls, SWT.FILL);
  group3.setText("Store Snippet");
  group3.setLayout(grid2);
  group3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

  Label tagLabel1 = new Label(group3,SWT.LEFT);
  tagLabel1.setText("Title");
  GridData gdTagLbl = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
  tagLabel1.setLayoutData(gdTagLbl);
  tagLabel1.setSize(30, 20);

  //A Text box to hold the title of a snippet as entered by user
  final Text titleInput = new Text(group3, SWT.BORDER);
  GridData gdTitle = new GridData(SWT.LEFT, SWT.TOP, true, true, 4, 1);
  gdTitle.minimumWidth= 250;
  gdTitle.minimumHeight= 20;
  titleInput.setLayoutData(gdTitle);

  Label tagLabel2 = new Label(group3,SWT.LEFT);
  tagLabel2.setText("Tags");
  GridData gridData7 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
  tagLabel2.setLayoutData(gridData7);
  tagLabel2.setSize(30, 20);

  //A Text box to hold the tags of a snippet as entered by user
  final Text tagInput = new Text(group3, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
  GridData gridData4 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
  gridData4.minimumWidth= 200;
  gridData4.minimumHeight= 40;
  tagInput.setLayoutData(gridData4);

  //Create the Save control
  final Label bSave = new Label(group3, SWT.RIGHT);
  bSave.setImage(imageSave);
  bSave.setCursor(cursor);
  GridData gridData6 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
  bSave.setLayoutData(gridData6);

  //Create the Delete control
  final Label bDelete = new Label(group3, SWT.FILL);
  bDelete.setImage(imageDelete);
  bDelete.setCursor(cursor);
  GridData gridData9 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
  bDelete.setLayoutData(gridData9);

  //Create the Help control
  final Label bHelp = new Label(group3, SWT.FILL);
  bHelp.setImage(imageHelp);
  bHelp.setCursor(cursor);
  GridData gridData10 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
  bHelp.setLayoutData(gridData10);

  
  //Text box created to hold the snippet for user to modify and save it
  final Text snippet1 = new Text(group3, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
  GridData gridData5 = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
  gridData5.minimumWidth= 300;
  gridData5.minimumHeight= 100;
  snippet1.setLayoutData(gridData5);
  
  //Listener to invoke the generate tags method of RepositoryAccessor class
  snippet1.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent arg0) {
       repAccess.generateTags(snippet1,tagInput);
      }
  });

  //Listener to invoke the Search repository method of RepositoryAccessor class
  bSearch.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {

    controlSelected =1;
    repAccess.searchSnippet(tags, snippet, snippet1, snippetLinks,titleInput,tagInput);

      }//End of method
  });//End of listener

  //Listener to invoke the searchRecent method of RepositoryAccessor class
  bRecent.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
         
    controlSelected =2;
    repAccess.searchRecent(tags,snippet, snippet1, snippetLinks,titleInput,tagInput);

      }//End of method
  });//End of listener

  //Listener to invoke the saveSnippet method of RepositoryAccessor class
  bSave.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(MouseEvent arg0){

    int saveStatus = repAccess.saveSnippet(snippet1, tagInput,titleInput);

    if(controlSelected == 1 & saveStatus == 1 ) {
        repAccess.searchSnippet(tags, snippet, snippet1, snippetLinks,titleInput,tagInput);
    } else if(controlSelected == 2 & saveStatus == 1) {
        repAccess.searchRecent(tags,snippet, snippet1, snippetLinks,titleInput,tagInput);
    }

      }//End of method
  });//End of listener

  //Listener to invoke the help method of RepositoryAccessor class
  bHelp.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(MouseEvent arg0){
    
       Display display=PlatformUI.getWorkbench().getDisplay();
       Shell shell = new Shell(display);
       shell.setText("Help Contents");
       GridLayout grid11=new GridLayout(3,false);
       
       //Layout of controls inside the plugin view
       shell.setLayout(grid11);
       
       //Gets the list of help topics to be displayed in the help window
       String helpMsg = repAccess.setTopicList(); 
       
        Link link = new Link(shell, SWT.FILL | SWT.BORDER);
        link.setText(helpMsg);
        GridData gridData1 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment=SWT.FILL;
        link.setLayoutData(gridData1);
        
        Label temp = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
        temp.setLayoutData(new GridData(GridData.FILL_VERTICAL));
    
        final Text openDialog2 = new Text(shell,SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        openDialog2.setLayoutData(new GridData(GridData.FILL_BOTH));
        /*GridData gridData10 = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
        gridData10.grabExcessVerticalSpace = true;
        gridData10.verticalAlignment=SWT.FILL;
        openDialog2.setLayoutData(gridData10);*/
       
        shell.open();
       
       //Method used to retrieve Help contents from the database and display
       // it in the Help window.
        repAccess.setHelpContents(link,openDialog2);
        
      }//End of method
  });//End of listener

  //Listener to invoke the deleteSnippet method of RepositoryAccessor class
  bDelete.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDown(MouseEvent arg0)
      {
      repAccess.deleteSnippet(snippet1,tagInput,titleInput,snippet);
      
       if(controlSelected == 1) {
         repAccess.searchSnippet(tags, snippet, snippet1, snippetLinks,titleInput,tagInput);
         snippet1.setText(" ");  
        } else if(controlSelected == 2) {
          repAccess.searchRecent(tags,snippet, snippet1, snippetLinks,titleInput,tagInput);
        }//End of If-Else
      }//End of Event method
  });//End of listener method

  //Listener to invoke the displaySnippets method of RepositoryAccessor class
  snippetLinks.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {

        repAccess.displaySnippets(snippet, snippet1, snippetLinks,titleInput,tagInput);

      }//End of Method
    });//End of listener
   }//End of createPartControl method

    /**
     * This method assigns highlighted text from code editor
     * to the tags field in repository view.
     * 
     * @param selectedText  - contains the text that the user
     *              has highlighted in his/her code editor
     */
    public void setSearchText(String selectedText) {
      tags.setText(selectedText);
    }//End of setSearchText method

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
      viewer.getControl().setFocus();
    }//End of setFocus method

}//End of Repository View class
