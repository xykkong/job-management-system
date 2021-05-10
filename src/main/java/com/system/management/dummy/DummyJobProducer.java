package com.system.management.dummy;

import com.system.management.exception.TooManyQueuedJobsException;
import com.system.management.job.Job;
import com.system.management.jobmanagementsystem.JobManagementSystem;
import java.util.Date;
import java.util.Random;

public class DummyJobProducer implements Runnable {

  private final int numberOfJobs;
  private final JobManagementSystem jms;

  public DummyJobProducer(JobManagementSystem jms, int numberOfJobs) {
    this.jms = jms;
    this.numberOfJobs = numberOfJobs;
  }

  @Override
  public void run() {
    for (int i = 0; i < numberOfJobs; i++) {
      Job job = generateRandomJob();
      try {
        jms.addJob(job);
        System.out.printf("%s %s has been added in the JMS\n", job.getClass().getSimpleName(),
            job.getName());
        Thread.sleep(new Random().nextInt(1000));
      } catch (TooManyQueuedJobsException | InterruptedException e) {
        System.err.println(e.getMessage());
        try {
          Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException interruptedException) {
          System.err.println(e.getMessage());
        }
      }
    }
  }

  private Job generateRandomJob() {
    var rand = new Random().nextInt(10);
    if (rand >= 5) {
      return new DummyImmediateJob();
    }
    DummyScheduledJob job = new DummyScheduledJob();
    job.setScheduleDate(generateRandomDate());
    return job;
  }

  private Date generateRandomDate() {
    long now = new Date().getTime();
    var randomOffset = (new Random().nextInt(30) * 1000);
    return new Date(now + randomOffset);
  }
}
