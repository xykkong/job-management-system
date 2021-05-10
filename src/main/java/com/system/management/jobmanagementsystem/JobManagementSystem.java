package com.system.management.jobmanagementsystem;

import com.system.management.configuration.Application;
import com.system.management.exception.TooManyQueuedJobsException;
import com.system.management.job.Job;
import com.system.management.scheduler.Scheduler;
import java.util.ArrayList;
import java.util.List;

public class JobManagementSystem implements Runnable {

  private Scheduler scheduler;
  private List<Job> jobs;

  public JobManagementSystem() {
    jobs = new ArrayList<>();
    scheduler = new Scheduler(Application.MAX_CONCURRENT_RUNNING_JOBS, Application.MAX_QUEUED_JOBS);
  }

  @Override
  public void run() {
    while (true) {
      try {
        scheduler.run();
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void addJob(Job job) throws TooManyQueuedJobsException {
    jobs.add(job);
    scheduler.addJob(job);
  }
}
