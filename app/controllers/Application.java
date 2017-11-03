package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import java.util.ArrayList;
import java.util.List;

import views.html.*;

// Import models
import models.*;

// Import security controllers
import controllers.security.*;

public class Application extends Controller {
    
    public Result index() {
		return ok(index.render("",models.users.User.getLoggedIn(session().get("email"))));
	}
	
    public Result about() {
		return ok(about.render("",models.users.User.getLoggedIn(session().get("email"))));
	}
    
    public Result contactUs() {
		return ok(contactUs.render("",models.users.User.getLoggedIn(session().get("email"))));
	}
    
    public Result manual() {
		return ok(manual.render("",models.users.User.getLoggedIn(session().get("email"))));
	}
    
    
    
}