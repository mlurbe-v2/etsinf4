package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import datamodel.Item;
import datamodel.DaoItems;

public class ItemResource {

	  @Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  String id;
	  
	  public ItemResource(UriInfo uriInfo, Request request, String id) {
	    this.uriInfo = uriInfo;
	    this.request = request;
	    this.id = id;
	  }
	      
	  @GET
	  @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public Item getItem() {
	    Item item = DaoItems.instance.getModel().get(id);
	    if(item==null)
	      throw new RuntimeException("Get: Item " + id +  " not found");
	    return item;
	  }
	  
	  @PUT
	  @Consumes(MediaType.APPLICATION_XML)
	  public Response putItem(JAXBElement<Item> item) {
	    Item it = item.getValue();
	    return putAndGetResponse(it);
	  }
	  
	  @PUT
	  @Path("dec")
	  @Consumes(MediaType.TEXT_PLAIN)
	  public Response decItem(String number) {

		//EJERCICIO: decrementar la cantidad (definida por el par‡metro number) del item.
		Item item = DaoItems.instance.getModel().get(id);
		try {
			item.decAmount(Long.parseLong(number));
		}catch(NumberFormatException e) {return null;}
		Response res = Response.ok().build();
	    return res;
	  }
	  
	  @PUT
	  @Path("inc")
	  @Consumes(MediaType.TEXT_PLAIN)
	  public Response dincItem(String number) {
		  
		//EJERCICIO: incrementar la cantidad (definida por el par‡metro number) del item.
		Item item = DaoItems.instance.getModel().get(id);
		try {
			item.incAmount(Long.parseLong(number));
		}catch(NumberFormatException e) {return null;}
		Response res = Response.ok().build();
	    return res;
	  }
	  
	  @DELETE
	  public void deleteItem() {
	    Item it = DaoItems.instance.getModel().remove(id);
	    if(it == null)
	      throw new RuntimeException("Delete: Item " + id +  " not found");
	  }
	     
	  private Response putAndGetResponse(Item item) {
	    Response res;
	    if(DaoItems.instance.getModel().containsKey(item.getId())) {
	      res = Response.noContent().build();
	    } else {
	      res = Response.created(uriInfo.getAbsolutePath()).build();
	    }
	    DaoItems.instance.getModel().put(item.getId(), item);
	    return res;
	  }
}
