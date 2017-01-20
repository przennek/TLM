package pl.edu.agh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Description("MongoDB has to be up and running for these to pass!")
public class TlmApplicationTests extends AbstractTestNGSpringContextTests {
    private final String UNAME = "test";
    private final String PASSWD = "123";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturn200() throws Exception {
        ResponseEntity<String> entity = this.restTemplate
                .withBasicAuth(UNAME, PASSWD)
                .getForEntity("/status", String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("{status: \"ok\"}\n");
    }

    @Test
    public void shouldReturn401() throws Exception {
        ResponseEntity<String> entity = this.restTemplate
                .getForEntity("/status", String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        entity = this.restTemplate
                .withBasicAuth(UNAME + "DUMMY", PASSWD + "DUMMY")
                .getForEntity("/status", String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}