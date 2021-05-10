package com.system.management.job;

import java.util.Date;

public class ScheduledJob extends Job {

  protected Date scheduleDate;

  public ScheduledJob(String name, Date scheduleDate, int priority) {
    super(name, priority);
    this.scheduleDate = scheduleDate;
  }

  public Date getScheduleDate() {
    return scheduleDate;
  }

  public void setScheduleDate(Date scheduleDate) {
    this.scheduleDate = scheduleDate;
  }
}
