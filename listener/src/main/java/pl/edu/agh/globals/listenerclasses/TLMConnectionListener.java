package pl.edu.agh.globals.listenerclasses;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


/**
 * Created by Przemek on 16.10.2016.
 */

public class TLMConnectionListener extends GlobalListener {
    // TODO move this scratch to designated encrypted session file.
    public static String sessionId;

    private static TLMLogger log = TLMLogger.getLogger(TLMConnectionListener.class.getName());

    public TLMConnectionListener() {
        super(98);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        try {
            Properties properties = FileHelper.get().getTlmProperties();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest
                    = new HttpPost(
                            String.format("http://%s:%s/auth/login?ref=http%s%s%s%s%sauth",
                                    properties.getProperty("gate"),
                                    properties.getProperty("port"),
                                    "%3A%2F%2F",
                                    properties.getProperty("gate"),
                                    "%3A",
                                    properties.getProperty("port"),
                                    "%2F"
                            )
            );
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("login", properties.getProperty("user")));
            nvps.add(new BasicNameValuePair("password", properties.getProperty("pass")));
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = httpClient.execute(postRequest);
            sessionId = Arrays.stream(response.getHeaders("Set-Cookie"))
                    .filter(x -> x.toString().contains("auth-token"))
                    .collect(Collectors.toList()).get(0).toString().substring(23, 59);
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    @Override
    public String toString() {
        return "<TLMConnectionListener>";
    }
}
