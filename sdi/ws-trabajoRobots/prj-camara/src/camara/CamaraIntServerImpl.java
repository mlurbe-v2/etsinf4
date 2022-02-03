package camara;

import comm.*;
import corba.instantanea.*;
import corba.khepera.escenario.EscenarioD;
import corba.camara.*;
import corba.consola.ConsolaInt;
import corba.consola.ConsolaIntHelper;
import corba.robot.*;
import khepera.escenario.Escenario;

import java.util.LinkedList;

import org.omg.CORBA.COMM_FAILURE;

import com.sun.corba.se.spi.ior.IOR;

import java.util.Collections;
import java.util.Iterator;

public class CamaraIntServerImpl extends corba.camara.CamaraIntPOA {

   private org.omg.PortableServer.POA poa_;
   private org.omg.CORBA.ORB orb_;

   private LinkedList<String> listaRobots;
   private LinkedList<String> listaConsolas;
   private LinkedList<EstadoRobotD> listaEstados;
   InstantaneaD instantanea;
   private int nrobots, nconsolas;
   private IPYPortD ipyport;
   private Escenario escenario;
   private boolean modificado;

    public

    CamaraIntServerImpl(org.omg.CORBA.ORB orb, org.omg.PortableServer.POA poa, IPYPortD iport) 
    {
        orb_ = orb;
        poa_ = poa;
        listaRobots = new LinkedList<String>();
        listaConsolas = new LinkedList<String>();
        listaEstados = new LinkedList<EstadoRobotD>();
        ipyport = new IPYPortD(iport.ip, iport.port);
        
        nrobots = 0;
        nconsolas = 0;
        escenario = new Escenario();
        modificado = false;
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
    public synchronized suscripcionD SuscribirRobot(String IORrob)
    {
       //EJERCICIO: Implementar la suscripcion al robot
    	if (!listaRobots.contains(IORrob)) {
	    	listaRobots.add(IORrob);
	    	nrobots++;
    	}
    	suscripcionD suscripcion = new suscripcionD(nrobots, ipyport, escenario.toEscenarioD());
    	System.out.println("Robot suscrito: " + IORrob);
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
                 for (Iterator<String> j = listaRobots.iterator(); i.hasNext();) {
       				try {
       					ior = j.next().toString();
       					RobotSeguidorInt robot = RobotSeguidorIntHelper.narrow(orb_
       							.string_to_object(ior));

       					robot.ModificarEscenario(escenario.toEscenarioD());

       				} catch (Exception ex) {
       					System.out.println("Detectado fallo Robot: " + ior);
       				}
                 }
                 
            } 
          }
          //Si se modifica el escenario, se informa a todos los elementos suscritos y se
          //resetean los robots.
          if(modificado) {
        	  for (Iterator<String> i = listaRobots.iterator(); i.hasNext();) {
  				try {
  					ior = i.next().toString();
  					RobotSeguidorInt robot = RobotSeguidorIntHelper.narrow(orb_
  							.string_to_object(ior));

  					robot.ModificarEscenario(escenario.toEscenarioD());

  				} catch (Exception e) {
  					System.out.println("Detectado fallo Robot: " + ior);
  				}
  			}
  			synchronized (listaConsolas) {
  				for (Iterator<String> i = listaConsolas.iterator(); i.hasNext();) {
  					try {
  						ior = i.next().toString();
  						ConsolaInt consola = ConsolaIntHelper.narrow(orb_
  								.string_to_object(ior));

  						consola.ModificarEscenario(escenario.toEscenarioD());

  					} catch (Exception e) {
  						System.out.println("Detectado fallo Consola: " + ior);
  					}
  				}
  			}

  			modificado = false;
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

	@Override
	public synchronized suscripcionD SuscribirConsola(String IORcons) {
		if (!listaConsolas.contains(IORcons)) {
			listaConsolas.add(IORcons);
	    	nconsolas++;
		}
    	suscripcionD suscripcion = new suscripcionD(nconsolas, ipyport, escenario.toEscenarioD());
    	System.out.println("Consola suscrita: " + IORcons);
		return suscripcion;
	}


	@Override
	public synchronized void BajaRobot(String IORrob) {
		listaRobots.remove(IORrob);
        System.out.println("Robot dado de baja: " + IORrob);
		
	}


	@Override
	public synchronized void BajaConsola(String IORcons) {
		listaConsolas.remove(IORcons);
        System.out.println("Consola dada de baja: " + IORcons);
		
	}


	@Override
	public synchronized ListaSuscripcionD ObtenerLista() {
		String[] listRobots = listaRobots.toArray(new String[0]);
        String[] listCons = listaConsolas.toArray(new String[0]);
        ListaSuscripcionD listas = new ListaSuscripcionD(listRobots, listCons);
        return listas;
	}


	@Override
	public IPYPortD ObtenerIPYPortDifusion() {
		return ipyport;
	}


	@Override
	public InstantaneaD ObtenerInstantanea() {
		InstantaneaD retorno = new InstantaneaD(this.listaEstados.toArray(new EstadoRobotD[0]));
        return retorno;
	}


	@Override
	public void ModificarEscenario(EscenarioD esc) {
		escenario = new Escenario(esc);	
		modificado = true;
		
	}


	@Override
	public EscenarioD ObtenerEscenario() {
		return escenario.toEscenarioD();
	}
}
