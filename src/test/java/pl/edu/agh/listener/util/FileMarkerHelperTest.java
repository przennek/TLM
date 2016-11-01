package pl.edu.agh.listener.util;

import javafx.scene.image.Image;
import org.testng.annotations.Test;
import pl.edu.agh.listener.annotated.annotations.TestType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.testng.Assert.assertEquals;
import static pl.edu.agh.listener.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 24.10.2016.
 */
@TestType(UnitTest)
public class FileMarkerHelperTest {
    private static final String TOKEN_RESULT = "testing";

    @Test
    public void shouldGenerateTokens() throws Exception {
        // given
        String result;

        // when
        result = FileMarkerHelper.uniqueToken(() -> TOKEN_RESULT, String -> true);

        // then
        assertEquals(result, TOKEN_RESULT);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldNotGenerateToken() {
        // should throw runtime exception
        FileMarkerHelper.uniqueToken(() -> TOKEN_RESULT, String -> false);
    }

    @Test
    public void shouldMarkFile() throws IOException {
        // given
        File testFile = new File("src/main/resources/tmp_test.file");
        testFile.createNewFile();
        Path path = testFile.toPath();

        // when
        String token = FileMarkerHelper.markTestClass(path);
        final byte[] readToken = (byte[]) Files.getAttribute(path, "user:test_class_id");

        // then
        assertEquals(token, new String(readToken, StandardCharsets.UTF_8));

        // cleanup
        testFile.delete();
    }
}
