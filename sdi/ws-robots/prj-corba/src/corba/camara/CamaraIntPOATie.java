package corba.camara;


/**
* corba/camara/CamaraIntPOATie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* martes 14 de diciembre de 2021 09H19' CET
*/

public class CamaraIntPOATie extends CamaraIntPOA
{

  // Constructors

  public CamaraIntPOATie ( corba.camara.CamaraIntOperations delegate ) {
      this._impl = delegate;
  }
  public CamaraIntPOATie ( corba.camara.CamaraIntOperations delegate , org.omg.PortableServer.POA poa ) {
      this._impl = delegate;
      this._poa      = poa;
  }
  public corba.camara.CamaraIntOperations _delegate() {
      return this._impl;
  }
  public void _delegate (corba.camara.CamaraIntOperations delegate ) {
      this._impl = delegate;
  }
  public org.omg.PortableServer.POA _default_POA() {
      if(_poa != null) {
          return _poa;
      }
      else {
          return super._default_POA();
      }
  }
  public corba.camara.suscripcionD SuscribirRobot (String IORrob)
  {
    return _impl.SuscribirRobot(IORrob);
  } // SuscribirRobot

  private corba.camara.CamaraIntOperations _impl;
  private org.omg.PortableServer.POA _poa;

} // class CamaraIntPOATie
