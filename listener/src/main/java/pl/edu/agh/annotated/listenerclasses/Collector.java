package pl.edu.agh.annotated.listenerclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.annotated.annotations.TestType;
import pl.edu.agh.exceptions.TestClassDataParseException;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.globals.listenerclasses.TLMConnectionListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.model.ws.TestClass;
import pl.edu.agh.util.FileHelper;
import pl.edu.agh.util.ListenerHelper;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static pl.edu.agh.util.TestClassDataExtractor.extractData;

/**
 * Created by Przemek on 16.10.2016.
 */
public class Collector extends PriorityAwareListener {
    //private static TLMLogger log = TLMLogger.getLogger(Collector.class.getName());

    public Collector() {
        super(99);
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing to do here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        Path path = null;
        try {
            path = FileHelper.get().preparePath(iInvokedMethod.getTestMethod());
        } catch (IOException e) {
           // log.error(e.getMessage(), e);
        }
        if (ListenerHelper.hasAnnotation(iInvokedMethod, TestType.class)) {
            if (iTestResult.isSuccess()) {
                try {
                    String token = FileHelper.get().getToken(path);
                    if ("".equals(token)) {
                        //log.error("Error while reading token from test file, does your OS support TLM?", null);
                        throw new TokenCouldNotBeParsedException();
                    }
                    String moduleName  =  FileHelper.get().getTlmProperties().getProperty("moduleName");

                    TestClass testClass = new TestClass()
                            .tokenId(token)
                            .testType(ListenerHelper.getAnnotationValue(iInvokedMethod, TestType.class))
                            .moduleName(moduleName);

                    try {
                        extractData(testClass, iInvokedMethod, path);
                    } catch (TestClassDataParseException e) {
                       // log.error("Critical error during parsing test data from file: " + path.toString(), e);
                        FileHelper.get().deleteMark(path);
                    }

                    if (!isTestInDB(testClass)) {
                        // register test
                        if (!register(testClass)) {
                            //log.error("Failed to register token: " + token + ", on file: " + path.toString(), null);
                            FileHelper.get().deleteMark(path);
                        }
                    }
                } catch (TokenCouldNotBeParsedException ignored) {
                    // consume
                } catch (IOException e) {
                   // log.error("Configuration provided is incorrect. TLM won't work as expected.", e);
                }
            } else {
                FileHelper.get().deleteMark(path);
            }
        } else {
            FileHelper.get().deleteMark(path);
        }
    }

    private Boolean register(TestClass testClass) {
        ObjectMapper mapper = new ObjectMapper();

        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            HttpPost postRequest = ListenerHelper.prepareRequest("addTest");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("testClass", mapper.writeValueAsString(testClass)));
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));

            HttpResponse response = httpClient.execute(postRequest);

            String json = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent())).readLine();

            return (Boolean) new ObjectMapper().readValue(json, HashMap.class).get("added");
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            return false;
        }
    }

    public static Boolean isTestInDB(TestClass testClass) {
        ObjectMapper mapper = new ObjectMapper();

        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            HttpPost postRequest = ListenerHelper.prepareRequest("isTestInDb");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("testClass",  mapper.writeValueAsString(testClass) ));
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));

            HttpResponse response = httpClient.execute(postRequest);

            String json = new BufferedReader(new InputStreamReader(
                                     response.getEntity().getContent())).readLine();

            return (Boolean) new ObjectMapper().readValue(json, HashMap.class).get("isInDB");
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public String toString() {
        return "<Collector>";
    }
}
