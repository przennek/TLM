package pl.edu.agh.web

@Controller
@RequestMapping(Array("/logs"))
class LogController () {
  @RestController
  @RequestMapping(method = Array(RequestMethod.GET))
  def list() = {
    "hello world"
  }
}