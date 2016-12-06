package pl.edu.agh.configuration.filters;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.agh.models.User;
import pl.edu.agh.sessionmanager.SessionManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.split;

/**
 * Created by Kamil on 02.12.2016.
 */
public class AuthFilter implements Filter {

    @Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String cookie = request.getHeader("cookie");
        if(cookie == null ) cookie = "";
        Map<String, String> headers = new HashMap<>();
        Arrays.stream(cookie.split(";")).forEach(el -> {
            String[] t = el.split("=");
            headers.put(t[0], t[1]);
         });
        String sessionId = headers.get("JSESSIONID");
        User user = SessionManager.sessionIds.get(sessionId);
        if(user != null) {
            request.getSession().setAttribute("ROLE", user.role());
            request.getSession().setAttribute("USERNAME", user.login());
            chain.doFilter(req, res);
        } else {
            ((HttpServletResponse) res).setStatus(401);
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Do nothing
    }
}