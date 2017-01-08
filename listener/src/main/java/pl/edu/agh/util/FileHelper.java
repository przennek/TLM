package pl.edu.agh.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.assertj.core.util.VisibleForTesting;
import org.testng.ITestNGMethod;
import pl.edu.agh.annotated.listenerclasses.Collector;
import pl.edu.agh.exceptions.TLMPropertiesNotFoundException;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.logger.TLMLogger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Przemek on 24.10.2016.
 */
public class FileHelper {
    //private static TLMLogger log = TLMLogger.getLogger(FileHelper.class.getName());
    private static FileHelper instance;
    private static final String TLM_TOKEN = "tlm-token";
    private URL PROPERTIES_PATH;

    public static FileHelper get() {
        if(instance == null) instance = new FileHelper();
        return instance;
    }

    private FileHelper() throws TLMPropertiesNotFoundException {
        try {
            if (Files.exists(Paths.get("src/main/resources/tlm.properties"))) {
                PROPERTIES_PATH = new File("src/main/resources/tlm.properties").toURI().toURL();
            } else if (Files.exists(Paths.get("src/main/resources/tlm.properties"))) {
                PROPERTIES_PATH = new File("src/main/resources/tlm.properties").toURI().toURL();
            } else {
                final String message = "Create tlm.properties file under: src/main/resources/ or src/main/resources";
                throw new TLMPropertiesNotFoundException(message);
            }
        } catch (MalformedURLException e) {
           // log.error(e.getMessage(), e);
        }
    }

    public String getTLMPropertiesPath() {
        return '/' == PROPERTIES_PATH.getPath().toCharArray()[0]
                ? PROPERTIES_PATH.getPath().substring(1, PROPERTIES_PATH.getPath().length()) : PROPERTIES_PATH.getPath();
    }

    public String markTestClass(Path path) {
        // TODO verify in db if token is unique
        String uniqueToken = uniqueToken(() -> UUID.randomUUID().toString(), FileHelper::isTokenUsed);
        try {
            Files.write(path, ("\n// " + TLM_TOKEN + ": " + uniqueToken).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //log.error("Error while setting token on file.", e);
        }
        return uniqueToken;
    }

    public void deleteMark(Path path) {
        try {
            File file = path.toFile();
            List<String> lines = FileUtils.readLines(file);
            List<String> updatedLines = lines.stream().filter(s -> !s.contains(("// " + TLM_TOKEN + ": "))).collect(Collectors.toList());
            FileUtils.writeLines(file, updatedLines, false);
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
        }
    }

    public Path preparePath(ITestNGMethod method) throws IOException {
        return Paths.get(getTestPackagePath() + "/" + method.getTestClass().toString().replaceAll("\\.", "/").replaceAll("]", "").split(" ")[2] + ".java");
    }

    public String getTestPackagePath() throws IOException {
        Properties properties = getTlmProperties();
        try {
            return properties.getProperty("testpackagepath").replaceAll("\"", "");
        } catch (NullPointerException npe) {
            throw new TLMPropertiesNotFoundException("testpackagepath property not found! Make sure it's in tlm.properties file!");
        }
    }

    public Properties getTlmProperties() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH.toString().replaceAll("file:/", "/")));
        } catch (IOException e) {
            //log.error("Error while reading properties file, check your TLM configuration!", null);
            //log.error(e.getMessage(), e);
            throw e;
        }
        return properties;
    }

    public String getToken(Path path) throws TokenCouldNotBeParsedException {
        final String[] readToken = new String[1];

        if (!Files.exists(path)) {
            //log.error("Error while reading test file on path: " + path.toString(), null);
            throw new TokenCouldNotBeParsedException();
        }

        try {
            final boolean[] found = {false};
            Files.lines(path).forEach(line -> {
                if (line.contains(TLM_TOKEN)) {
                    found[0] = true;
                    readToken[0] = line;
                }
            });

            if(!found[0]) {
                return "";
            }

            return readToken[0].replaceAll("//", "").replaceAll(TLM_TOKEN + ":", "").trim();
        } catch (IOException e) {
            //log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        }
    }

    public static Boolean isTokenUsed(String token) {
        try (DefaultHttpClient httpClient = new DefaultHttpClient()) {
            HttpPost postRequest = ListenerHelper.prepareRequest("isTokenUsed");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("tokenId", token));
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

    @VisibleForTesting
    protected String uniqueToken(Supplier<String> tokenGenerator, Function<String, Boolean> isUnique) {
        final Integer MAX_RETRIES = 10;
        Integer n = 0;
        String token = tokenGenerator.get();
        while (isUnique.apply(token)) {
            if (n++ > MAX_RETRIES) throw new RuntimeException("Max retries exceeded! Check sanity of your db!");
            token = tokenGenerator.get();
        }
        return token;
    }
}
