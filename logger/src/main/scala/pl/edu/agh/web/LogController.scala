package pl.edu.agh.web

import java.util.NoSuchElementException

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{CrossOrigin, RequestMapping, RestController}
import play.api.libs.json.Json

import scalaj.http.{Http, HttpOptions}


@Controller
@RequestMapping(Array("/logs"))
class LogController() {
  val TIMEOUT: Int = 10000;

  @RestController
  @CrossOrigin(origins = Array("http://localhost:4200"))
  @RequestMapping(Array("/getAllByLevel"))
  def level(level: String): String = {
    try {
      (
        Json.parse(
          Http(s"http://localhost:9200/log/$level/_search?pretty&filter_path=hits.hits._source.*")
            .header("Content-Type", "application/json")
            .header("Charset", "UTF-8")
            .option(HttpOptions.readTimeout(TIMEOUT))
            .asString.body
        ) \ "hits" \ "hits"
        ).get.toString
    } catch {
      case nse: NoSuchElementException => "NOTHING FOUND"
    }
  }

  @RestController
  @CrossOrigin(origins = Array("http://localhost:4200"))
  @RequestMapping(Array("/getContainingMessage"))
  def contains(msg: String): String = {
    try {
      (
        Json.parse(
          Http(s"http://localhost:9200/log/_search?pretty&filter_path=hits.hits._source.*")
            .postData(s"""{"query" : {"match" : { "message" : "$msg" }}}""")
            .header("Content-Type", "application/json")
            .header("Charset", "UTF-8")
            .option(HttpOptions.readTimeout(TIMEOUT))
            .asString.body
        ) \ "hits" \ "hits"
        ).get.toString
    } catch {
      case nse: NoSuchElementException => "NOTHING FOUND"
    }
  }
}