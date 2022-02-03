package camara;

import comm.*;
import corba.instantanea.*;
import corba.camara.*;
import corba.camara.suscripcionD;
import corba.camara.IPYPortD;
import corba.robot.*;
import java.util.LinkedList;

import org.omg.CORBA.COMM_FAILURE;

import com.sun.corba.se.spi.ior.IOR;

import java.util.Collections;
import java.util.Iterator;

public class CamaraIntServerImpl extends corba.camara.CamaraIntPOA {

   private org.omg.PortableServer.POA poa_;
   private org.omg.CORBA.ORB orb_;

   private LinkedList<String> listaRobots = new LinkedList<String>();
   private LinkedList<EstadoRobotD> listaEstados = new LinkedList<EstadoRobotD>();
   InstantaneaD instantanea;
   private int nrobots;
   private IPYPortD ipyport;

    public

    CamaraIntServerImpl(org.omg.CORBA.ORB orb, org.omg.PortableServer.POA poa, IPYPortD iport) 
    {
        orb_ = orb;
        poa_ = poa;
        ipyport = new IPYPortD(iport.ip, iport.port);
        
        nrobots = 0;
    }


    public org.omg.PortableServer.POA
    _default_POA()
    {
        if(poa_ != null)
            return poa_;
        else
            return super._default_POA();
    }

    //
    // IDL:corba/Camara/CamaraInt/SuscribirRobot:1.0
    //
    public synchronized suscripcionD
    SuscribirRobot(String IORrob)
    {
       //EJERCICIO: Implementar la suscripcion al robot
    	listaRobots.add(IORrob);
    	nrobots++;
    	suscripcionD suscripcion = new suscripcionD(nrobots, ipyport);
    	System.out.println("Robot suscrito " + IORrob);
		return suscripcion;

    }
    
    public void start(){
        new CamaraDifusion(ipyport).start();
    }

    //------------------------------------------------------------------------------
    // La clase anidada CamaraDifusion
    //------------------------------------------------------------------------------
    class CamaraDifusion extends Thread{
     private Difusion difusion;
     
      //------------------------------------------------------------------------------
      public CamaraDifusion(IPYPortD iport){
         difusion = new DifusionJMS(iport);
      }

      //------------------------------------------------------------------------------
      public void run(){
        corba.instantanea.EstadoRobotDHolder st = new EstadoRobotDHolder();
        String ior=null;
        LinkedList<String> listaFallos = new LinkedList<String>();
        int nEstados = 0;
        
         while(true){
           listaEstados.clear();
           listaFallos.clear();
           for (Iterator<String> i = listaRobots.iterator(); i.hasNext(); ){
             try {
                //EJERCICIO: invocar via CORBA el metodo ObtenerEstado y anyadir
               //el estado del robot correspondiente a la lista de estados  
            	 ior = i.next();
            	 org.omg.CORBA.Object obj = orb_.string_to_object(ior);
            	 RobotSeguidorInt robot = (RobotSeguidorInt) obj; 
            	 robot.ObtenerEstado(st);
            	 listaEstados.add(st.value);
            	 nEstados++;

             } catch (COMM_FAILURE e){
                 System.out.println("Detectado fallo Robot: " + ior );
               //EJERCICIO: anyadir el robot caido a la lista de fallos 
                 listaRobots.remove(ior);
                 listaFallos.add(ior);
                 
            } 
          }

           //EJERCICIO: crear una instantanea a partir de la lista de estados de los robots. 
           instantanea = new InstantaneaD((EstadoRobotD[]) listaEstados.toArray(new EstadoRobotD[0])); 
           
           //EJERCICIO: difundir la instantanea 
           difusion.sendObject(instantanea);
           
           try{
               Thread.sleep(400);
           }catch(InterruptedException e){
               e.printStackTrace();
           }
        }
      }
    }
}
