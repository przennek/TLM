package pl.edu.agh.priorityaware;

import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;

import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 01.01.2017.
 */
@TestType(UnitTest)
public class FileMarkerTest {
    @Test
    public void setup() {

    }
}

// tlm-token: 9883bae5-ae79-4670-93c4-b0cd9db1700e