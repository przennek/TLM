package pl.edu.agh.configuration;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.edu.agh.beans.AuthConfig;
import pl.edu.agh.messaging.Sender;

/**
 * Created by Przemek on 23.10.2016.
 */

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan("pl.edu.agh.configuration")
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MongoDBAuthenticationProvider authenticationProvider;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .anyRequest().fullyAuthenticated()
            .and()
            .httpBasic()
            //.and()
           // .logout().clearAuthentication(true).deleteCookies().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
            //.and()
            //.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
