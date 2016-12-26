package pl.edu.agh.annotated.listenerclasses;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.annotated.annotations.TestTypes;
import pl.edu.agh.util.FileHelper;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Created by Przemek on 26.12.2016.
 */
@TestType(value = TestTypes.UnitTest, collectionEnabled = false)
public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @BeforeClass
    public void setup() {
        treeBuilder = new TreeBuilder();
    }

    @Test
    public void shouldCreateTree() throws IOException {
        // given
        String path = FileHelper.get().getTestPackagePath();

        // then
        treeBuilder.crawl2(path, false);
        System.out.println(path);
    }
}