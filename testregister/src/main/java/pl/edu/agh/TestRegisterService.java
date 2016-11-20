package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import pl.edu.agh.messaging.Receiver;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("pl.edu.agh")
public class TestRegisterService implements CommandLineRunner {
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
        receiver.register("test-exchange", System.out::println, "test.*", "test-pipe.info.*");
        receiver.register("auth-pipe", System.out::println, "broadcast.*");
    }
}
