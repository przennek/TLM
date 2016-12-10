package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import pl.edu.agh.logger.TLMLogger;
import org.springframework.security.access.annotation.Secured;
import pl.edu.agh.messaging.Receiver;
import pl.edu.agh.sessionmanager.SessionManager;

@EnableDiscoveryClient
@SpringBootApplication
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@ComponentScan("pl.edu.agh")
public class TestRegisterService implements CommandLineRunner {
    private static TLMLogger logger = TLMLogger.getLogger(TestRegisterService.class.getName());

    final Receiver receiver;

    @Autowired
    public TestRegisterService(Receiver receiver) {
        this.receiver = receiver;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestRegisterService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
        BasicConfigurator.configure();
        logger.info("Test register service is running.", null);
        receiver.register("auth-exchange", SessionManager::addId, "auth.token.broadcast.login");
        receiver.register("auth-exchange", SessionManager::deleteId, "auth.token.broadcast.logout");
    }
}
