package pl.edu.agh.model.mongo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by Przemek on 23.10.2016.
 */
@Getter @Setter @Accessors(fluent = true)
@ToString
public class User {
    @Id
    public ObjectId id;
    public String login;
    public String password;
    public String role;
}
