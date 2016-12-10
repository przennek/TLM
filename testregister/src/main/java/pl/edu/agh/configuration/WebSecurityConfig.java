package pl.edu.agh.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import pl.edu.agh.configuration.filters.AuthFilter;

/**
 * Created by Przemek on 23.10.2016.
 */

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthFilter(),  BasicAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
            .csrf().disable();
    }
}
