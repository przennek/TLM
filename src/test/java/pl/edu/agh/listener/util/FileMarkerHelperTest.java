package pl.edu.agh.listener.util;

import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.edu.agh.listener.annotated.annotations.TestType;
import pl.edu.agh.listener.annotated.listenerclasses.FileMarker;
import pl.edu.agh.listener.exceptions.TokenCouldNotBeParsedException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static pl.edu.agh.listener.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 24.10.2016.
 */
@TestType(UnitTest)
public class FileMarkerHelperTest {
    private static final String TOKEN_RESULT = "testing";
    private File testFile;
    private Path path;

    @BeforeMethod
    public void prepareFile() throws IOException {
        testFile = new File("src/main/resources/tmp_test.file");
        testFile.createNewFile();
        path = testFile.toPath();
    }

    @AfterMethod
    public void deleteFile() {
        testFile.delete();
    }

    @Test
    public void shouldGenerateToken() throws Exception {
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
        // test file

        // when
        String token = FileMarkerHelper.markTestClass(path);

        // then
        assertEquals(token, readToken(path));
    }

    @Test
    public void shouldReturnTokenThenDelete() throws TokenCouldNotBeParsedException, IOException {
        // given
        FileMarkerHelper.markTestClass(path);

        // when
        String token = FileMarkerHelper.getToken(path);

        // then
        assertEquals(token, readToken(path));

        // when
        FileMarkerHelper.deleteMark(path);

        // then
        assertEquals(readToken(path), "");
    }

    @Test
    public void shouldNotReturnToken() throws TokenCouldNotBeParsedException {
        // given
        // test file without token

        // when
        String token = FileMarkerHelper.getToken(path);

        // then
        assertEquals(token, "");
    }

    @Test
    public void shouldReturnTestPath() throws IOException {
        // given
        ITestNGMethod method = mock(ITestNGMethod.class);
        ITestClass clazz = mock(ITestClass.class);

        // when
        when(method.getTestClass()).thenReturn(clazz);
        when(clazz.toString()).thenReturn("[TestClass name=class pl.edu.agh.listener.util.FileMarkerHelperTest]");
        Path mypath = FileMarkerHelper.preparePath(method);

        // then
        assertEquals(mypath.toString(), "src/test/java/pl/edu/agh/listener/util/FileMarkerHelperTest.java");
    }

    private String readToken(Path path) throws IOException {
        final byte[] readToken = (byte[]) Files.getAttribute(path, "user:test_class_id");
        return new String(readToken, StandardCharsets.UTF_8);
    }
}
