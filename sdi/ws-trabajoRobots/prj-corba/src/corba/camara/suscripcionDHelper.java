package corba.camara;


/**
* corba/camara/suscripcionDHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from robot.idl
* domingo 16 de enero de 2022 19H41' CET
*/

abstract public class suscripcionDHelper
{
  private static String  _id = "IDL:corba/camara/suscripcionD:1.0";

  public static void insert (org.omg.CORBA.Any a, corba.camara.suscripcionD that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static corba.camara.suscripcionD extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_ulong);
          _members0[0] = new org.omg.CORBA.StructMember (
            "id",
            _tcOf_members0,
            null);
          _tcOf_members0 = corba.camara.IPYPortDHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "iport",
            _tcOf_members0,
            null);
          _tcOf_members0 = corba.khepera.escenario.EscenarioDHelper.type ();
          _members0[2] = new org.omg.CORBA.StructMember (
            "esc",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (corba.camara.suscripcionDHelper.id (), "suscripcionD", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static corba.camara.suscripcionD read (org.omg.CORBA.portable.InputStream istream)
  {
    corba.camara.suscripcionD value = new corba.camara.suscripcionD ();
    value.id = istream.read_ulong ();
    value.iport = corba.camara.IPYPortDHelper.read (istream);
    value.esc = corba.khepera.escenario.EscenarioDHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, corba.camara.suscripcionD value)
  {
    ostream.write_ulong (value.id);
    corba.camara.IPYPortDHelper.write (ostream, value.iport);
    corba.khepera.escenario.EscenarioDHelper.write (ostream, value.esc);
  }

}