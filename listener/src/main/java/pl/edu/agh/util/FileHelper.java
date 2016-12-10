package pl.edu.agh.util;

import org.assertj.core.util.VisibleForTesting;
import org.testng.ITestNGMethod;
import pl.edu.agh.LoadListener;
import pl.edu.agh.exceptions.TLMPropertiesNotFoundException;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.logger.TLMLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Przemek on 24.10.2016.
 */
public class FileHelper {
    private static TLMLogger log = TLMLogger.getLogger(FileHelper.class.getName());
    private static FileHelper instance;
    private URL PROPERTIES_PATH;

    public static FileHelper get() {
        if(instance == null) instance = new FileHelper();
        return instance;
    }

    private FileHelper() throws TLMPropertiesNotFoundException {
        try {
            if (Files.exists(Paths.get("src/resources/tlm.properties"))) {
                PROPERTIES_PATH = new File("src/resources/tlm.properties").toURI().toURL();
            } else if (Files.exists(Paths.get("src/main/resources/tlm.properties"))) {
                PROPERTIES_PATH = new File("src/main/resources/tlm.properties").toURI().toURL();
            } else {
                final String message = "Create tlm.properties file under: src/main/resources/ or src/resources";
                throw new TLMPropertiesNotFoundException(message);
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getTLMPropertiesPath() {
        return '/' == PROPERTIES_PATH.getPath().toCharArray()[0]
                ? PROPERTIES_PATH.getPath().substring(1, PROPERTIES_PATH.getPath().length()) : PROPERTIES_PATH.getPath();
    }

    public String markTestClass(Path path) {
        // TODO verify in db if token is unique
        String uniqueToken = uniqueToken(() -> UUID.randomUUID().toString(), String -> true);
        try {
            Files.setAttribute(path, "user:test_class_id", uniqueToken.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Error while setting token on file attribute. OS probably doesn't support such operations.", e);
        }
        return uniqueToken;
    }

    public void deleteMark(Path path) {
        try {
            Files.setAttribute(path, "user:test_class_id", "".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Path preparePath(ITestNGMethod method) throws IOException {
        // read test path from properties
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH.toString().replaceAll("file:/", "/")));
        } catch (IOException e) {
            log.error("Error while reading properties file, check your TLM configuration!", null);
            log.error(e.getMessage(), e);
            throw e;
        }

        // absolute path to test case
        try {
            String packagePath = properties.getProperty("testpackagepath").replaceAll("\"", "");
            return Paths.get(packagePath + "/" + method.getTestClass().toString().replaceAll("\\.", "/").replaceAll("]", "").split(" ")[2] + ".java");
        } catch (NullPointerException npe) {
            throw new TLMPropertiesNotFoundException("testpackagepath property not found! Make sure it's in tlm.properties file!");
        }
    }

    public String getToken(Path path) throws TokenCouldNotBeParsedException {
        byte[] readToken;

        if (!Files.exists(path)) {
            log.error("Error while reading test file on path: " + path.toString(), null);
            throw new TokenCouldNotBeParsedException();
        }

        try {
            readToken = (byte[]) Files.getAttribute(path, "user:test_class_id");
        } catch (NoSuchFileException e) {
            return "";
        } catch (FileSystemException e) {
            return "";
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TokenCouldNotBeParsedException();
        }
        return new String(readToken, StandardCharsets.UTF_8);
    }

    @VisibleForTesting
    protected String uniqueToken(Supplier<String> tokenGenerator, Function<String, Boolean> isUnique) {
        final Integer MAX_RETRIES = 10;
        Integer n = 0;
        String token = tokenGenerator.get();
        while (!isUnique.apply(token)) {
            if (n++ > MAX_RETRIES) throw new RuntimeException("Max retries exceeded! Check sanity of your db!");
            token = tokenGenerator.get();
        }
        return token;
    }
}
