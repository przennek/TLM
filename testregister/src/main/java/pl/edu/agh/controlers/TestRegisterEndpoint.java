package pl.edu.agh.controlers;

import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.logger.TLMLogger;

import java.io.IOException;

/**
 * Created by Przemek on 28.10.2016.
 */
@RestController
public class TestRegisterEndpoint {
    private static TLMLogger logger = TLMLogger.getLogger(TestRegisterEndpoint.class.getName());

    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)
    public String registerTest(String testClass){
        System.out.println(testClass);
        return "{\"added\": false}";
    }

    @RequestMapping(value = "/isTestInDb", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)
    public String isTestInDB(String testFileId) {
        return String.format("{\"test\": \"%s\", \"isInDB\": false}", testFileId);
    }

    @RequestMapping(value = "/addTestExecutionStamp", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)
    public String addStamp(String testFileId, String user, String timestamp, String isSuccess) {
        System.out.println(testFileId + ", " + user + ", " + timestamp + ", " + isSuccess);
        return "{\"added\": false}";
    }
}
