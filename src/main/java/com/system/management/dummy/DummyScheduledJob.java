package com.system.management.dummy;

import com.system.management.job.ScheduledJob;

public class DummyScheduledJob extends ScheduledJob {

  public DummyScheduledJob() {
    super(DummyDataHelper.generateRandomName(), DummyDataHelper.randomDateOffsetInSec(),
        DummyDataHelper.generateRandomPriority());
  }
}
