package models.users;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

//https://www.playframework.com/documentation/2.2.x/JavaGuide4

// Product entity managed by Ebean
@Entity
// specify mapped table name
@Table(name = "user")
// Map inherited classes to a single table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
// Discriminator column used to define user type
@DiscriminatorColumn(name = "userType")
// This user type is user
@DiscriminatorValue("user") 

public class User extends Model {

    //@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) 
	//public Long id;
	
	@Constraints.Required
    @Id
    public String email;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String password;


    // Default constructor
    public  User() {
    }
    // Constructor to initialise object
    public  User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

	//Generic query helper for entity User with unique id String
    public static Finder<String,User> find = new Finder<String,User>(String.class, User.class);
    
    // Static method to authenticate based on username and password
    // Returns user object if found, otherwise NULL
    public	static User authenticate(String email, String password) {
        // If found return the user object with matching username and password
        return find.where().eq("email", email).eq("password", password).findUnique();
    }

    // Check if a user is logged in (by id - email address)
    public static User getLoggedIn(String id) {
        if (id == null)
                return null;
        else
            // Find user by id and return object
            return find.byId(id);
    }
		
   
    @Transient
    public String getUserType(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
        
}

