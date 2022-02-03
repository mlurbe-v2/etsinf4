package server;

public class EchoServerImpl extends corba.EchoPOA{
	
	EchoObject obj = new EchoObject();
	@Override
	public String echoService(String input) {
		return obj.echo(input);
	}

}
