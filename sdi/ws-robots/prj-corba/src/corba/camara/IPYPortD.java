package corba.camara;


/**
* corba/camara/IPYPortD.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* martes 14 de diciembre de 2021 09H19' CET
*/

public final class IPYPortD implements org.omg.CORBA.portable.IDLEntity
{
  public String ip = null;
  public int port = (int)0;

  public IPYPortD ()
  {
  } // ctor

  public IPYPortD (String _ip, int _port)
  {
    ip = _ip;
    port = _port;
  } // ctor

} // class IPYPortD