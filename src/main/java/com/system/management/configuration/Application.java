package com.system.management.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Application {

  private static final String PROPERTIES_FILE = "application.properties";
  public static int MAX_JOB_EXECUTION_TIME_IN_SEC;
  public static int MAX_CONCURRENT_RUNNING_JOBS;
  public static int MAX_QUEUED_JOBS;

  static {
    try {
      Properties prop = readProperties(PROPERTIES_FILE);
      MAX_JOB_EXECUTION_TIME_IN_SEC = Integer.valueOf(prop.getProperty("maxJobExecutionTimeInSec"));
      MAX_CONCURRENT_RUNNING_JOBS = Integer.valueOf(prop.getProperty("maxConcurrentRunningJob"));
      MAX_QUEUED_JOBS = Integer.valueOf(prop.getProperty("maxQueuedJobs"));
    } catch (IOException e) {
      System.err.printf("Error on loading %s\n", PROPERTIES_FILE);
      System.exit(1);
    }
  }

  static Properties readProperties(String filename) throws IOException {
    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
    Properties prop = new Properties();
    if (is != null) {
      prop.load(is);
    }
    return prop;
  }
}
