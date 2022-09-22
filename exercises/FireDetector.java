import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FireDetector {

    public static void main(String[] argv) throws Exception {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicPublish("fireDispatcher", "firedetector.fire", null, "EMPTY".getBytes("UTF-8"));
        System.out.println(" [x] Sent ");
    }

}