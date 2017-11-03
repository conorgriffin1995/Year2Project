package controllers.security;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

// Import required classes
import java.util.ArrayList;
import java.util.List;

// Import models
import models.*;

public class LoginCtrl extends Controller {

    public Result login() {
	   // Pass a login form to the login view and render
	   return ok(login.render(Form.form(models.users.Login.class), models.users.User.getLoggedIn(session().get("email"))));
    }

    // Process the user login form
    public Result authenticate() {
        // Bind form instance to the values submitted from the form  
        Form<models.users.Login> loginForm = Form.form(models.users.Login.class).bindFromRequest();

        // Check for errors
        // Uses the validate method defined in the Login class
        if (loginForm.hasErrors()) {
            // If errors, show the form again
            return badRequest(login.render(loginForm, models.users.User.getLoggedIn(session().get("email"))));
        } 
        else {
            // User Logged in sucessfully
            // Clear the existing session
            session().clear();
            // Store the logged in email in the session
            session("email", loginForm.get().email);
            
            // Check user type
            models.users.User u = models.users.User.getLoggedIn(loginForm.get().email);
            // If admin - go to admin section
            if (u != null && "admin".equals(u.getUserType())) {
                return redirect(controllers.routes.AdminProductCtrl.index());
            }
            
            // Return to home page
            return redirect(controllers.routes.Application.index());
        }
    }	

    public Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.LoginCtrl.login()
        );
    }
}
