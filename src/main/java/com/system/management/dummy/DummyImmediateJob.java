package com.system.management.dummy;

import com.system.management.job.ImmediateJob;

public class DummyImmediateJob extends ImmediateJob {

  public DummyImmediateJob() {
    super(DummyDataHelper.generateRandomName(), DummyDataHelper.generateRandomPriority());
  }

}
