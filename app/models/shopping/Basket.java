package models.shopping;

import java.util.*;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;


// Product entity managed by Ebean
@Entity
public class Basket extends Model {

    @Id
    public Long id;
    
    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST)  
    public List<OrderItem> basketItems;
    
    @OneToOne
    public Customer customer;

    // Default constructor
    public  Basket() {
    }
    
    // Add product to basket
    // Either update existing order item or ad a new one.
    public void addProduct(Product p) {
        
        boolean itemFound = false;
        // Check if product already in this basket
        // Check if item in basket
        // Find orderitem with this product
        // if found increment Stock
        for (OrderItem i : basketItems) {
            if (i.product.id == p.id) {
                i.increaseStock();
                itemFound = true;
                break;
            }
        }
        if (itemFound == false) {
            // Add orderItem to list
            OrderItem newItem = new OrderItem(p);
            // Add to items
            basketItems.add(newItem);
        }
    }
    
    public void removeItem(OrderItem item) {

        // Using an iterator ensures 'safe' removal of list objects
        // Removal of list items is unreliable as index can change if an item is added or removed elsewhere
        // iterator works with an object reference which does not change
        for (Iterator<OrderItem> iter = basketItems.iterator(); iter.hasNext();) {
            OrderItem i = iter.next();
            if (i.id.equals(item.id))
            {
                // If more than one of these items in the basket then decrement
                if (i.stock > 1 ) {
                    i.decreaseStock();
                }
                // If only one left, remove this item from the basket (via the iterator)
                else {
                    // delete object from db
                    i.delete();
                    // remove object from list
                    iter.remove();
                    break;
                }             
            }
		}
    }
    public void deleteItem(OrderItem item) {

        // Using an iterator ensures 'safe' removal of list objects
        // Removal of list items is unreliable as index can change if an item is added or removed elsewhere
        // iterator works with an object reference which does not change
        for (Iterator<OrderItem> iter = basketItems.iterator(); iter.hasNext();) {
            OrderItem i = iter.next();
            if (i.id.equals(item.id))
            {
                    // delete object from db
                    i.delete();
                    // remove object from list
                    iter.remove();
                    break;
                       
            }
		}
    }
    
    public void removeAllItems() {
        for(OrderItem i: this.basketItems) {
            i.delete();
        }
        this.basketItems = null;
    }

    public double getBasketTotal() {
        
        double total = 0;
        
        for (OrderItem i: basketItems) {
            total += i.getItemTotal();
        }
        return total;
    }
	
	//Generic query helper
    public static Finder<Long,Basket> find = new Finder<Long,Basket>(Basket.class);

    //Find all Products in the database
    public static List<Basket> findAll() {
        return Basket.find.all();
    }
	
}

