package com.system.management.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ApplicationTest {

  @Test
  public void TEST_VARIABLES_SHOULD_BE_NOT_NULL() {
    assertNotNull(Application.MAX_CONCURRENT_RUNNING_JOBS);
    assertNotNull(Application.MAX_QUEUED_JOBS);
    assertNotNull(Application.MAX_JOB_EXECUTION_TIME_IN_SEC);
  }

  @ParameterizedTest
  @ValueSource(strings = {"app.properties", "application", "application.propertie"}) // six numbers
  public void TEST_READPROPERTIES_RETURN_EMPTY_PROPERTIES(String filename) throws IOException {
    assertEquals(new Properties(), Application.readProperties(filename));
  }

  @Test
  public void TEST_READPROPERTIES_SHOULD_RETURN_OK() throws IOException {
    Properties prop = new Properties();
    prop.setProperty("a", "1");
    prop.setProperty("b", "2");
    assertEquals(prop, Application.readProperties("application.test.properties"));
  }
}
