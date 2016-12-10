package pl.edu.agh.configuration;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Przemek on 20.11.2016.
 */
@Service
public class MongoDBAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private MongoCollection users;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String rawPassword = authentication.getCredentials().toString();
            pl.edu.agh.model.mongo.User usr = userMapper((Document) users.find(eq("login",username )).first());
            if(!passwordEncoder.matches(rawPassword, usr.password())) throw new AuthenticationServiceException("Wrong password");
            loadedUser = new User(usr.login(), usr.password(), Arrays.asList(new SimpleGrantedAuthority("ROLE_" + usr.role())));
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        return loadedUser;
    }

    private pl.edu.agh.model.mongo.User userMapper(Document user) {
        final String login = user.get("login", String.class);
        final String password = user.get("password", String.class);
        final String role = user.get("role", String.class);
        return new pl.edu.agh.model.mongo.User()
                .login(login)
                .password(password)
                .role(role);
    }
}
