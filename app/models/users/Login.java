package models.users;

// Login class 'backs' the login form
public class Login {

  public String email;
  public String password;

	// Validate method - invoked during error checking
	// after form based on a Login object has been submitted
	public String validate() {
		
		// Call the static authenticate method in User
		if (User.authenticate(email, password) == null) {
		  return "Invalid user or password";
		}
		return null;
	}
}

