package pl.edu.agh.beans;

import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.annotated.annotations.TestTypes;

/**
 * Created by Przemek on 20.11.2016.
 */
@ContextConfiguration(classes = AuthConfig.class)
@TestType(value = TestTypes.UnitTest)
public class AuthConfigTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MongoCollection users;

    @Test
    public void shouldReturnUsers() throws Exception {
        System.out.println(users.find().first());
    }
}

// tlm-token: e65a5af2-60fb-45f1-b197-f17392bb991e