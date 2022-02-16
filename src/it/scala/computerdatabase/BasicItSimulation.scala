package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.{Level, LoggerContext}

class BasicItSimulation extends Simulation {
  val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  // Log all HTTP requests -> TRACE

  context.getLogger("io.gatling.core").setLevel(Level.valueOf("WARN"))
  context.getLogger("io.gatling.http").setLevel(Level.valueOf("WARN"))
  context.getLogger("io.gatling.http.engine.response").setLevel(Level.valueOf("WARN"))

  // Log failed HTTP requests
  //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

  val httpProtocol = http
    .baseUrl("http://localhost:8080") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

    val scn = scenario("Scenario: Pages with different Widget Counts") // A scenario is a chain of requests and pauses
      .exec(http("home") //1st request can be slow, as cold start
        .get("/"))
      .pause(10)

      .exec(http("1 widget")
        .get("/p/1_widget?profile=Responsive"))
      .pause(10)

      .exec(http("4 widgets")
        .get("/p/4_widgets?profile=Responsive"))
      .pause(10)

      .exec(http("4 widgets - diff sorting")
        .get("/p/4_widgets_diff_sorting?profile=Responsive"))
      .pause(10)

      .exec(http("16 widgets")
        .get("/p/16_widgets?profile=Responsive"))
      .pause(10)

      .exec(http("32 widgets")
        .get("/p/32_widgets?profile=Responsive"))
      .pause(10)

      .exec(http("32 widgets - 2")
        .get("/p/32_widgets?profile=Responsive"))
      .pause(10)

      .exec(http("64 widgets (more than 32 anyway)")
        .get("/p/64_widgets?profile=Responsive"))
      .pause(10)

    setUp(scn.inject(atOnceUsers(100)).protocols(httpProtocol))
  }
