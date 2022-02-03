package client;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

public class ClientRestItems {

	
	private static UriBuilder getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8080/prj-restItems/rest");
	}
	
	public static void main(String[] args) {  
		
	    Client client = ClientBuilder.newClient();
	    
	    //List all items
		WebTarget target =  client.target(getBaseURI().path("items").build());
		//(GET) XML Request
		String result = target.request("application/xml").get(String.class); 
		System.out.println("Result: "+result);
		
		//EJERCICIO: Listar todos los items en JSON.	
		String jsonResult = target.request("application/json").get(String.class);
		System.out.println("Lista de items: " + jsonResult);
		
		String itemId ="REF00001";
		long amount = 2;
		Entity<String> entity = Entity.entity(""+amount,MediaType.TEXT_PLAIN_TYPE);
		
		//EJERCICIO: Decrementar la cantidad del item "itemId" 
		//           en una cantidad "amount" (llamada PUT enviando "entity").
		target.request("application/json").put(entity);
		
		//EJERCICIO: Obtener y mostrar en pantalla una representaci—n del item "itemId" para 
		//           comprobar que efectivamente se ha decrementado la cantidad. (GET)
		String res = target.request("application/json").get(itemId.getClass());
		System.out.println(res);
	}
}
