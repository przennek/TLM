package pl.edu.agh.logger

import pl.edu.agh.messaging.Sender

/**
  * Created by Przemek on 04.12.2016.
  */
trait MessageBrokerLogging {
  def applyMSG(level: String, msg: String, stack_trace: java.lang.Throwable): Unit
  def info(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("INFO", msg, stack_trace)
  def warn(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("WARN", msg, stack_trace)
  def debug(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("DEBUG", msg, stack_trace)
  def trace(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("TRACE", msg, stack_trace)
  def fatal(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("FATAL", msg, stack_trace)
  def error(msg: String, stack_trace: java.lang.Throwable = null) = applyMSG("ERROR", msg, stack_trace)
}

class TLMLogger (val tyPe: String, val sender: Sender, val exchange_name: String) extends MessageBrokerLogging {
  val logger = org.apache.log4j.Logger.getLogger(tyPe)
  override def applyMSG(level: String, msg: String, stack_trace: Throwable): Unit = {
    level match {
      case "INFO" => logger.info(msg, stack_trace);
      case "WARN" => logger.warn(msg, stack_trace);
      case "DEBUG" => logger.debug(msg, stack_trace);
      case "TRACE" => logger.trace(msg, stack_trace);
      case "FATAL" => logger.fatal(msg, stack_trace);
      case "ERROR" => logger.error(msg, stack_trace);
    }

    try {
      val stack_message = stack_trace.getMessage
      val json: String = String.format("{class: \"%s\", level: \"%s\", msg: \"%s\", stack_message: \"%s\" }",
                                                                                      tyPe, level, msg, stack_message)
      sender.sendOverTopic(exchange_name, s"log.$level", json)
    } catch {
      case e: NullPointerException =>
        sender.sendOverTopic(exchange_name, s"log.$level", String.format("{class: \"%s\", level: \"%s\", msg: \"%s\"}", tyPe, level, msg))
    }
  }
}

object TLMLogger {
  val sender: Sender = new Sender("localhost")
  def getLogger(c: String): TLMLogger = {
    new TLMLogger(c, sender, "log-exchange")
  }
}