/**
 * PointscalcSOAPImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.pointscalc;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedLong;

import com.dataaccess.www.webservicesserver.NumberConversionLocator;
import com.dataaccess.www.webservicesserver.NumberConversionSoapType;

public class PointscalcSOAPImpl implements org.example.www.pointscalc.Pointscalc_PortType{
    public java.lang.String pointsToMoney(int points, java.lang.String name) throws java.rmi.RemoteException {
    	
    	NumberConversionLocator service; 
		NumberConversionSoapType stub; 
		String numberStr = "";
    	
		int euros = points * 318;
		UnsignedLong eurosConv = new UnsignedLong(euros);
		
		service = new NumberConversionLocator();
		
		try { 
			  stub = service.getNumberConversionSoap(); 
		
		} catch (ServiceException e) { 
			  e.printStackTrace(); 
			  return numberStr;
		}
		
		
    	try {
			  numberStr = stub.numberToWords(eurosConv);
			  
		} catch (RemoteException e) {
			  e.printStackTrace();
		} catch (IllegalArgumentException e) {
			  e.printStackTrace();
		}
    	String out = name + " you have " + points + " points. You will receive " + numberStr + " (" + euros + ") Euro.";
        return out ;
    }

}
