package pl.edu.agh.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.cassandra.repositories.TestsRepository;
import pl.edu.agh.cassandra.repositories.TestsTreeRepository;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.model.ws.DbTest;
import pl.edu.agh.model.ws.DbTestLog;
import pl.edu.agh.model.ws.TestClass;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Przemek on 28.10.2016.
 */
@RestController
public class TestRegisterEndpoint {
    private static TLMLogger logger = TLMLogger.getLogger(TestRegisterEndpoint.class.getName());

    @Autowired
    private TestsRepository testsRepository;

    @Autowired
    private TestsTreeRepository testsTreeRepository;

    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    public String registerTest(String testClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            TestClass test =  mapper.readValue(testClass, TestClass.class);
            testsRepository.updateTest(UUID.fromString(test.tokenId()), test.className(), test.moduleName(), testClass);
            return "{\"added\": true,  \"message\": \"ok\"}";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{\"added\": false, \"message\": \"error\"}";
        }
    }

    @RequestMapping(value = "/isTestInDb", method = RequestMethod.POST)
    public String isTestInDB(String testClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TestClass test =  mapper.readValue(testClass, TestClass.class);
            DbTest testObj = testsRepository.findOne(UUID.fromString(test.tokenId()));
            boolean isTestInDb = testObj != null && testObj.getJsonData().equals(testClass);
            return String.format("{\"test\": \"%s\", \"isInDB\": "+ Boolean.toString(isTestInDb)+", \"message\": \"ok\"}", test.tokenId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{\"message\": \"error\"}";
        }
    }

    @RequestMapping(value = "/isTokenUsed", method = RequestMethod.POST)
    public String isTokenUsed(String tokenId) {
        try {
            DbTest testObj = testsRepository.findOne(UUID.fromString(tokenId));
            boolean isTestInDb = testObj != null;
            return String.format("{\"token\": \"%s\", \"isInDB\": "+ Boolean.toString(isTestInDb)+"}", tokenId);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{\"message\": \"error\"}";
        }
    }

    @RequestMapping(value = "/addTestExecutionStamp", method = RequestMethod.POST)
    public String addStamp(String testFileId, String user, String timestamp, String isSuccess) {
        ObjectMapper mapper = new ObjectMapper();
        DbTestLog editLogObj = new DbTestLog().userName(user).date(Long.parseLong(timestamp)).isSuccess(Boolean.parseBoolean(isSuccess));
        try {
            testsRepository.updateTestLog(UUID.fromString(testFileId), mapper.writeValueAsString(editLogObj));
            return "{\"added\": true, \"message\": \"ok\"}";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{\"added\": false, \"message\": \"error\"}";
        }
    }

    @RequestMapping(value = "/addTestTree", method = RequestMethod.POST)
    public String addTestTree(String moduleName, String testTree) {
        try {
            testsTreeRepository.updateTree(moduleName, testTree);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return "{\"added\": false, \"message\": \"error\"}";
        }
        return "{\"added\": true, \"message\": \"ok\"}";
    }
}
