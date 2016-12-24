package pl.edu.agh.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import pl.edu.agh.configuration.filters.AuthFilter

@EnableWebSecurity
@Configuration class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @throws[Exception]
  override def configure(web: WebSecurity) {
    web.ignoring.antMatchers("/js/**", "/css/**")
  }

  @throws[Exception]
  override protected def configure(http: HttpSecurity) {
    http.csrf.disable
      .addFilterBefore(new AuthFilter, classOf[BasicAuthenticationFilter])
      .authorizeRequests.anyRequest.permitAll
  }
}
