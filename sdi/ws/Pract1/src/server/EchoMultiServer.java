package server;

import java.net.*;
import java.io.*;

public class EchoMultiServer {
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) {
      int port = 7;
      try {
           serverSocket = new ServerSocket(port);
      } catch (IOException e) {
           System.out.println("EchoMultiServer: could not listen on port: " + port + ", " + e.toString());
           System.exit(1);
      }
      System.out.println("EchoMultiServer listening on port: " + port);
      
      boolean listening = true;
      while (listening) {
    	//EJERCICIO: aceptar una nueva conexión 
    	//EJERCICIO: y crear un Thread para que la gestione 
    	 Socket clientSocket = null;
    	 Thread hilo = null;
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.out.println("Error al conectarse");
		}
		if (clientSocket != null) {
          Runnable client =new EchoMultiServerThread(clientSocket);
          hilo=new Thread(client);
          hilo.start();
          System.out.println("Hilo con ID : " + hilo.getId() + " y con nombre: "+ hilo.getName());
		}
     }

     try {
          serverSocket.close();
     } catch (IOException e) {
          System.err.println("Could not close server socket." + e.getMessage());
     }
   }
}

//----------------------------------------------------------------------------
//  class EchoMultiServerThread
//----------------------------------------------------------------------------

class EchoMultiServerThread extends Thread {
    private EchoObject eo;
    private Socket clientSocket = null;
    private String myURL = "localhost";
    private BufferedReader is = null;
    private PrintWriter os = null;
    private String inputline = new String();

    EchoMultiServerThread(Socket socket) {
        super("EchoMultiServerThread");
        clientSocket = socket;
        eo = new EchoObject();
        try {
        	is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
        	os = new PrintWriter( clientSocket.getOutputStream()); 

        } catch (IOException e) {
            System.err.println("Error sending/receiving" + e.getMessage());
            e.printStackTrace();
        }
        try {
           myURL=InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
           System.out.println("Unknown Host :" + e.toString());
           System.exit(1);
       }
    }

    public void run() {
       try {
            while ((inputline = is.readLine()) != null) {
            	os.println(eo.echo(inputline));
                os.flush();

            }
            os.close();
            is.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error sending/receiving: " + e.getMessage());
            e.printStackTrace();
        }
    }
}