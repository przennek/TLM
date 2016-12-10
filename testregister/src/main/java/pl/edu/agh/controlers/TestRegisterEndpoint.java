package pl.edu.agh.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.model.ws.TestClass;

import java.io.IOException;

/**
 * Created by Przemek on 28.10.2016.
 */
@Controller
public class TestRegisterEndpoint {
    private static TLMLogger logger = TLMLogger.getLogger(TestRegisterEndpoint.class.getName());

    @RequestMapping(value = "/addtest/{testClass}", method = RequestMethod.POST)
    @ExceptionHandler(IOException.class)

    public String registerTest(@PathVariable TestClass testClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(testClass);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
