package pl.edu.agh.listener.annotated.listenerclasses;

import org.assertj.core.util.VisibleForTesting;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import pl.edu.agh.listener.annotated.annotations.TestType;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

//            String token = getToken("/" + iInvokedMethod.getTestMethod()
//                    .getRealClass().toString().replaceAll("\\.", "/").split(" ")[1] + ".java");

            if(!isTestInDB("")) {
                System.out.println("HIT");
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
    protected String getToken(String suffix) {
        Properties properties = new Properties();
        byte[] readToken = null;
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH));
            String packagePath = properties.getProperty("testpackagepath").replaceAll("\"", "");
            readToken = (byte[]) Files.getAttribute(Paths.get(packagePath + suffix), "user:test_class_id");
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
        if(readToken == null) throw new RuntimeException("Error while parsing tlm.properties file! Check your configuration.");
        return new String(readToken, StandardCharsets.UTF_8);
    }
}
