package pl.edu.agh

import org.apache.log4j.BasicConfigurator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.{ComponentScan, Configuration}
import pl.edu.agh.logger.TLMLogger
import pl.edu.agh.messaging.{Receiver, Sender}
import pl.edu.agh.sessionmanager.SessionManager

/**
  * Created by Przemek on 04.12.2016.
  */
object FrontEndService extends App {
  SpringApplication.run(classOf[FrontEndService])

}
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
class FrontEndService () {
  private val logger: TLMLogger = TLMLogger.getLogger(classOf[FrontEndService].getName)

  BasicConfigurator.configure()
  logger.info("Test register service is running.", null)
  val receiver: Receiver = new Receiver("localhost")
  receiver.register("auth-exchange", SessionManager.addId, "auth.token.broadcast.login")
  receiver.register("auth-exchange", SessionManager.deleteId, "auth.token.broadcast.logout")
}