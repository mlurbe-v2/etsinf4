package corba.instantanea;

/**
* corba/instantanea/EstadoRobotDHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
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