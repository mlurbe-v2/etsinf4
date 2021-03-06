package client;

import java.net.*;
import java.rmi.RemoteException;
import java.io.*;
import rmi.EchoInt;
import java.util.Timer;
import java.util.TimerTask;

public class EchoObjectStub2 implements EchoInt {

  private Socket echoSocket = null;
  private PrintWriter os = null;
  private BufferedReader is = null;
  private String host = "localhost";
  private int port=7;
  private String output = "Error";
  private boolean connected  = false;
  Timeout tout=null;


  public void setHostAndPort(String host, int port) {
    this.host= host; this.port =port;
    tout = new Timeout(5,this);
  }

  public String echo(String input) throws java.rmi.RemoteException {
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
    programDisconnection();
    return output;
  }

  private synchronized void connect() throws java.rmi.RemoteException {
	  if(!connected) {
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
	  else {
		  tout.cancel();
	  }

  }

  private synchronized void disconnect() throws RemoteException{

	  if(connected) {
		try {
			echoSocket.close();
			is.close();
			os.close();
			connected = false;
		} catch (IOException e) {
			throw new java.rmi.RemoteException();
		} 
	}
  }

  private synchronized void programDisconnection(){
    tout.start();
  }

  class Timeout {
     Timer timer;
     EchoObjectStub2 stub;
     TimeoutTask task;
     int seconds;

     public Timeout (int seconds, EchoObjectStub2 stub) {
       this.seconds = seconds;
       this.stub = stub;
     }

     public void start() {
    	 timer = new Timer();
         task = new TimeoutTask();
         timer.schedule(task, seconds*1000); 
     }

     public void cancel() {
    	 
    	 task.cancel();
    	 timer.cancel();
    	 timer.purge();
         start();
     }

     class TimeoutTask extends TimerTask {
    	 public void run() 
         {
             try {
            	
				stub.disconnect();
				if(!connected)
					System.out.println("Desconectado");
				
			} catch (RemoteException e) {
				System.out.println("Error al desconectar");
				e.printStackTrace();
			}
             
             timer.cancel();
         }
     }

   }
}




