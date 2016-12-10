package pl.edu.agh

import org.apache.log4j.BasicConfigurator
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}
import pl.edu.agh.messaging.{Receiver, Sender}

/**
  * Created by Przemek on 04.12.2016.
  */
object FrontEndService extends App {
  SpringApplication.run(classOf[FrontEndService])
}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class FrontEndService () {
  BasicConfigurator.configure()
}