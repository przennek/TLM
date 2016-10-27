package tools;

import pl.edu.agh.listener.util.FileMarkerHelper;

import java.nio.file.Paths;

/**
 * Created by Przemek on 27.10.2016.
 */
public class MarkCleaner {
    public static void main(String[] args) {
        FileMarkerHelper.deleteMark(Paths.get("src/test/java/pl/edu/agh/listener/util/FileMarkerHelperTest.java"));
        FileMarkerHelper.deleteMark(Paths.get("src/test/java/pl/edu/agh/TlmApplicationTests.java"));
    }
}
