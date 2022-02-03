package comm;

import java.io.*;
import java.net.*;
import corba.camara.IPYPortD;

public class DifusionMulticast implements Difusion{

  MulticastSocket socket;
  corba.camara.IPYPortD ipyport;
  public InetAddress group;

//------------------------------------------------------------------------------
  public  DifusionMulticast(IPYPortD ipyport){
    this.ipyport = ipyport;

  //EJERCICIO: 
  //Crear el socket multicast 
  //EJERCICIO: 
  //Obtener la direccion del grupo 
  //EJERCICIO: 
  //Unirse al grupo 
    
    try {
		socket = new MulticastSocket(ipyport.port);
		group = InetAddress.getByName(ipyport.ip);
		socket.joinGroup(group);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }

//------------------------------------------------------------------------------
  public Object receiveObject(){

    Object object = null;
    ObjectInputStream ois = null;
    byte[] buffer;
    DatagramPacket packet;
    ByteArrayInputStream bis;


  //EJERCICIO: recibir el paquete y deserializarlo 
    
    try {
    	buffer = new byte[4096];
    	packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		buffer = packet.getData();
		bis = new ByteArrayInputStream(buffer);
	    ois = new ObjectInputStream(bis);
	    object = ois.readObject();
	} catch (Exception e) {
		e.printStackTrace();
	}

    return object;
  }

//------------------------------------------------------------------------------
  public void sendObject(Object object){

    ByteArrayOutputStream bos;
    ObjectOutputStream oos = null;
    byte[] buffer;
    DatagramPacket packet;

  //EJERCICIO: serializar el paquete y difundirlo 
    
    try {
    	bos = new ByteArrayOutputStream();
    	oos = new ObjectOutputStream(bos);
    	oos.writeObject(object);
    	buffer = bos.toByteArray();
    	packet = new DatagramPacket(buffer, buffer.length, group, ipyport.port);
		socket.send(packet);
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
}