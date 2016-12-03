package pl.edu.agh;

import com.netflix.discovery.DiscoveryManager;
import com.netflix.eureka.EurekaServerContext;
import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaConstants;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by przemek on 01.11.16.
 */
@EnableEurekaClient
@SpringBootApplication
@ComponentScan("pl.edu.agh")
public class AuthService implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AuthService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // basic configuration for log4j
        // TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
        BasicConfigurator.configure();
    }
}