package pl.edu.agh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
  * Created by Przemek on 04.12.2016.
  */
object MainApp extends App {
  SpringApplication.run(classOf[MainApp])
}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class MainApp () {
  val log4jLevels: Array[String] = Array ("DEBUG", "ERROR", "FATAL", "INFO", "TRACE", "WARN")
//  for (level <- log4jLevels) { receiver.register("log-exchange", x => println(x), s"log.$level") }
}