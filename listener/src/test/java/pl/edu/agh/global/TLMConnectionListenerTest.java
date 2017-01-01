package pl.edu.agh.global;

import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;

import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 01.01.2017.
 */
@TestType(UnitTest)
public class TLMConnectionListenerTest {
    @Test
    public void setup() {

    }
}

// tlm-token: 863fce4e-47c7-42ef-82d0-f02393e62adf