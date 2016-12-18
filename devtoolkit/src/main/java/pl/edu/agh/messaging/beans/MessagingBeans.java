package pl.edu.agh.messaging.beans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.edu.agh.messaging.Receiver;
import pl.edu.agh.messaging.Sender;

/**
 * Created by Przemek on 20.11.2016.
 */
@Component
public class MessagingBeans {
    @Bean
    @Qualifier("localhost")
    public Sender sender() {
        return new Sender("rabbitmq-service.default.svc.cluster.local");
    }

    @Bean
    @Qualifier("localhost")
    public Receiver receiver() {
        return new Receiver("rabbitmq-service.default.svc.cluster.local");
    }
}
