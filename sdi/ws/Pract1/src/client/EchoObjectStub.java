package client;

import java.net.*;
import java.rmi.RemoteException;
import java.io.*;
import rmi.*;

public class EchoObjectStub implements EchoInt {

  private Socket echoSocket = null;
  private PrintWriter os = null;
  private BufferedReader is = null;
  private String host = "localhost";
  private int port=7;
  private String output = "Error";
  private boolean connected  = false;

  public void setHostAndPort(String host, int port) {
    this.host= host; this.port =port;
  }

  public String echo(String input)throws java.rmi.RemoteException {
    connect();
    if (echoSocket != null && os != null && is != null) {
  	try {
             os.println(input);
             os.flush();
             output= is.readLine();
      } catch (IOException e) {
        System.err.println("I/O failed in reading/writing socket");
      }
    }
    disconnect();
    return output;
  }

  private synchronized void connect() throws java.rmi.RemoteException {
	try {
		echoSocket = new Socket(host,port);
		os = new PrintWriter(echoSocket.getOutputStream());
		is = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		connected = true;
	} catch (UnknownHostException e) {
		throw new java.rmi.RemoteException();
	} catch (IOException e) {
		throw new java.rmi.RemoteException();
	} 
	


  }

  private synchronized void disconnect() throws RemoteException { 
	if(connected) {
		try {
			echoSocket.close();
			os.close();
			is.close();
		} catch (IOException e) {
			throw new java.rmi.RemoteException();
		}
		
		
		connected = false;
	}
  }
}
