package corba.camara;


/**
* corba/camara/CamaraIntOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

public interface CamaraIntOperations 
{
  corba.camara.suscripcionD SuscribirRobot (String IORrob);
  corba.camara.suscripcionD SuscribirConsola (String IORcons);
  void BajaRobot (String IORrob);
  void BajaConsola (String IORcons);
  corba.camara.ListaSuscripcionD ObtenerLista ();
  corba.camara.IPYPortD ObtenerIPYPortDifusion ();
  corba.instantanea.InstantaneaD ObtenerInstantanea ();
  void ModificarEscenario (corba.khepera.escenario.EscenarioD esc);
  corba.khepera.escenario.EscenarioD ObtenerEscenario ();
} // interface CamaraIntOperations
