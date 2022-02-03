package test;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;


import org.example.www.pointscalc.Pointscalc_ServiceLocator;
import org.example.www.pointscalc.Pointscalc_PortType;

public class PTest {

	public static void main(String[] args) {
		Pointscalc_ServiceLocator service;
		Pointscalc_PortType stub;
		
		String nombre = "Paco";
		int puntos = 3;
		
		String respuesta = "";
		
		service = new Pointscalc_ServiceLocator();
		
		try {
		stub = service.getpointscalcSOAP();
		}catch (ServiceException e) { 
			  e.printStackTrace(); 
			  return;
		}
		
		try {
			  respuesta = stub.pointsToMoney(puntos, nombre);
			  
		} catch (RemoteException e) {
			  e.printStackTrace();
		} catch (IllegalArgumentException e) {
			  e.printStackTrace();
		}
		
		System.out.println(respuesta);
	}

}
