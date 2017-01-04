package pl.edu.agh

import java.util.Optional

import org.apache.log4j.BasicConfigurator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.{ComponentScan, Configuration}
import pl.edu.agh.messaging.{Receiver, Sender}
import pl.edu.agh.sessionmanager.SessionManager
import pl.edu.agh.util.LoggerUtil

/**
  * Created by Przemek on 04.12.2016.
  */
object LogService extends App {
  SpringApplication.run(classOf[LogService])
//  LoggerUtil.restClient.close()
}

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableDiscoveryClient
class LogService () {
  BasicConfigurator.configure()
  val log4jLevels: Array[String] = Array ("DEBUG", "ERROR", "FATAL", "INFO", "TRACE", "WARN")
  val receiver: Receiver = new Receiver("localhost")
  val sender: Sender = new Sender("localhost")

  for (level <- log4jLevels) {
    receiver.register("log-exchange", x => LoggerUtil.appendLog(x), s"log.$level")
  }
  SessionManager.getAuthTokens(Optional.ofNullable(null), sender, receiver, "logger")
  receiver.register("auth-exchange", SessionManager.addId, "auth.token.broadcast.login")
  receiver.register("auth-exchange", println, "auth.token.broadcast.login")
  receiver.register("auth-exchange", SessionManager.deleteId, "auth.token.broadcast.logout")
  receiver.register("auth-exchange", println, "auth.token.broadcast.logout")

}