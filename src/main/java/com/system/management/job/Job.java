package com.system.management.job;

import com.system.management.exception.JobFailedException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

public abstract class Job implements Callable<Integer> {

  private final int MAX_RANDOM_SLEEP_TIME = 10000;
  protected String name;
  protected int priority;
  protected JobState state;
  protected Date startedAt;
  protected Date finishedAt;

  public Job(String name, int priority) {
    this.name = name;
    this.priority = priority;
  }

  @Override
  public Integer call() throws JobFailedException {
    System.out.printf("%s started\n", this.name);
    try {
      doSomething();
    } catch (InterruptedException | JobFailedException e) {
      System.out.printf("%s has failed\n", this.name);
      throw new JobFailedException();
    }
    System.out.printf("%s has completed with success\n", this.name);
    return 0;
  }

  private void doSomething() throws InterruptedException, JobFailedException {
    var rand = new Random();
    Thread.sleep(rand.nextInt(MAX_RANDOM_SLEEP_TIME));
    var randomNumber = rand.nextInt(10);
    if (randomNumber >= 9) {
      throw new JobFailedException();
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public JobState getState() {
    return state;
  }

  public void setState(JobState state) {
    this.state = state;
  }

  public Date getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Date startedAt) {
    this.startedAt = startedAt;
  }

  public Date getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(Date finishedAt) {
    this.finishedAt = finishedAt;
  }
}
