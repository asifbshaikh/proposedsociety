import sbt._
import Keys._
import play.Project._
import java.io.File

object ApplicationBuild extends Build {

  val appName         = "ProposedSociety"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.json" % "json" % "20090211",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    "mysql" % "mysql-connector-java" % "5.1.25",
    "org.mindrot" % "jbcrypt" % "0.3m",
    "com.google.code.gson" % "gson" % "2.2.4",
    "com.google.inject" % "guice" % "3.0",
    "org.apache.httpcomponents" % "httpclient" % "4.2.5",
    "org.apache.commons" % "commons-io" % "1.3.2",
    "org.apache.commons" % "commons-lang3" % "3.1",

    // Libraries installed in local repository for CCAvenue Payment Gateway.
    "com.ccavenue.security" % "ccavutil" % "1.0",
    
    //Quartz framework jars (Quartz 2.2.1 full distribution)
    "org.quartz-scheduler" % "quartz" % "2.2.1",
    "org.quartz-scheduler" % "quartz-jobs" % "2.2.1",
    "net.sf.opencsv" % "opencsv" % "2.3",
    
    //Apache HttpAsyncClient
    "org.apache.httpcomponents" % "httpasyncclient" % "4.0.1",
    
    // BIRT runtime 
    ("org.eclipse.birt.runtime" % "org.eclipse.birt.runtime" % "4.2.0").exclude("org.milyn", "flute"),
    //json node
    "org.json"%"org.json"%"chargebee-1.0",
    
    //Apache common mail 
    "org.apache.commons" % "commons-email" % "1.3.3"	
  )
  
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers += "Third Party Jars" at "file:///" + new File("repository").getAbsolutePath()
  )

}
