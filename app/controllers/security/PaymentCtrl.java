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
public class PaymentCtrl extends Controller{
    
    
    public Result paymentForm(){
        //Instantiate a form object based on the User class
        Form<models.users.Customer> paymentForm = Form.form(models.users.Customer.class);
        
        //Render the Add User view, passing the form object
        System.out.println("1");
        return ok(payment.render(paymentForm, models.users.Customer.getLoggedIn(session().get("email"))));
    }
    
    //Handle the form data when a new user is submitted.
    public Result paymentFormSubmit(){
        
        //Create a user form object (to hold submitted data)
        //'Bind' the object to the submitted form (this copies the filled form)
        Form<models.users.Customer> newPaymentForm = Form.form(models.users.Customer.class).bindFromRequest();
        System.out.println("2");
        if(newPaymentForm.hasErrors()){
            
            return badRequest(payment.render(newPaymentForm, models.users.Customer.getLoggedIn(session().get("email"))));
        
        }
        
        newPaymentForm.get().save();
        flash("success", "Transaction "+ newPaymentForm.get().name + " has been completed.");
        
        return redirect("/orderConfirmed");
         
    } 
}