package pl.edu.agh.util

import java.util
import java.util.Collections

import org.apache.commons.collections.map.HashedMap
import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity
import org.apache.http.{HttpEntity, HttpHost}
import org.elasticsearch.client.RestClient
import org.joda.time.DateTime
import play.api.libs.json._

/**
  * Created by Przemek on 23.12.2016.
  */
class LoggerUtil {

}

object LoggerUtil {

  def appendLog(line: String): Unit = {
    val json = Json.parse(line)
    val className = (json \ "className").as[String]
    val level = (json \ "level").as[String]
    val message = (json \ "msg").as[String]
    val dateStamp = DateTime.now
    println(s"${dateStamp.toDate}  $level --- $className : $message")
    logInElastic(className, level, message, dateStamp.toDate.toString)
  }

  def logInElastic(className: String, level: String, message: String, date: String): Unit = {
    val restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build()

    val entry = "{\n\"className\" : \"" + className+
      "\",\n\"date\" : \"" +date+
      "\",\n\"level\" : \""+level+
      "\",\n\"message\" : \""+message+ "\"\n}"

    println(entry)

    val entity = new NStringEntity(entry, ContentType.APPLICATION_JSON)
    val map = Collections.emptyMap[String, String]
    val indexResponse = restClient.performRequest(
      "POST", s"/log/$level", map, entity)

    restClient.close()
  }
}