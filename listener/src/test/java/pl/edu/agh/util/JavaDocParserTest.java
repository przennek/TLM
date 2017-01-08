package pl.edu.agh.util;

import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;

import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 01.01.2017.
 */
@TestType(UnitTest)
public class JavaDocParserTest {
    @Test
    public void setup() {

    }
}

// tlm-token: f133b645-3aac-45ee-a3ba-ca33617bcd8c