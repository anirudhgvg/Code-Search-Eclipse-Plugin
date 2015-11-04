package searchcode.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author ProSoft
 *
 * This class perform the operation of showing
 * notification messages to the user during runtime
 * in a pop-up window. This class takes user friendly
 * messages from various classes and displays it the
 * pop-up window using the method mentioned below.
 *
 * Class Modules:-
 *
 * showMessage()  - Perform the operation of displaying the user
 *                  			  friendly messages in a pop-up window
 *
 */
public class ShowNotification {

  /**
   * @param msg      		- Contains the user friendly message to be displayed
   * @param msgType 	- Contains the user friendly message type i.e. Error or Information
   */
  public final void showMessage(final String msg, final int msgType) {

    Display disp = Display.getCurrent();
      Shell shell = disp.getActiveShell();

      switch(msgType) {
      //Error Message
      case 0:
        MessageDialog.openError(shell, "Alert Message", msg);
        break;
      //Information Message
      case 1:
        MessageDialog.openInformation(shell, "Alert Message", msg);
        break;
      //Question Message
        default:
          MessageDialog.openWarning(shell, "Alert Message", msg);
          break;
      }

  } //End of showMessage method

} //End of showNotification class
