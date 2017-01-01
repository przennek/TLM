package pl.edu.agh.annotated.listenerclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.globals.PriorityAwareListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;
import pl.edu.agh.util.ListenerHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Przemek on 23.12.2016.
 */
public class TestExecution extends PriorityAwareListener {
    private static TLMLogger log = TLMLogger.getLogger(TestExecution.class.getName());

    public TestExecution() {
        super(98);
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing here
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        try {
            Path path = FileHelper.get().preparePath(iInvokedMethod.getTestMethod());
            String token = FileHelper.get().getToken(path);
            if (!"".equals(token)) {
                if (FileHelper.isTokenUsed(token)) {
                    if (!logExecution(token, iTestResult.isSuccess())) {
                        log.error("Something went terribly wrong and TLM is unable to log timestamp of this test execution.", null);
                    }
                }
            }
        } catch (TokenCouldNotBeParsedException ignored) {
            // consume
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Boolean logExecution(String token, Boolean success) {
        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            HttpPost postRequest = ListenerHelper.prepareRequest("addTestExecutionStamp");

            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("testFileId", token));
            nvps.add(new BasicNameValuePair("user",
                    FileHelper.get().getTlmProperties().getProperty("user")));
            nvps.add(new BasicNameValuePair("timestamp", Long.toString(new Date().getTime())));
            nvps.add(new BasicNameValuePair("isSuccess", success.toString()));

            postRequest.setEntity(new UrlEncodedFormEntity(nvps));

            HttpResponse response = httpClient.execute(postRequest);
            String json = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent())).readLine();

            return (Boolean) new ObjectMapper().readValue(json, HashMap.class).get("added");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
