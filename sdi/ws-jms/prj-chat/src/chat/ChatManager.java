package chat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TextMessage;


public class ChatManager implements javax.jms.MessageListener{

    Context context = null;
    TopicConnectionFactory factory = null;
    TopicConnection connection = null;
    String factoryName = "ConnectionFactory";
    Topic dest = null;
    TopicSession sus_session = null;
    TopicSession pub_session = null;
    TopicSubscriber subscriber = null;
    TopicPublisher publisher = null;
    String destName = "dynamicTopics/Chat";
    String username ="anonimo";  
    String convname = "laboratorio"; 
    String durablename = "Chat durable"; 
    boolean durable=false;

    ChatUI ui=null;  
    
    
    public void setUI(ChatUI miui){
    	this.ui=miui;
    }
	public ChatManager() {
        super();
	}
	
	public void setParams(String dname, String user,String conv, boolean dur, String durname) {
        this.destName=dname;
        this.username=user;
        this.convname=conv;
        this.durable=dur;
        this.durablename=durname;
	}
	public void open() {
		this.open(this.destName, this.username,this.convname, this.durable, this.durablename);
	}
	public void open(String dname, String user,String conv, boolean dur, String durname) {
        try {
            // create the JNDI initial context
            Properties env = new Properties( );
            // ActiveMQ
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
            
            context = new InitialContext(env);
            
            this.destName=dname;
            this.username=user;
            this.convname=conv;
            this.durable=dur;
            this.durablename=durname;
            
            //EJERCICIO:
            // look up the ConnectionFactory
            // look up the Destination
            // create the connection
            // setId
            // create the sessions
            // create the publisher
            // create the receiver (take into account if should be durable)
            // set message listener
            // start the connection, to enable message receipt
            
            factory = (TopicConnectionFactory) context.lookup(factoryName);
            dest = (Topic) context.lookup("dynamicTopics/" + destName);
            connection = factory.createTopicConnection();            
            connection.setClientID(username +"/"+ destName +"/"+ durablename);
            pub_session = connection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            sus_session = connection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            publisher = pub_session.createPublisher(dest);
            if (durable) {
            	
            	subscriber = sus_session.createDurableSubscriber(dest, durablename);
            	
            }
            else {
            	subscriber = sus_session.createSubscriber(dest);
            }
            subscriber.setMessageListener(this);
            // start the connection, to enable message receipt
            connection.start();
            
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        }
	}
	public void close() {
		try { 
			    subscriber.close( ); 
			    publisher.close();
			    if (durable) sus_session.unsubscribe(durablename);
				connection.close( );
			    this.factory = null;
			    this.connection = null;
			    this.dest = null;
			    this.sus_session = null;
			    this.pub_session = null;
			    this.subscriber = null;
			    this.publisher = null;
	    }catch (javax.jms.JMSException jmse){
				jmse.printStackTrace( ); 
		}
	}
	
	public void send(String text, long timespan){
		try {
              //EJERCICIO: 
			
			System.out.println(text + "("+ connection.getClientID()+")");
			TextMessage m=(TextMessage)pub_session.createTextMessage(text+"_");
			//publisher.publish(m);
			m.setStringProperty("user", username);
			m.setStringProperty("conversacion", convname);
			publisher.publish(m,
					javax.jms.DeliveryMode.PERSISTENT, 
					javax.jms.Message.DEFAULT_PRIORITY, 
					timespan);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	
	public void onMessage(Message message) {
		try { 
			TextMessage textMessage = (TextMessage) message; 
			String text = textMessage.getText( ); 
			String conv = textMessage.getStringProperty("conversacion");
			String user = textMessage.getStringProperty("user");
		    if (conv.equalsIgnoreCase(convname)){
				if (this.ui!=null) {
			    	this.ui.nuevoMensaje("--> ("+user+"):"+text+"\n");
			    } else {
			    	System.out.println("Recibido:"+" ("+user+"):"+text+"\n" );
			    }
		    }

		} catch (JMSException jmse){ jmse.printStackTrace( ); }

	}
	
}
