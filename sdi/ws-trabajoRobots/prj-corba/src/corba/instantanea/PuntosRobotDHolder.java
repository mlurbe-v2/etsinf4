package corba.instantanea;

/**
* corba/instantanea/PuntosRobotDHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

public final class PuntosRobotDHolder implements org.omg.CORBA.portable.Streamable
{
  public corba.instantanea.PuntosRobotD value = null;

  public PuntosRobotDHolder ()
  {
  }

  public PuntosRobotDHolder (corba.instantanea.PuntosRobotD initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = corba.instantanea.PuntosRobotDHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    corba.instantanea.PuntosRobotDHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return corba.instantanea.PuntosRobotDHelper.type ();
  }

}
