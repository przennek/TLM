package pl.edu.agh.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@Controller
@RequestMapping(Array("/tlm"))
class LogController () {
  @RestController
  @RequestMapping(method = Array(RequestMethod.GET))
  def hello() = {
    "hello world"
  }
}