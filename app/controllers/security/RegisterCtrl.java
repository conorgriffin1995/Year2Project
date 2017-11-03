package controllers.security;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.*;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;

import views.html.*;

//Import Required Classes
import java.util.ArrayList;
import java.util.List;

//Import models
import models.*;
import models.users.*;


//Other Imports
import play.data.format.*;
import play.data.validation.*;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.*;



//Product entity managed by ebean

@Entity
public class RegisterCtrl extends Controller{
    
    
    public Result registerForm(){
        //Instantiate a form object based on the User class
        Form<models.users.Customer> registerForm = Form.form(models.users.Customer.class);
        
        //Render the Add User view, passing the form object
        return ok(register.render(registerForm, models.users.Customer.getLoggedIn(session().get("email"))));
    }
    
    //Handle the form data when a new user is submitted.
    public Result registerFormSubmit(){
        
        //Create a user form object (to hold submitted data)
        //'Bind' the object to the submitted form (this copies the filled form)
        Form<models.users.Customer> newRegisterForm = Form.form(models.users.Customer.class).bindFromRequest();
        
        if(newRegisterForm.hasErrors()){
            
            return badRequest(register.render(newRegisterForm, models.users.Customer.getLoggedIn(session().get("email"))));
        
        }
        
        newRegisterForm.get().save();
        flash("success", "User "+ newRegisterForm.get().name + " has been registered");
        
        return redirect("/login");
         
    } 
}