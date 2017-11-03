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
import models.users.*;

// Import security controllers
import controllers.security.*;

public class OrderCtrl extends Controller {

    // Get a user - if logged in email will be set in the session
	public models.users.Customer getCurrentUser() {
		models.users.Customer u = models.users.Customer.getLoggedIn(session().get("email"));
		return u;
	}
    	    
    public Result Orders() {
		return redirect(routes.OrderCtrl.listOrders(0));
    }

	// Get a list of orders
    // If cat parameter is 0 then return all orders
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
       
        
        // Render the list products view, passwing parameters
        // products lists
        // current user - if one is logged in
        return ok();
    }
    
    // Render the page that displays all the orders
    public Result orders() {
		return ok(orders.render(models.users.Customer.getLoggedIn(session().get("email"))));
	}
    
    
    // Delete Order
    public Result deleteOrder(Long id) {
        // Call delete method
        models.shopping.ShopOrder.find.ref(id).delete();
        // Add message to flash session 
        flash("success", "Product has been deleted");
        // Redirect home
        return redirect(routes.OrderCtrl.orders());
    }
    

    
}

    

