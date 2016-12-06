package pl.edu.agh.configuration;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.edu.agh.beans.AuthConfig;
import pl.edu.agh.beans.CustomAuthenticationSuccessHandler;
import pl.edu.agh.beans.CustomLogoutSuccessHandler;
import pl.edu.agh.messaging.Sender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                .antMatchers("/logout-success").permitAll()
                .antMatchers("/login-failed").permitAll()
                .anyRequest().authenticated()
            .and()
            //.httpBasic()
                .formLogin()
                .usernameParameter("login")
                .passwordParameter("password")
                .loginPage("/login")
                //.defaultSuccessUrl("/", true)
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            .and()
            .logout().logoutUrl("/logout")
                .invalidateHttpSession(true)
                //.logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler())
            .and()
            .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }



}
