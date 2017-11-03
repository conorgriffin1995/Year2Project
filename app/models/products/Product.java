package models.products;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

import models.shopping.*;
import models.products.*;
import models.users.*;

// Product entity managed by Ebean
@Entity
public class Product extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters
    @Id
    public Long id;

    // many to many mapping
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
    public List<Category> categories; //= new ArrayList<Category>();
    
    //@OneToOne(mappedBy="product")
    //public OrderItem item = new OrderItem();
    
    // List of category ids - this will be bound to checkboxes in the view form
    public List<Long> catSelect = new ArrayList<Long>();

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public int stock;

    @Constraints.Required
    public double price;

    // Default constructor
    public  Product() {
    }

    // Constructor to initialise object
    public  Product(Long id, String name, String description, int stock, double price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price; 
    }
	
	//Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Long.class, Product.class);

    public static List<Product> findAll(String filter) {
        return Product.find.where()
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }
    
    // Find all Products for a category
    // Filter product name 
    public static List<Product> findFilter(Long catID, String filter) {
        return Product.find.where()
                        // Only include products from the matching cat ID
                        // In this case search the ManyToMany relation
                        .eq("categories.id", catID)
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }
	
}

