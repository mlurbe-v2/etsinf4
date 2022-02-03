package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.compute.TaskInt;
import interfaces.echo.EchoInt;



public class TaskEcho implements TaskInt,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Override
	public Object execute() throws RemoteException {
		
		Object echoResult = null;
        try{
        	//EJERCICIO: "lookup" the Echo RMI object
        	Registry reg = LocateRegistry.getRegistry();
        	EchoInt echo = (EchoInt) reg.lookup("echoRMI");

        	  //EJERCICIO: call echo RMI object 
        	  echoResult =  (Object)echo.echo(null);
              
        }catch(Exception e){}
		return echoResult;
	}

	@Override
	public Object execute(Object params) throws RemoteException {
			
			Object echoResult = null;
	        try{
	        	//EJERCICIO: "lookup" the Echo RMI object
	        	Registry reg = LocateRegistry.getRegistry();
	        	EchoInt echo = (EchoInt) reg.lookup("echoRMI");

	        	  //EJERCICIO: call echo RMI object 
	        	  echoResult =  (Object)echo.echo((String)params);
	              
	        }catch(Exception e){}
			return echoResult;
		
	}		
		
}
	
	

