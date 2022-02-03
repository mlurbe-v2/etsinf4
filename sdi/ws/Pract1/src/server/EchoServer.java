package server;

import java.net.*;
import java.io.*;

class EchoServer {
   private static EchoObject eo = new EchoObject();
   private static String myURL="localhost";
   private static ServerSocket serverSocket = null;
   private static Socket clientSocket = null;
   private static BufferedReader is = null;
   private static PrintWriter os = null;
   private static String inputline = new String();

   public static void main(String[] args) {
      try {
           myURL=InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException e) {
           System.out.println("Unknown Host :" + e.toString());
           System.exit(1);
      }

      try {
           serverSocket = new ServerSocket(4000);
      } catch (IOException e) {
           System.out.println(myURL + ": could not listen on port: 4000, " + e.toString());
           System.exit(1);
      }
      System.out.println(myURL + ": EchoServer listening on port: 4000");

      try {
          boolean listening = true;
          while(listening){
            clientSocket = serverSocket.accept();
            is = new BufferedReader( new InputStreamReader(
            								         clientSocket.getInputStream()));
            os = new PrintWriter(clientSocket.getOutputStream());
            while ((inputline = is.readLine()) != null) {
            	os.println(eo.echo(inputline));
            	os.flush();
            }
          }

          os.close();
          is.close();
          clientSocket.close();
          serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error sending/receiving" + e.getMessage());
            e.printStackTrace();
      }
    }
}