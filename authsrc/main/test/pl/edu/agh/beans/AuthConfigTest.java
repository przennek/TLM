package pl.edu.agh.beans;

import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Przemek on 20.11.2016.
 */
@ContextConfiguration(classes = AuthConfig.class)
public class AuthConfigTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MongoCollection users;

    @Test
    public void shouldReturnUsers() throws Exception {
        System.out.println(users.find().first());
    }
}