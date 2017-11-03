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
import models.products.*;

// Import security controllers
import controllers.security.*;

public class ProductCtrl extends Controller {

    // Get a user - if logged in email will be set in the session
	public models.users.User getCurrentUser() {
		models.users.User u = models.users.User.getLoggedIn(session().get("email"));
		return u;
	}
    	    
    public Result Products() {
		return redirect(routes.ProductCtrl.listProducts(0, ""));
    }

	// Get a list of products
    // If cat parameter is 0 then return all products
    // Otherwise return products for a category (by id)
    public Result listProducts(Long cat, String filter) {
        // Get list of all categories in ascending order
        List<models.products.Category> categories = models.products.Category.find.where().orderBy("name asc").findList();
        // Instantiate products, an Array list of products			
        List<models.products.Product> products = new ArrayList<models.products.Product>();
    
        if (cat == 0) {
            // Get the list of ALL products
            products = models.products.Product.findAll(filter);
        }
        else {
            // Get products for the selected category
            // Each category object contains a list of products
            products = Product.findFilter(cat, filter);
            
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).id == cat) {
                    products = categories.get(i).products;
                    break;
                }
            }
        }
        // Render the list products view, passwing parameters
        // categories and products lists
        // current user - if one is logged in
        return ok(listProducts.render(categories, products, cat, filter, getCurrentUser()));
    }
}
