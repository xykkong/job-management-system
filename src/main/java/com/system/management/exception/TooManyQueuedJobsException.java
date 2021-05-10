package com.system.management.exception;

public class TooManyQueuedJobsException extends Exception {

  public TooManyQueuedJobsException() {
    super("Error: too many jobs in the queue. Please wait a few minutes before you try again.");
  }
}
