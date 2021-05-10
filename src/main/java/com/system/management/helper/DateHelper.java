package com.system.management.helper;

import java.util.Date;

public abstract class DateHelper {

  public static long dateDiff(Date date) {
    return now() - date.getTime();
  }

  public static boolean isOverdue(Date date) {
    return dateDiff(date) <= 0;
  }

  static long now() {
    return new Date().getTime();
  }
}
