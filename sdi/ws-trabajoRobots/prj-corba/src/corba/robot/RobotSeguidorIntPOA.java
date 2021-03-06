package corba.robot;


/**
* corba/robot/RobotSeguidorIntPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

public abstract class RobotSeguidorIntPOA extends org.omg.PortableServer.Servant
 implements corba.robot.RobotSeguidorIntOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("ObtenerEstado", new java.lang.Integer (0));
    _methods.put ("ModificarEscenario", new java.lang.Integer (1));
    _methods.put ("ModificarObjetivo", new java.lang.Integer (2));
    _methods.put ("ModificarPosicion", new java.lang.Integer (3));
    _methods.put ("ModificarLider", new java.lang.Integer (4));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // corba/robot/RobotSeguidorInt/ObtenerEstado
       {
         corba.instantanea.EstadoRobotDHolder est = new corba.instantanea.EstadoRobotDHolder ();
         this.ObtenerEstado (est);
         out = $rh.createReply();
         corba.instantanea.EstadoRobotDHelper.write (out, est.value);
         break;
       }

       case 1:  // corba/robot/RobotSeguidorInt/ModificarEscenario
       {
         corba.khepera.escenario.EscenarioD esc = corba.khepera.escenario.EscenarioDHelper.read (in);
         this.ModificarEscenario (esc);
         out = $rh.createReply();
         break;
       }


  // raises (CORBA::COMM_FAILURE);
       case 2:  // corba/robot/RobotSeguidorInt/ModificarObjetivo
       {
         corba.khepera.robot.PosicionD NuevoObj = corba.khepera.robot.PosicionDHelper.read (in);
         this.ModificarObjetivo (NuevoObj);
         out = $rh.createReply();
         break;
       }

       case 3:  // corba/robot/RobotSeguidorInt/ModificarPosicion
       {
         corba.khepera.robot.PosicionD npos = corba.khepera.robot.PosicionDHelper.read (in);
         this.ModificarPosicion (npos);
         out = $rh.createReply();
         break;
       }

       case 4:  // corba/robot/RobotSeguidorInt/ModificarLider
       {
         int idLider = in.read_ulong ();
         this.ModificarLider (idLider);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:corba/robot/RobotSeguidorInt:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public RobotSeguidorInt _this() 
  {
    return RobotSeguidorIntHelper.narrow(
    super._this_object());
  }

  public RobotSeguidorInt _this(org.omg.CORBA.ORB orb) 
  {
    return RobotSeguidorIntHelper.narrow(
    super._this_object(orb));
  }


} // class RobotSeguidorIntPOA
