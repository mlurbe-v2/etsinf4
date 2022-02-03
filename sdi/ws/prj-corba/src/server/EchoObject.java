package server;

import java.net.*;
import java.text.*;
import java.util.*;

import corba.Echo;
import corba.EchoOperations;

public class EchoObject{
  String myURL="localhost";

  public EchoObject(){
     try {
          myURL=InetAddress.getLocalHost().getHostName();
     } catch (UnknownHostException e) {
          myURL="localhost";
     }
  }

  public String echo(String input) {
     Date h = new Date();
     String fecha = DateFormat.getTimeInstance(DateFormat.LONG,Locale.FRANCE).format(h);

     String ret = myURL + ":" + fecha + "> " +  input;
     try {
           Thread.sleep(3000);  ret = ret + " (retrasada 3 segundos)";
     } catch (InterruptedException e) {}

     return ret;
  }
}

