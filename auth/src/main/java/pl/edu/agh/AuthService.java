package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.messaging.Receiver;
import pl.edu.agh.messaging.Sender;
import pl.edu.agh.sessionmanager.SessionManager;

import java.util.Optional;

/**
 * Created by przemek on 01.11.16.
 */
@EnableEurekaClient
@SpringBootApplication
@ComponentScan("pl.edu.agh")
@PropertySource(value = { "classpath:bootstrap.properties" })
public class AuthService implements CommandLineRunner {
    private static TLMLogger logger = TLMLogger.getLogger(AuthService.class.getName());

    final Receiver receiver;
    final Sender sender;

    @Autowired
    public AuthService(Receiver receiver, Sender sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(AuthService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BasicConfigurator.configure();
        logger.info("Auth service is running.", null);
        SessionManager.getAuthTokens(Optional.of(discoveryClient), sender, receiver, env.getProperty("spring.application.name"));
        receiver.register("auth-exchange", SessionManager::addId, "auth.token.broadcast.login");
        receiver.register("auth-exchange", SessionManager::deleteId, "auth.token.broadcast.logout");
    }
}
