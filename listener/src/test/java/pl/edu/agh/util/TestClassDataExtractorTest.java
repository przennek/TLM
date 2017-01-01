package pl.edu.agh.util;

/**
 * Created by Kamil on 01.11.2016.
 */
import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.model.ws.JavaDocTag;
import pl.edu.agh.model.ws.TestClass;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static pl.edu.agh.annotated.annotations.TestTypes.UnitTest;

/**
 * Test comment to test class
 *
 * @author Test author tag
 */
@TestType(UnitTest)
public class TestClassDataExtractorTest {
    private final String TEST_PATH = "\\src\\test\\java\\pl\\edu\\agh\\util\\TestClassDataExtractorTest.java";
    private TestClass parsedData;

    TestClassDataExtractorTest() throws Exception {
        parseJavaDoc();
    }

    public void parseJavaDoc() throws Exception {
        parsedData = JavaDocParser.parse(Paths.get(new File(".").getCanonicalPath() + TEST_PATH)).get(0);
    }

    @Test
    public void validateTestClassName() {
        //given
        String className;

        //when
        className = parsedData.className();

        //then
        assertEquals(className, TestClassDataExtractorTest.class.getName());
    }

    @Test
    public void validateTestClassDescription() {
        //given
        String classDescription;

        //when
        classDescription = parsedData.classComment();

        //then
        assertEquals(classDescription, "Test comment to test class");
    }

    /**
     * Test comment to test
     *
     * @param arg1 test first argument
     * @param arg2 test second argument
     * @param arg3 test third argument
     * @return the image at the specified URL
     * @author Test author tag
     * @see Image
     */
    @Test
    public void validateTestClassTags() {
        //given
        List<JavaDocTag> classTags;
        JavaDocTag validTag1 = new JavaDocTag()
                .tagName("@author")
                .tagText("Test author tag");
        List<JavaDocTag> validTags = new ArrayList<>();
        validTags.add(validTag1);

        //when
        classTags = parsedData.classTags();

        //then
        for (Integer i = 0; i < validTags.size(); i++) {
            assertEquals(classTags.get(i).tagName(), validTags.get(i).tagName());
            assertEquals(classTags.get(i).tagText(), validTags.get(i).tagText());
        }
    }
}

// tlm-token: 6b7e564e-3a72-4265-b1e8-eed351f565be