package pl.edu.agh.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * Created by Kamil on 04.01.2017.
 */
@Getter
@Setter
@Accessors(fluent = true)
@ToString
public class TokenExchange {
    public String sourceServiceName;
    public String targetServiceName;
    public String action;
    public Map<String, User> tokens;
}
