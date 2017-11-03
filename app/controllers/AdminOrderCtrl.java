package controllers;

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
import play.Logger;

// File upload and image editing dependencies
import org.im4java.process.ProcessStarter;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import views.html.productAdmin.*;

// Import required classes
import java.util.ArrayList;
import java.util.List;

// Import models and security
import models.*;
import models.users.*;
import models.products.*;

import controllers.security.*;


// // Authenticate user
// @Security.Authenticated(Secured.class)
// // Authorise user (check if admin)
// @With(CheckIfAdmin.class)

public class AdminOrderCtrl extends Controller {

        // Get a user - if logged in email will be set in the session
	    private models.users.User getCurrentUser() {
		      models.users.User u = models.users.User.getLoggedIn(session().get("email"));
		  return u;
	    }
        

        public Result adminOrders() {
            return ok(adminOrders.render(models.users.Administrator.getLoggedIn(session().get("email"))));
        }
    
    
        // Get a list of orders
        // If id parameter is 0 then return all orders
        // Otherwise return orders by id
        public Result listOrders(Long id) {
            // Instantiate products, an Array list of orders			
            List<models.shopping.ShopOrder> orders = new ArrayList<models.shopping.ShopOrder>();

            if (id == 0) {
                // Get the list of ALL orders
                orders = models.shopping.ShopOrder.findAll();
            }
                     
            for(int i = 0; i < orders.size(); i++) {
                System.out.println(orders.get(i).OrderDate.toString());
            }

            
            // Render the list products view, passing parameters
            // products lists
            // current user - if one is logged in
            return ok();
        }
        

}