package searchcode.views;

/**
* @author Prosoft
*
* This class perform the operation of validating user input
* when the user provides input to the text box while searching
* or storing snippets in the repository. This class is invoked
* from the RepositoryAccessor class and also takes the user
* input as parameter.
*
* Class Modules:-
*
* verifyInput() - Perform the operation of validating user input
*                        and return 1 or 0 based upon isEmpty() function.
*
*/
public class Validator {

/**
  * @param str1 - Holds string value for checking empty or not
  * @return true or false based on empty string
  */
 public final boolean verifyInput(final String str1) {

   return str1.trim().isEmpty();
 } //End of verifyInput method

} //End of Validator class
