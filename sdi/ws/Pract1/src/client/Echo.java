package client;

import java.io.*;
import java.net.*;

public class Echo {
  private static EchoObjectStub2 ss;

  public static void main(String[] args) {

    if (args.length<2) {
        System.out.println("Usage: Echo <host> <port#>");
        System.exit(1);
    }
    ss = new EchoObjectStub2();
    if(ss != null) {
    ss.setHostAndPort(args[0],Integer.parseInt(args[1]));
    }

    

    BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
    PrintWriter stdOut = new PrintWriter(System.out);
    String input,output;
    try {
    	//EJERCICIO: el bucle infinito: 
    	//EJERCICIO: Leer de teclado //EJERCICIO: Invocar el stub 
    	//EJERCICIO: Imprimir por pantalla 
    	while(true) {
    		//System.out.println("Introduce cadena de texto: ");
    		input = stdIn.readLine();
    		output = ss.echo(input);
    		
    		if (input != null) {
                
                stdOut.println(output);
                stdOut.flush();
    		}
	
    	}

    } catch (IOException e) {
      System.err.println("I/O failed for connection to host: "+args[0]);
    }
  }
}