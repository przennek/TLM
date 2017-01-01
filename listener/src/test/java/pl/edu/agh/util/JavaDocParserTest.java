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
// tlm-token: 31086b50-daa0-46ba-b211-1f5bdc6473c2