package pl.edu.agh.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by Kamil on 04.01.2017.
 */
@Getter
@Setter
@Accessors(fluent = true)
@ToString
public class SessionData {
    public String sessionId;
    public String role;
    public String username;
}
