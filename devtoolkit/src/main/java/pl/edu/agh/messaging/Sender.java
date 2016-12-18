package pl.edu.agh.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.logger.TLMLogger;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Created by Przemek on 06.11.2016.
 */
public class Sender {
    private static TLMLogger logger = TLMLogger.getLogger(Sender.class.getName());
    private static boolean wasLogged = false;
    private String host;

    public Sender(String host) {
        this.host = host;
    }

    public void sendOverTopic(String exchangeName, String routingKey, String msg) {
        Optional<Connection> connection = Optional.empty();
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            connection = Optional.of(factory.newConnection());
            Channel channel = connection.get().createChannel();
            channel.exchangeDeclare(exchangeName, "topic");
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        } catch (IOException | TimeoutException e) {
            if (wasLogged) {
                logger.logger().error(e.getMessage(), e);
                wasLogged = true;
            } else {
                logger.error(e.getMessage(), e);
            }
        } finally {
            if(connection.isPresent()) {
                try {
                    connection.get().close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
