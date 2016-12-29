package pl.edu.agh.globals.listenerclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import pl.edu.agh.annotated.model.FileEntry;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.globals.GlobalListener;
import pl.edu.agh.logger.TLMLogger;
import pl.edu.agh.model.ws.TestClass;
import pl.edu.agh.util.FileHelper;
import pl.edu.agh.util.ListenerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Przemek on 18.12.2016.
 */
public class TreeBuilder extends GlobalListener {
    private static TLMLogger log = TLMLogger.getLogger(TreeBuilder.class.getName());

    public TreeBuilder() {
        super(98);
    }

    @Override
    public void before(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        try {
            String moduleName  =  FileHelper.get().getTlmProperties().getProperty("moduleName");
            registerTree(moduleName, crawl(FileHelper.get().getTestPackagePath(), true));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String crawl(String path, Boolean tokenActivated) {
        File inputFolder = Paths.get(path).toFile();
        FileEntry parentNode = new FileEntry(inputFolder);
        return "["+walk(parentNode, tokenActivated)+"]";
    }

    public String walk(FileEntry parentNode, Boolean tokenActivated) {
        if (parentNode.directory()) {
            File childNodes[] = parentNode.listFiles();
            for (File childNode : childNodes) {
                FileEntry fileEntry = new FileEntry(childNode);
                try {
                    if(!fileEntry.directory()) {
                        if ("".equals(FileHelper.get().getToken(fileEntry.file().toPath())) || !tokenActivated) {
                            parentNode.addSuccessor(fileEntry);
                        }
                    } else {
                        parentNode.addSuccessor(fileEntry);
                    }
                } catch (TokenCouldNotBeParsedException e) {
                    // consume
                }
                walk(fileEntry, tokenActivated);
            }
        }
        return parentNode.toString();
    }

    @Override
    public void after(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // do nothing

    }

    private Boolean registerTree(String moduleName, String jsonData) {
        ObjectMapper mapper = new ObjectMapper();

        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            HttpPost postRequest = ListenerHelper.prepareRequest("addTestTree");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("moduleName", moduleName));
            nvps.add(new BasicNameValuePair("testTree", jsonData));
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));

            HttpResponse response = httpClient.execute(postRequest);

            String json = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent())).readLine();

            return (Boolean) new ObjectMapper().readValue(json, HashMap.class).get("added");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
