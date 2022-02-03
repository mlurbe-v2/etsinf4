package datamodel;

import java.util.HashMap;
import java.util.Map;

public enum DaoItems {
	instance;
	  private Map<String, Item> contentProvider = new HashMap<String, Item>();
	  
	  private DaoItems() {
	    
      //EJERCICIO: Crear algunos items por defecto para las pruebas.
	Item item = new Item("REF00001","Ordenador", 50);
	contentProvider.put("REF00001", item);
	
	item = new Item("REF00002","Teclado", 20);
	contentProvider.put("REF00002", item);
	
	item = new Item("REF00003","Raton", 150);
	contentProvider.put("REF00003", item);
	    
	  }
	  public Map<String, Item> getModel(){
	    return contentProvider;
	  }
}
