package corba.instantanea;

/**
* corba/instantanea/EstadoRobotDHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* martes 14 de diciembre de 2021 09H19' CET
*/

public final class EstadoRobotDHolder implements org.omg.CORBA.portable.Streamable
{
  public corba.instantanea.EstadoRobotD value = null;

  public EstadoRobotDHolder ()
  {
  }

  public EstadoRobotDHolder (corba.instantanea.EstadoRobotD initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = corba.instantanea.EstadoRobotDHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    corba.instantanea.EstadoRobotDHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return corba.instantanea.EstadoRobotDHelper.type ();
  }

}
