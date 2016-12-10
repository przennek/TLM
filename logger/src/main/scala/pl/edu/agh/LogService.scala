package pl.edu.agh

/**
  * Created by Przemek on 04.12.2016.
  */
object LogService extends App {
  SpringApplication.run(classOf[LogService])
}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class LogService () {
  BasicConfigurator.configure()
  val log4jLevels: Array[String] = Array ("DEBUG", "ERROR", "FATAL", "INFO", "TRACE", "WARN")
  val receiver: Receiver = new Receiver("localhost")
  for (level <- log4jLevels) { receiver.register("log-exchange", x => println(x), s"log.$level") }
}