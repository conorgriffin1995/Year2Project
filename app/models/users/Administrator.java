package models.users;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

@Entity
// This is a User of type admin
@DiscriminatorValue("admin")

// Administrator inherits from the User class
public class Administrator extends User{
	
	public Administrator(String email, String name, String password)
	{
		super(email, name, password);
	}
	
} 