package pl.edu.agh;

import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.model.mongo.User;
import pl.edu.agh.model.mongo.UserRepository;

/**
 * Created by przemek on 01.11.16.
 */
@SpringBootApplication
public class AuthService implements CommandLineRunner {
    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(AuthService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // basic configuration for log4j
        // TODO create fancy log4j config as in http://logging.apache.org/log4j/1.2/manual.html
        BasicConfigurator.configure();

        // create and save some test users
        // TODO user creation from web app
        repository.save(new User().login("przemek").password("3uLhYwc3UmLeCr8R").role("USER"));
        repository.save(new User().login("kamil").password("s3K923jrZVt4VRbL").role("USER"));
    }
}