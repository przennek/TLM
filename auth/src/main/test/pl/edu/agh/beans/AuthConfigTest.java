package pl.edu.agh.beans;

import com.mongodb.client.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.annotated.annotations.TestTypes;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@ContextConfiguration(classes = AuthConfig.class)
@TestType(value = TestTypes.UnitTest)
public class AuthConfigTest extends AbstractTestNGSpringContextTests {
    @Autowired
    MongoCollection users;

    @Test
    public void shouldReturnUsers() throws Exception {
        assertThat(users.find().first(), notNullValue());
    }
}


