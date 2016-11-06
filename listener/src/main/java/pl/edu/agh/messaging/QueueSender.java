package pl.edu.agh.messaging;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Przemek on 06.11.2016.
 */
@Deprecated
public class QueueSender {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(QueueSender.class);

    private String name;
    private String host;

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public QueueSender(String name, String host) {
        this.name = name;
        this.host = host;
        factory = new ConnectionFactory();
        factory.setHost(host);
    }

    public void send(String msg) {
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(name, false, false, false, null);
            channel.basicPublish("", name, null, msg.getBytes());
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        }
    }
}
