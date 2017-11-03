package models.products;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

// Product entity managed by Ebean
@Entity
public class Category extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters


    @Id
    public Long id;

    @Constraints.Required
    public String name;

    // Category contains many products
    @ManyToMany(cascade = CascadeType.ALL)
    public List<Product> products; // = new ArrayList<Product>();

    // Default constructor
    public  Category() {
    }

    public  Category(Long id, String name, List<Product> products) {
			this.id = id;
			this.name = name;
			this.products = products;
    }

	//Generic query helper for entity Computer with id Long
    public static Finder<Long,Category> find = new Finder<Long,Category>(Long.class, Category.class);
    
    //Find all Products in the database
    public static List<Category> findAll() {
        return Category.find.all();
    }

	// Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Category c: Category.find.orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }
    

    // Check if a product is in a category
    public static boolean inCategory(Long category, Long product) {
        return find.where()
            .eq("products.id", product)
            .eq("id", category)
            .findRowCount() > 0;
    } 
	
}

