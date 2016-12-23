package pl.edu.agh.globals.listenerclasses;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.util.FileHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


/**
 * Created by Przemek on 16.10.2016.
 */

public class TLMConnectionListener extends GlobalListener {
    private static final int BEGIN_OF_COOKIE_VALUE = 23;
    private static final int END_OF_AUTH_TOKEN = 59;
    private static final int END_OF_JSESSION_ID = 55;
    public static String sessionId;
    public static String jSessionId;

    private static TLMLogger log = TLMLogger.getLogger(TLMConnectionListener.class.getName());

    public TLMConnectionListener() {
        super(98);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        try {
            getSessionId();
            globalAuthorisation();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void getSessionId() throws IOException {
        Properties properties = FileHelper.get().getTlmProperties();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest
                = new HttpPost(
                String.format("http://%s:%s/auth/login",
                        properties.getProperty("gate"),
                        properties.getProperty("port")
                )
        );
        postRequest.setEntity(new UrlEncodedFormEntity(getTLMCredentialsAsPostValue(properties)));
        HttpResponse response = httpClient.execute(postRequest);
        sessionId = getValueFromHeaders(response, "Set-Cookie", "auth-token", BEGIN_OF_COOKIE_VALUE, END_OF_AUTH_TOKEN);
        jSessionId = getValueFromHeaders(response, "Set-Cookie", "JSESSIONID", BEGIN_OF_COOKIE_VALUE, END_OF_JSESSION_ID);
        httpClient.getConnectionManager().shutdown();
    }

    private Boolean globalAuthorisation() throws IOException {
        Properties properties = FileHelper.get().getTlmProperties();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest
                = new HttpPost(
                String.format("http://%s:%s/auth/login-success",
                        properties.getProperty("gate"),
                        properties.getProperty("port")
                )
        );
        postRequest.setHeader(new BasicHeader("Cookie",
                "auth-token=" + TLMConnectionListener.sessionId + ";" +
                "JSESSIONID=" + TLMConnectionListener.jSessionId
        ));
        httpClient.execute(postRequest);
        httpClient.getConnectionManager().shutdown();
        return true;
    }

    private List<NameValuePair> getTLMCredentialsAsPostValue(Properties properties) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("login", properties.getProperty("user")));
        nvps.add(new BasicNameValuePair("password", properties.getProperty("pass")));
        return nvps;
    }

    private String getValueFromHeaders(HttpResponse response, String headerName,
                                       String key, Integer subB, Integer subE) {
        return Arrays.stream(response.getHeaders(headerName))
                .filter(x -> x.toString().contains(key))
                .collect(Collectors.toList()).get(0).toString().substring(subB, subE);
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // nothing here
    }

    @Override
    public String toString() {
        return "<TLMConnectionListener>";
    }
}
