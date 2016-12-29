package pl.edu.agh.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.cassandra.repositories.TestsRepository;
import pl.edu.agh.cassandra.repositories.TestsTreeRepository;
import pl.edu.agh.model.ws.DbTest;
import pl.edu.agh.model.ws.DbTestTree;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static pl.edu.agh.annotated.annotations.TestTypes.IntegrationTest;

/**
 * Created by Kamil on 27.12.2016.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestType(IntegrationTest)
@Description("MongoDB has to be up and running for these to pass!")
public class CassandraTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestsRepository testsRepository;

    @Autowired
    private TestsTreeRepository testsTreeRepository;

    private UUID testId;
    private UUID testTreeId;


    @BeforeClass
    public void setupTest() {
        testId = UUID.randomUUID();
        testTreeId = UUID.randomUUID();
        testsRepository.addTest(testId, "Testclass", "TestModule", "{}");
        testsTreeRepository.addTree("testModule", "{}");
    }

    @AfterClass
    public void flushTest() {
        testsRepository.deleteTest(testId);
        testsTreeRepository.deleteTree("testModule");
    }

    @Test
    public void getAllTestsTest() throws Exception {
        testsRepository.updateTest(testId, "Testclass", "TestModule", "{}");
        List<DbTest> tests = testsRepository.findAllTests();
    }

    @Test
    public void getAllTestTrees() throws Exception {
        testsRepository.updateTest(testId, "Testclass", "TestModule", "{}");
        testsTreeRepository.updateTree("testModule2", "{a:'a'}");
        List<DbTestTree> testTrees = testsTreeRepository.findAllTrees();
    }


}