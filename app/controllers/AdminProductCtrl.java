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


// Authenticate user
@Security.Authenticated(Secured.class)
// Authorise user (check if admin)
@With(CheckIfAdmin.class)

public class AdminProductCtrl extends Controller {
	
    // Get a user - if logged in email will be set in the session
	private models.users.User getCurrentUser() {
		models.users.User u = models.users.User.getLoggedIn(session().get("email"));
		return u;
	}
    
    public Result index() {
        return redirect(routes.AdminProductCtrl.listProducts(0));
    }

	// Get a list of products
    // If cat parameter is 0 then return all products
    // Otherwise return products for a category (by id)
    public Result listProducts(Long cat) {
        // Get list of categories in ascending order
        List<models.products.Category> categories = models.products.Category.find.where().orderBy("name asc").findList();
        // Instantiate products, an Array list of products			
        List<models.products.Product> products = new ArrayList<models.products.Product>();

        if (cat == 0) {
            // Get the list of ALL products
            products = models.products.Product.findAll("");
        }
        else {
            // Get products for the selected category
            // Each category object contains a list of products
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
        return ok(listProducts.render(categories, products, getCurrentUser()));
    }
    
    // Load the add product view
    // Display an empty form in the view
    public Result addProduct() {   
        // Instantiate a form object based on the Product class
        Form<models.products.Product> addProductForm = Form.form(models.products.Product.class);
        // Render the Add Product View, passing the form object
        return ok(addProduct.render(addProductForm, getCurrentUser()));
    }

    // Handle the form data when a new product is submitted
    public Result addProductSubmit() {

        String saveImageMsg;

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<models.products.Product> newProductForm = Form.form(models.products.Product.class).bindFromRequest();	

        // Check for errors (based on Product class annotations)	
        if(newProductForm.hasErrors()) {
            // Display the form again
            return badRequest(addProduct.render(newProductForm, getCurrentUser()));
        }
     
        models.products.Product newProduct = newProductForm.get();
        
        // Save product now to set id (needed to update manytomany)
        newProduct.save();
        
        // Get category ids (checked boxes from form)
        // Find category objects and set categories list for this product
        for (Long cat : newProduct.catSelect) {
            newProduct.categories.add(models.products.Category.find.byId(cat));
        }

        // Update thenew Product to save categories
        newProduct.update();

        // Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");
        
        // Save the image file
        saveImageMsg = saveFile(newProduct.id, image);

        // Set a sucess message in temporary flash
        flash("success", "Product " + newProduct.name + " has been created" + " " + saveImageMsg);
            
        // Redirect to the admin home
        return redirect(routes.AdminProductCtrl.index());
    }
        
    // Update a product by ID
    // called when edit button is pressed
    public Result updateProduct(Long id) {
        // Retrieve the product by id
        models.products.Product p = models.products.Product.find.byId(id);
        // Create a form based on the Product class and fill using p
        Form<models.products.Product> productForm = Form.form(models.products.Product.class).fill(p);
        // Render the updateProduct view
        // pass the id and form as parameters
        return ok(updateProduct.render(id, productForm, models.users.User.getLoggedIn(session().get("email"))));		
    }


    // Handle the form data when an updated product is submitted
    public Result updateProductSubmit(Long id) {

        String saveImageMsg;

        // Create a product form object (to hold submitted data)
        // 'Bind' the object to the submitted form (this copies the filled form)
        Form<models.products.Product> updateProductForm = Form.form(models.products.Product.class).bindFromRequest();	

        // Check for errors (based on Product class annotations)	
        if(updateProductForm.hasErrors()) {
            // Display the form again
            return badRequest(updateProduct.render(id, updateProductForm, getCurrentUser()));
        }
        
        // Update the Product (using its ID to select the existing object))
        models.products.Product p = updateProductForm.get();
        p.id = id;
        
        // Get category ids (checked boxes from form)
        // Find category objects and set categories list for this product
        List<models.products.Category> newCats = new ArrayList<models.products.Category>();
        for (Long cat : p.catSelect) {
            newCats.add(models.products.Category.find.byId(cat));
        }
        p.categories = newCats;
        
        // update (save) this product            
        p.update();

        // Get image data
        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(p.id, image);

        // Add a success message to the flash session
        flash("success", "Product " + updateProductForm.get().name + " has been updates" + " " + saveImageMsg);
            
        // Return to admin home
        return redirect(routes.AdminProductCtrl.index());
    }


    // Delete Product
    public Result deleteProduct(Long id) {
        // Call delete method
        models.products.Product.find.ref(id).delete();
        // Add message to flash session 
        flash("success", "Product has been deleted");
        // Redirect home
        return redirect(routes.AdminProductCtrl.index());
    }
    
    // Save an image file
    public String saveFile(Long id, FilePart image) {
        if (image != null) {
            // Get mimetype from image
            String mimeType = image.getContentType();
            // Check if uploaded file is an image
            if (mimeType.startsWith("image/")) {
                // Create file from uploaded image
                File file = image.getFile();
                // create ImageMagick command instance
                ConvertCmd cmd = new ConvertCmd();
                // create the operation, add images and operators/options
                IMOperation op = new IMOperation();
                // Get the uploaded image file
                op.addImage(file.getAbsolutePath());
                // Resize using height and width constraints
                op.resize(300,200);
                // Save the  image
                op.addImage("public/images/productImages/" + id + ".jpg");
                // thumbnail
                IMOperation thumb = new IMOperation();
                // Get the uploaded image file
                thumb.addImage(file.getAbsolutePath());
                thumb.thumbnail(60);
                // Save the  image
                thumb.addImage("public/images/productImages/thumbnails/" + id + ".jpg");
                // execute the operation
                try{
                    cmd.run(op);
                    cmd.run(thumb);
                }
                catch(Exception e){
                    e.printStackTrace();
                }				
                return " and image saved";
            }
        }
        return "image file missing";	
    } 
}
