package pl.edu.agh.model.ws;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created by Przemek on 26.10.2016.
 */
@Getter @Setter @Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class TestClass {
    private String tokenId;
    private String className;
    private List<String> testMethods;

    @Override
    public String toString() {
        return this.tokenId;
    }
}
