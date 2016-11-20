package pl.edu.agh.messaging;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.edu.agh.TestRegisterService;

/**
 * Created by Przemek on 19.11.2016.
 */
// TODO find alternative for this annotation (for TestNG) in Spring in action as stackoverflow is useless for this case
@SpringApplicationConfiguration(classes = TestRegisterService.class)
public class PopulateMessageTest extends AbstractTestNGSpringContextTests {
    @Test
    // semiautomatic; ain't no one have time for fully automated e2e tests here
    // TODO do it the right way, assertion based on an endpoint call of some sort
    public void shouldPopulateMessageOverTopic() throws Exception {
        Sender sender = new Sender("localhost");
        sender.sendOverTopic("test-exchange", "test.cast", "Hi over topic");
    }
}
