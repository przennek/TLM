package pl.edu.agh.global;

import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;

import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 01.01.2017.
 */
@TestType(UnitTest)
public class Log4jConfigListenerTest {
    @Test
    public void setup() {

    }
}

// tlm-token: bfff37d2-db9d-47c3-bd62-eabcff61eb17