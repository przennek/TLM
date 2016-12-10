package pl.edu.agh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import pl.edu.agh.logger.TLMLogger;

@EnableEurekaServer
@SpringBootApplication
public class EurekaService {
    private static TLMLogger logger = TLMLogger.getLogger(EurekaService.class.getName());

    public static void main(String[] args) {
        logger.info("Discovery service is running.", null);
        SpringApplication.run(EurekaService.class, args);
    }
}