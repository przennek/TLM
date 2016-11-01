package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TlmApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(TlmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// basic configuration for log4j
		// TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
		BasicConfigurator.configure();
	}
}
