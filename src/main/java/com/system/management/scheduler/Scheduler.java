package com.system.management.scheduler;

import com.system.management.configuration.Application;
import com.system.management.exception.TooManyQueuedJobsException;
import com.system.management.helper.DateHelper;
import com.system.management.job.Job;
import com.system.management.job.JobComparator;
import com.system.management.job.JobState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scheduler implements Runnable {

  protected int maxQueueSize;
  protected int maxRunningJobs;
  private ExecutorService WORKER_THREAD_POOL;
  private Map<JobState, Collection<Job>> jobs = new HashMap<>();
  private Map<Future, Job> results = new HashMap<>();

  public Scheduler(int maxRunningJobs, int maxQueueSize) {
    this.maxQueueSize = maxQueueSize;
    this.maxRunningJobs = maxRunningJobs;
    this.WORKER_THREAD_POOL = Executors.newFixedThreadPool(maxRunningJobs);

    for (JobState state : JobState.values()) {
      if (JobState.QUEUED.equals(state)) {
        jobs.put(state, new PriorityQueue<>(new JobComparator()));
      } else {
        jobs.put(state, new ArrayList<>());
      }
    }
  }

  @Override
  public void run() {
    if (!jobs.get(JobState.QUEUED).isEmpty() && hasAvailableRunningSlots()) {
      Job job = ((PriorityQueue<Job>) jobs.get(JobState.QUEUED)).poll();
      job.setStartedAt(new Date());
      job.setState(JobState.RUNNING);
      jobs.get(JobState.RUNNING).add(job);

      var future = WORKER_THREAD_POOL.submit(job);
      results.put(future, job);
    }

    if (!results.isEmpty()) {
      List<Entry<Future, Job>> jobsToKill = results.entrySet().stream()
          .filter((e) -> isReachedMaxRunningTime(e.getValue())).toList();
      handleJobsToKill(jobsToKill);

      List<Future> jobsCompleted = results.keySet().stream()
          .filter((r) -> r.isDone() || r.isCancelled()).toList();
      handleCompletedJobs(jobsCompleted);
    }

    System.out
        .printf("There are %d queued, %d running, %d failed and %d completed successfully jobs.\n",
            jobs.get(JobState.QUEUED).size(),
            jobs.get(JobState.RUNNING).size(), jobs.get(JobState.FAILED).size(),
            jobs.get(JobState.SUCCESS).size());
  }

  private void handleJobsToKill(List<Entry<Future, Job>> jobsToKill) {
    for (var entry : jobsToKill) {
      Future future = entry.getKey();
      future.cancel(true);

      Job job = entry.getValue();
      job.setFinishedAt(new Date());
      job.setState(JobState.FAILED);
      results.remove(future);
      jobs.get(JobState.RUNNING).remove(job);
      jobs.get(job.getState()).add(job);
      System.out
          .printf("%s has been interrupted. Reason: job exceeded the maxJobExecutionTimeInSec\n",
              job.getName());

    }
  }

  private void handleCompletedJobs(List<Future> jobsCompleted) {
    if (!jobsCompleted.isEmpty()) {
      for (Future future : jobsCompleted) {
        Job job = results.get(future);
        job.setFinishedAt(new Date());
        if (finalizedWithSuccess(future)) {
          job.setState(JobState.SUCCESS);
        } else {
          job.setState(JobState.FAILED);
        }
        results.remove(future);
        jobs.get(JobState.RUNNING).remove(job);
        jobs.get(job.getState()).add(job);
      }
    }
  }


  private boolean finalizedWithSuccess(Future future) {
    try {
      //Check for status code equals to zero
      return future.get().equals(0);
    } catch (InterruptedException | ExecutionException e) {
      return false;
    }
  }

  private boolean isReachedMaxRunningTime(Job job) {
    return DateHelper.dateDiff(job.getStartedAt())
        >= Application.MAX_JOB_EXECUTION_TIME_IN_SEC * 1000;
  }

  private boolean hasAvailableRunningSlots() {
    return jobs.get(JobState.RUNNING).size() < maxRunningJobs;
  }

  public void addJob(Job job) throws TooManyQueuedJobsException {
    if (jobs.get(JobState.QUEUED).size() < maxQueueSize) {
      job.setState(JobState.QUEUED);
      jobs.get(JobState.QUEUED).add(job);
    } else {
      throw new TooManyQueuedJobsException();
    }
  }
}
