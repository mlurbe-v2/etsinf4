package datamodel;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item {


	private String id;
	  private long amount;
	  private String description;
	  
	  public Item(){
	    
	  }
	  public Item(String idnew, String desc, long n){
		  this.id = idnew;
		  this.description = desc;
		  this.amount = n;    
	  }
	  
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}
		
		public void decAmount(long amount) {
			this.amount -= amount;
		}
		public void incAmount(long amount) {
			this.amount += amount;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
}
