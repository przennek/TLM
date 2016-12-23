package pl.edu.agh


import org.apache.log4j.BasicConfigurator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.{ComponentScan, Configuration}
import pl.edu.agh.messaging.Receiver

/**
  * Created by Przemek on 04.12.2016.
  */
object LogService extends App {
  SpringApplication.run(classOf[LogService])
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
  for (level <- log4jLevels) { receiver.register("log-exchange", x => println(x), s"log.$level") }
}