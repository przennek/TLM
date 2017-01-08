package pl.edu.agh

import java.util.Optional
import java.util.function.Consumer

import org.apache.log4j.BasicConfigurator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Configuration
import pl.edu.agh.cassandra.services.TestsService
import pl.edu.agh.logger.TLMLogger
import pl.edu.agh.messaging.{Receiver, Sender}
import pl.edu.agh.sessionmanager.SessionManager




object FrontEndService extends App {
  SpringApplication.run(classOf[FrontEndService])
}

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
 class FrontEndService() {
  private val logger: TLMLogger = TLMLogger.getLogger(classOf[FrontEndService].getName)
  BasicConfigurator.configure()
  logger.info("Test register service is running.", null)
  val receiver: Receiver = new Receiver("rabbitmq-service.default.svc.cluster.local")
  val sender: Sender = new Sender("rabbitmq-service.default.svc.cluster.local")
  var addId: Consumer[String] = new Consumer[String] {
    override def accept(t: String): Unit = SessionManager.addId(t)
  }
  var deleteId: Consumer[String] = new Consumer[String] {
    override def accept(t: String): Unit = SessionManager.deleteId(t)
  }
  SessionManager.getAuthTokens(Optional.ofNullable(null), sender, receiver, "frontendservice")
  receiver.register("auth-exchange", addId , "auth.token.broadcast.login")
  receiver.register("auth-exchange", deleteId, "auth.token.broadcast.logout")
  TestsService.findTestTreeByModuleName("init");
}

