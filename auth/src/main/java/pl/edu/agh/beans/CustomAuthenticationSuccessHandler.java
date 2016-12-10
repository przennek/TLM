package pl.edu.agh.beans;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import pl.edu.agh.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kamil on 04.12.2016.
 */
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
        super();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Cookie cookie = new Cookie("auth-token", Utils.generateAuthToken());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        if(httpServletRequest.getParameter("ref") != null) {
            httpServletResponse.sendRedirect(httpServletRequest.getParameter("ref") + "/login-success");
        }
    }
}
