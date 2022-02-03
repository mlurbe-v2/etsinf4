package corba.robot;


/**
* corba/robot/RobotSeguidorIntOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

public interface RobotSeguidorIntOperations 
{
  void ObtenerEstado (corba.instantanea.EstadoRobotDHolder est);
  void ModificarEscenario (corba.khepera.escenario.EscenarioD esc);

  // raises (CORBA::COMM_FAILURE);
  void ModificarObjetivo (corba.khepera.robot.PosicionD NuevoObj);
  void ModificarPosicion (corba.khepera.robot.PosicionD npos);
  void ModificarLider (int idLider);
} // interface RobotSeguidorIntOperations