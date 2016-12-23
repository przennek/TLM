package pl.edu.agh.configuration.filters

import pl.edu.agh.models.User
import pl.edu.agh.sessionmanager.SessionManager
import javax.servlet._
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.util.Arrays
import java.util.HashMap
import java.util.Map

/**
  * Created by Kamil on 02.12.2016.
  */
class AuthFilter extends Filter {
  def destroy() {
    // Do nothing
  }

  @throws[IOException]
  @throws[ServletException]
  def doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
    val request: HttpServletRequest = req.asInstanceOf[HttpServletRequest]
    var cookie: String = request.getHeader("cookie")
    if (cookie == null) cookie = ""
    val headers: util.Map[String, String] = new util.HashMap[String, String]
    Arrays.stream(cookie.split(";")).forEach(el -> {
      String[] t = el.contains("=") ? el.split("="): el.split ":"
      headers.put(t[ 0].trim(), t[ 1].trim())
    })
    val sessionId: String = headers.get("auth-token")
    val user: User = SessionManager.sessionIds.get(sessionId)
    if (user != null) {
      request.getSession.setAttribute("ROLE", user.role)
      request.getSession.setAttribute("USERNAME", user.login)
      chain.doFilter(req, res)
    }
    else res.asInstanceOf[HttpServletResponse].setStatus(401)
  }

  @throws[ServletException]
  def init(arg0: FilterConfig) {
    // Do nothing
  }
}