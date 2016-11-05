package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TestRegisterService implements CommandLineRunner {
	@Autowired
	private DiscoveryClient discoveryClient;

	public static void main(String[] args) {
		SpringApplication.run(TestRegisterService.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// basic configuration for log4j
		// TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
		BasicConfigurator.configure();
	}
}
