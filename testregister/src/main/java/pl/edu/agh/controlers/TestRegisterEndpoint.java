package pl.edu.agh.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.model.ws.TestClass;

import java.io.IOException;

/**
 * Created by Przemek on 28.10.2016.
 */
@RestController
public class TestRegisterEndpoint {
    private static TLMLogger logger = TLMLogger.getLogger(TestRegisterEndpoint.class.getName());

    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)
    public String registerTest(TestClass testClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String testJson = mapper.writeValueAsString(testClass);
            System.out.println(testJson);
            return testJson;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @RequestMapping(value = "/isTestInDb/", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)
    public String isTestInDB(String testFileId) {
        return String.format("{\"test\": \"%s\", \"isInDB\": false}", testFileId);
    }
}
