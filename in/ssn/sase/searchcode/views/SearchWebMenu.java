package searchcode.views;
/**
 * 
 */

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author ProSoft
 *
 *This class performs the operation of capturing the
 *user highlighted text dynamically from the code editor and 
 *making it able to pass between views i.e. from code editor 
 *view to the Web  View.
 *
 */
  public class SearchWebMenu implements IObjectActionDelegate {
  
    @Override
    public void run(IAction arg0) {
  
      ShowNotification displayMessage = new ShowNotification();
      
      try {               
        //this code gets the textbox control from WebView  
        WebView view1 = (WebView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(WebView.ID);
          IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        
        if ( part instanceof ITextEditor ) {
          
          final ITextEditor editor = (ITextEditor)part;
          ISelection sel = editor.getSelectionProvider().getSelection();
            
          if ( sel instanceof TextSelection ) {
                // Here is your String
                final TextSelection textSel = (TextSelection)sel;
              
                //This line assigns user selected text to the textbox present in sampleview
              view1.setSearchText(textSel.getText());
               }//End of Inner IF
        }//End of Outer IF
      } catch ( Exception errMsg ) {
      displayMessage.showMessage("The following error occured:\n" + errMsg.getMessage(),0);
      }//End of Catch
    }//End of Method

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
    }//End of Method

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
    }//End of Method
  }//End of SearchWebMenu class
