package pl.edu.agh.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Przemek on 06.11.2016.
 */
public class Sender {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Sender.class);
    private String host;

    public Sender(String host) {
        this.host = host;
    }

    public void sendOverTopic(String exchangeName, String routingKey, String msg) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, "topic");

            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

            connection.close();
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        }
    }
}
