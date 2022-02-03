package services;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import datamodel.Item;
import datamodel.DaoItems;

@Path("/items")
public class ItemsResource {

	  @Context
	  UriInfo uriInfo;
	  @Context
	  Request request;


	  // Returns the list of items to the user in the browser
	  @GET
	  @Produces(MediaType.TEXT_XML)
	  public List<Item> getAllItems() {
	    List<Item> items = new ArrayList<Item>();
	    items.addAll(DaoItems.instance.getModel().values());
	    return items; 
	  }
	  
	  // Return the list of items for applications
	  @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public List<Item> getAllItemsApp() {
		  
		//EJERCICIO: Crear una lista con todos los items y retornarla.
		List<Item> items = new ArrayList<Item>();
		items.addAll(DaoItems.instance.getModel().values());
		return items;
	  }
	  
	  // Returns the number of items
	  @GET
	  @Path("count")
	  @Produces(MediaType.TEXT_PLAIN)
	  public String getCount() {
	    int count = DaoItems.instance.getModel().size();
	    return String.valueOf(count);
	  }
	  
	  @POST
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public void newItem(
		  @FormParam("id") String id,
	      @FormParam("description") String description,
	      @FormParam("amount") String amount,
	      @Context HttpServletResponse servletResponse) throws IOException {
  
		  //EJERCICIO: Crear un nuevo item utilizando los par‡metros del Form.
		  //EJERCICIO: A–adir el item creado al modelo de datos.
		  long valor = 0;
		  
		  try {
			  valor = Long.parseLong(amount);
		  }catch(NumberFormatException e) {return;}
		  
		  Item item = new Item(id,description,valor);
		  DaoItems.instance.getModel().put(id, item);
	  }
	  
	  // Tunnel
	  @Path("{item}")
	  public ItemResource getItem(@PathParam("item") String id) {
		  
		//EJERCICIO: Crear un "ItemREsource" y retornarlo.
		
	    return new ItemResource(uriInfo,request, id);
	  }	
}
