package corba.consola;

/**
* corba/consola/ConsolaIntHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

public final class ConsolaIntHolder implements org.omg.CORBA.portable.Streamable
{
  public corba.consola.ConsolaInt value = null;

  public ConsolaIntHolder ()
  {
  }

  public ConsolaIntHolder (corba.consola.ConsolaInt initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = corba.consola.ConsolaIntHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    corba.consola.ConsolaIntHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return corba.consola.ConsolaIntHelper.type ();
  }

}
