package pl.edu.agh.listener.annotated.listenerclasses;

import org.assertj.core.util.VisibleForTesting;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import pl.edu.agh.listener.annotated.annotations.TestType;
import pl.edu.agh.listener.exceptions.TokenCouldNotBeParsedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static pl.edu.agh.listener.util.ListenerHelper.hasAnnotation;

/**
 * Created by Przemek on 16.10.2016.
 */
public class Collector implements IInvokedMethodListener {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Collector.class);
    private final String PROPERTIES_PATH = "src/main/resources/tlm.properties";

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(hasAnnotation(iInvokedMethod, TestType.class)) {
            // class methods
            // iInvokedMethod.getTestMethod().getRealClass().getMethods()

            try {
                String token = getToken(preparePathSuffix(iInvokedMethod.getTestMethod()));
                if(!isTestInDB(token)) {
                    // TODO build ws call
                }
            } catch (TokenCouldNotBeParsedException e) {
                return;
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing to do here
    }

    // TODO fill up
    private Boolean isTestInDB(String token) {
        return false;
    }

    @VisibleForTesting
    protected String getToken(String suffix) throws TokenCouldNotBeParsedException {
        Properties properties = new Properties();
        byte[] readToken;

        // read test path from properties
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH));
        } catch (IOException e) {
            log.error("Error while reading properties file, check your TLM configuration!");
            log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        }

        // absolute path to test case
        String packagePath = properties.getProperty("testpackagepath").replaceAll("\"", "");
        final Path path = Paths.get(packagePath + suffix);

        if (!Files.exists(path)) {
            log.error("Error while reading test file on path: " + path.toString());
            throw new TokenCouldNotBeParsedException();
        }

        // token reading
        try {
            readToken = (byte[]) Files.getAttribute(path, "user:test_class_id");
        } catch (NoSuchFileException e) {
            log.error("Error while reading token from test file, does your OS support TLM?");
            log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        }
        return new String(readToken, StandardCharsets.UTF_8);
    }

    private String preparePathSuffix(ITestNGMethod method) {
        return "/" + method.getRealClass().toString().replaceAll("\\.", "/").split(" ")[1] + ".java";
    }
}
