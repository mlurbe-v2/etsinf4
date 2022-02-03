package robot;

import corba.instantanea.EstadoRobotDHolder;
import corba.khepera.escenario.EscenarioD;
import corba.khepera.robot.PosicionD;
import corba.robot.RobotSeguidorInt;
import khepera.control.Braitenberg;
import khepera.control.Destino;
import khepera.control.Trayectoria;
import khepera.escenario.Escenario;
import khepera.robot.IzqDer;
import khepera.robot.Polares;
import khepera.robot.Posicion;
import khepera.robot.RobotKhepera;

import org.omg.PortableServer.POA;

import comm.*;
import corba.instantanea.*;
import corba.camara.*;

/**
 * This class is the implementation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class RobotSeguidorIntServerImpl extends corba.robot.RobotSeguidorIntPOA {

    org.omg.CORBA.ORB orb;
    private POA poa_;
    CamaraInt camara;

    String minombre;
    int miid;
    String miIOR;
    RobotSeguidorInt miref;
    PuntosRobotD mipunt;
    Polares mipos;
    PosicionD miobj;
    int miidlider;
    Escenario escenario;
    RobotKhepera robot;
    Destino dst;
    Braitenberg bra;
    Trayectoria tra; 
    IzqDer nv;
    IzqDer nv2;
    
    private InstantaneaD instantanea;
    
	/**
	 * Constructor for RobotSeguidorIntServerImpl 
	 */
	public RobotSeguidorIntServerImpl(POA poa) {
		mipos = new Polares();
        miobj = new PosicionD(500, 500);
        miidlider = -1;
        escenario = new Escenario();
        robot = new RobotKhepera(new PosicionD(60, 60), escenario, 0);
        
        dst = new Destino();
        bra  = new Braitenberg();
        poa_ = poa;
        mipunt = robot.posicionRobot();
        nv = new IzqDer();
        nv2 = new IzqDer();
		/*
		 * PosicionD centro = new PosicionD(50, 50); PosicionD[] sens = new
		 * PosicionD[9]; PosicionD[] finsens = new PosicionD[9]; PosicionD[] inter = new
		 * PosicionD[8]; for (int i = 0; i < 9; ++i) { sens[i] = new PosicionD(50, 50);
		 * finsens[i] = new PosicionD(50, 50); if(i < 8) { inter[i] = new PosicionD(50,
		 * 50); } }
		 */

        //mipunt = new PuntosRobotD(centro, sens, finsens, inter);
	}
	
	public org.omg.PortableServer.POA
    _default_POA()
    {
        if(poa_ != null)
            return poa_;
        else
            return super._default_POA();
    }

	public void ObtenerEstado(EstadoRobotDHolder est) {

	        est.value = new corba.instantanea.EstadoRobotD(minombre, miid, miIOR, miref, mipunt, miobj, miidlider); // new corba.instantanea.EstadoRobotD();
		
	}
	
    public void start(){
        new RobotDifusion().start();
    }

    //------------------------------------------------------------------------------
    // La clase anidada RobotDifusion
    //------------------------------------------------------------------------------

    class RobotDifusion extends Thread{

      private Difusion difusion;
      private EstadoRobotD sr;
      private suscripcionD sus;
      private int numRobot;
      public void run(){
      //EJERCICIO: suscribir el robot en la camara
      //EJERCICIO: crear la difusion
    	  
    	  sus = camara.SuscribirRobot(miIOR);
    	  miid=sus.id;
    	  escenario = new Escenario(sus.esc);
    	  robot = new RobotKhepera(new PosicionD(60,60),escenario,0);
    	  difusion = new DifusionJMS(sus.iport);
          
          System.out.println("Robot suscrito con " + miIOR);
          instantanea = (InstantaneaD) difusion.receiveObject();
          for(int i=0; i < instantanea.estadorobs.length ; i++) {
      		sr = instantanea.estadorobs[i];
	            //System.out.println("Robot " + i + " : " + sr.nombre);
      		if (sr.id == miid) {
                this.numRobot = i;
                
          }
      		}
          if (this.numRobot > 0) {
              miidlider = instantanea.estadorobs[numRobot - 1].id;
              
          }
        while(true){
           //EJERCICIO: recibir instantanea
        	instantanea = (InstantaneaD) difusion.receiveObject();
           //EJERCICIO: iterar sobre la lista de estados, imprimiendo el nombre de
           //todos los robots cuyo estado figura en la instantanea.
        	for(int i=0; i < instantanea.estadorobs.length ; i++) {
        		sr = instantanea.estadorobs[i];
        		if (sr.id == miidlider && sr.id != miid) {
        			miobj = instantanea.estadorobs[i].puntrob.centro;
                }
	            //System.out.println("Robot " + i + " : " + sr.nombre);
        	}
	        accionControl();  

          try{
            Thread.sleep(400);
          }catch(InterruptedException e){
            e.printStackTrace();
          }
        }
      }
    }

	@Override
	public void ModificarEscenario(EscenarioD esc) {
		escenario = new Escenario(esc);
		robot = new RobotKhepera(new PosicionD(0, 0), escenario, 0);
        System.out.println("Escenario modificado");
		
	}

	@Override
	public void ModificarObjetivo(PosicionD NuevoObj) {
		miobj = NuevoObj;
        miidlider = -1;
        System.out.println("Nuevo Objetivo: " + new Posicion(NuevoObj).toString());
		
	}

	@Override
	public void ModificarPosicion(PosicionD npos) {
		this.robot.fijarPosicion(npos);
        System.out.println("Nueva Posicion: " + new Posicion(npos).toString());
		
	}

	@Override
	public void ModificarLider(int idLider) {
		 boolean existeLider = false;
		 EstadoRobotD estadoAct;
	        if (instantanea != null) {
	            for (int i = 0; i < instantanea.estadorobs.length; ++i) {
	                estadoAct  = instantanea.estadorobs[i];
	                if (estadoAct.id == idLider && estadoAct.id != this.miid) {
	                	existeLider = true;
	                    miidlider = idLider;
	                    System.out.println("Nuevo Lider: " + idLider);
	                    break;
	                }
	            }
	            if (!existeLider) {
	                System.out.println("Lider Invalido: " + idLider);
	            }
	        }
		
	}
	
	void accionControl() {	
		robot.avanzar();  
        mipos = robot.posicionPolares();
        mipunt = robot.posicionRobot();
        tra = new Trayectoria(mipos, miobj);
        float[] ls = robot.leerSensores();
        nv = dst.calcularVelocidad((Object)tra);
        nv2 = bra.calcularVelocidad((Object)ls);
        nv.izq += nv2.izq / 90;
        nv.der += nv2.der / 90;
        robot.fijarVelocidad(nv.izq, nv.der);
    }
}
