package test;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedLong;

import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;

public class NTest {

	public static void main(String[] args) {
		NumberConversionLocator service; 
		NumberConversionSoapType stub; 
		
		UnsignedLong number = new UnsignedLong(3556789);
		String numberStr = "";
			
		service = new NumberConversionLocator(); 
		
		try { 
			  stub = service.getNumberConversionSoap(); 
		
		} catch (ServiceException e) { 
			  e.printStackTrace(); 
			  return;
		}
		//
		try {
			  numberStr = stub.numberToWords(number);
			  
		} catch (RemoteException e) {
			  e.printStackTrace();
		} catch (IllegalArgumentException e) {
			  e.printStackTrace();
		}
		//
		System.out.println("The number "+ number + " is " + numberStr);
	}
	
}
