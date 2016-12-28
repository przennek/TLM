package pl.edu.agh.util;

import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Created by Przemek on 24.10.2016.
 */
@TestType(UnitTest)
public class FileHelperTest {
    private static final String TOKEN_RESULT = "testing";
    private File testFile = null;
    private Path path;

    @BeforeClass
    public void prepareFile() throws IOException {
        testFile = new File("src\\main\\resources\\tmp_test.file\\");
        testFile.createNewFile();
        path = testFile.toPath();
    }

    @AfterClass
    public void deleteFile() {
        testFile.delete();
    }

    @Test
    public void shouldGenerateToken() throws Exception {
        // given
        String result;

        // when
        result = FileHelper.get().uniqueToken(() -> TOKEN_RESULT, String -> true);

        // then
        assertEquals(result, TOKEN_RESULT);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void shouldNotGenerateToken() {
        // should throw runtime exception
        FileHelper.get().uniqueToken(() -> TOKEN_RESULT, String -> false);
    }

    @Test
    public void shouldMarkFile() throws IOException, TokenCouldNotBeParsedException {
        // given
        // test file

        // when
        String token = FileHelper.get().markTestClass(path);

        // then
        assertEquals(token, FileHelper.get().getToken(path));
    }

    @Test
    public void shouldReturnTokenThenDelete() throws TokenCouldNotBeParsedException, IOException {
        // given
        FileHelper.get().markTestClass(path);

        // when
        String token = FileHelper.get().getToken(path);

        // then
        assertEquals(token, FileHelper.get().getToken(path));

        // when
        FileHelper.get().deleteMark(path);

        // then
        assertEquals(FileHelper.get().getToken(path), "");
    }

    @Test
    public void shouldNotReturnToken() throws TokenCouldNotBeParsedException {
        // given
        FileHelper.get().deleteMark(path);

        // when
        String token = FileHelper.get().getToken(path);

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
        when(clazz.toString()).thenReturn("[TestClass name=class pl.edu.agh.FileHelperTest]");
        Path mypath = FileHelper.get().preparePath(method);

        // then
        assertEquals(mypath.toString().replaceAll("\\\\", "").replaceAll("/", ""), "srctestjavapleduaghFileHelperTest.java");
    }
}
// tlm-token: 1a2cacf5-fbe6-4460-a13f-c251551e4038