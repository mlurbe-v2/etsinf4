package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.Properties;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

class EchoClientImpl {
	private corba.Echo target = null;
	private org.omg.CORBA.ORB orb = null;

	/**
	 * Constructor for EchoClientImpl
	 * 
	 * @throws IOException
	 */
	public EchoClientImpl() throws IOException {
		initORB(new String[0]);
	}

	/**
	 * Constructor for EchoClientImpl
	 * 
	 * @throws IOException
	 * @see java.lang.Object#Object()
	 */
	public EchoClientImpl(String[] args) throws IOException {
		initORB(args);
	}

	/**
	 * Initialize ORB.
	 *  
	 * @param args
	 * @throws IOException
	 */
	public void initORB(String[] args) throws IOException {

        
		Properties props = System.getProperties();
		props.setProperty("org.omg.CORBA.ORBClass", "com.sun.corba.se.internal.POA.POAORB");
		props.setProperty("org.omg.CORBA.ORBSingletonClass", "com.sun.corba.se.internal.corba.ORBSingleton");
		props.put("org.omg.CORBA.ORBInitialPort","1050");
		props.put("org.omg.CORBA.ORBInitialHost","localhost");

		
		// Initialize the ORB
		orb = org.omg.CORBA.ORB.init((String[])args, props);

		// ---- Uncomment below to enable Naming Service access. ----
		org.omg.CORBA.Object ncobj = null;
		try {
			ncobj = orb.resolve_initial_references("NameService");
		} catch (InvalidName e) {
			e.printStackTrace();
		}
		 NamingContextExt nc = NamingContextExtHelper.narrow(ncobj);
		 org.omg.CORBA.Object obj = null;
		try {
			obj = nc.resolve_str("EchoObject");
		} catch (NotFound e) {
			e.printStackTrace();
		} catch (CannotProceed e) {
			e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			e.printStackTrace();
		}

//		LineNumberReader input = new LineNumberReader(new FileReader("server.ior"));
//		String ior = input.readLine();
//		obj = orb.string_to_object(ior);

		target = corba.EchoHelper.narrow(obj);		
	}

	/**
	 * Obtain ORB Interface.
	 * 
	 * @return
	 */
	public corba.Echo getORBInterface() {
		return target;
	}

	/**
	 * Shutdown ORB.
	 */
	public void shutdown() {
		orb.shutdown(true);
	}

	/**
	 * Test driver for EchoClientImpl.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EchoClientImpl test = new EchoClientImpl(args);
			
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
		    		output = test.getORBInterface().echoService(input);
		    		
		    		if (input != null) {
		                
		                stdOut.println(output + "\n>");
		                stdOut.flush();
		    		}
			
		    	}

		    } catch (IOException e) {
		      System.err.println("I/O failed for connection to host: "+args[0]);
		    }
			
	

			test.shutdown();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
