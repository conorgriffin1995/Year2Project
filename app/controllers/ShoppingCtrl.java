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
import models.shopping.*;

// Import security controllers
import controllers.security.*;

// Authenticate user
@Security.Authenticated(Secured.class)
// Authorise user (check if user is a customer)
@With(CheckIfCustomer.class)

public class ShoppingCtrl extends Controller {
    
    // Get a user - if logged in email will be set in the session
	private models.users.Customer getCurrentUser() {
		return models.users.Customer.getLoggedIn(session().get("email"));
	}
    
    public Result showBasket() {
        return ok(basket.render(getCurrentUser()));
    }
    
    // Add item to customer basket
    public Result addToBasket(Long id) {
        
        // Find the product
        models.products.Product p = models.products.Product.find.byId(id);
        
        // Get basket for logged in customer
        models.users.Customer customer = models.users.Customer.getLoggedIn(session().get("email"));
        
        // Check if item in basket
        if (customer.basket == null) {
            // If no basket, create one
            customer.basket = new models.shopping.Basket();
            customer.basket.customer = customer;
            customer.update();
        }
        // Add product to the basket and save
        customer.basket.addProduct(p);
        customer.update();
        
        // Show the basket contents     
        return ok(basket.render(customer));
    }
    
    // Add an item to the basket
    public Result addOne(Long itemId) {
        
        models.products.Product product = models.products.Product.find.byId(itemId);
        
        // Get the order item
        models.shopping.OrderItem item = models.shopping.OrderItem.find.byId(itemId);
        
        if(item.product.stock < item.stock +1){
            flash("error","Not enough stock");
            return redirect(controllers.routes.ShoppingCtrl.showBasket());
        }
        else{
            // Increment quantity
            item.increaseStock();
            // Save
            item.update();
            // Show updated basket
            return redirect(controllers.routes.ShoppingCtrl.showBasket());
        }
        
        
        
        
    }
    
    public Result removeOne(Long itemId) {
        
        // Get the order item
        models.shopping.OrderItem item = models.shopping.OrderItem.find.byId(itemId);
        // Get user
        models.users.Customer c = getCurrentUser();
        // Call basket remove item method
        c.basket.removeItem(item);
        c.basket.update();
        // back to basket
        return ok(basket.render(c));
        
    }

    // Empty Basket
    public Result emptyBasket() {
        
        models.users.Customer c = getCurrentUser();
        c.basket.removeAllItems();
        c.basket.update();
        
        return ok(basket.render(c));
    }
    
    public Result placeOrder() {
        models.users.Customer c = getCurrentUser();
        
        // Create an order instance
        models.shopping.ShopOrder order = new models.shopping.ShopOrder();
        
        // Associate order with customer
        order.customer = c;
        
        // Copy basket to order
        order.items = c.basket.basketItems;
        
        // Save the order now to generate a new id for this order
        order.save();
       
       // Move items from basket to order
        for (models.shopping.OrderItem i: order.items) {
            // Associate with order
            i.order = order;
            // Remove from basket
            i.basket = null;
            
            i.product.stock -= i.stock;
            // update item
            i.update();
            i.product.update();

        }              
        
        // Update the order
        order.update();
        
        // Clear and update the shopping basket
        c.basket.basketItems = null;
        c.basket.update();
        
        // Show order confirmed view
        return ok(orderConfirmed.render(c, order));
    }
    
    
        
        
    
    // View an individual order
    public Result viewOrder(long id) {
        models.shopping.ShopOrder order = models.shopping.ShopOrder.find.byId(id);
        return ok(orderConfirmed.render(getCurrentUser(), order));
    }
    
    public Result deleteItem(Long id, Long basketID) {
        // Call delete method
        List<Basket> baskets = Basket.findAll();
        for(Basket b : baskets){
            if(b.id == basketID){
                for(int i = 0; i < b.basketItems.size(); i++){
                        b.deleteItem(b.basketItems.get(i));                   
                }
            }
        }
        // Add message to flash session 
        flash("success", "Product has been deleted");
        // Redirect home
        return redirect(routes.ShoppingCtrl.showBasket());
    }
    


    

}