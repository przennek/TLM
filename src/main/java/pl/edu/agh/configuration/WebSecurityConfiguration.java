package pl.edu.agh.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.agh.model.mongo.UserRepository;

/**
 * Created by Przemek on 23.10.2016.
 */
@Configuration
@ComponentScan(basePackages = {"pl.edu.agh.model"})
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    private UserRepository repository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            pl.edu.agh.model.mongo.User user = repository.findByLogin(username);
            if(repository.findByLogin(username) != null) {
                return new User(user.login(), user.password(), true, true, true, true, AuthorityUtils.createAuthorityList(user.role()));
            } else {
                throw new UsernameNotFoundException("Could not find the user '" + username + "'");
            }
        };
    }
}


