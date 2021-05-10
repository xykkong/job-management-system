package com.system.management.dummy;

import java.util.Date;
import java.util.Random;

public abstract class DummyDataHelper {

  public static int generateRandomPriority() {
    return new Random().nextInt(10);
  }

  public static String generateRandomName() {
    return String.format("Job %d", new Random().nextInt(10000));
  }

  public static Date randomDateOffsetInSec() {
    int randomDateOffsetInSec = new Random().nextInt(120) * 1000;
    Date date = new Date(new Date().getTime() + randomDateOffsetInSec);
    return date;
  }
}
