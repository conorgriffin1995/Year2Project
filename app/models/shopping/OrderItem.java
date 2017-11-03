package models.shopping;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.products.*;
import models.users.*;

// OrderItem entity managed by Ebean
@Entity
public class OrderItem extends Model {

    @Id
    public Long id;

    @ManyToOne
    public ShopOrder order;
    
    @ManyToOne
    public Basket basket;
    
    // Unidirection mapping - Many order items can have one product
    // Product not interested in this
    @ManyToOne
    public Product product;
    
    public int stock;
    public double price;

    // Default constructor
    public  OrderItem() {
    }
    
    public OrderItem(Product p) {
            product = p;
            stock = 1;
            price = p.price;
    }
    
    // Increment stock
    public void increaseStock() {
        stock += 1;
    }
    
    // Decrement stock
    public void decreaseStock() {
        stock -= 1;
    }
    
    
    // Calculate and return total price for this order item
    public double getItemTotal() {
        return this.price * this.stock;
    }
	
	//Generic query helper
    public static Finder<Long,OrderItem> find = new Finder<Long,OrderItem>(OrderItem.class);

    //Find all Products in the database
    public static List<OrderItem> findAll() {
        return OrderItem.find.all();
    }
	
}

