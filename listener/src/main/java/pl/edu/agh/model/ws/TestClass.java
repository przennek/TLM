package pl.edu.agh.model.ws;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Przemek on 26.10.2016.
 */
@Getter
@Setter
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class TestClass {
    private String tokenId;
    private String className;
    private String classComment;
    private List<JavaDocTag> classTags;
    private List<TestMethod> testMethods;

    public List<TestMethod> testMethods() {
        if(testMethods == null) testMethods = new ArrayList<>();
        return testMethods;
    }

    public List<JavaDocTag> classTags() {
        if(classTags == null) classTags = new ArrayList<>();
        return classTags;
    }

    @Override
    public String toString() {
        return this.tokenId;
    }
}
