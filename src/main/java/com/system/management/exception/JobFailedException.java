package com.system.management.exception;

public class JobFailedException extends Exception {

  public JobFailedException() {
    super("Error: job failed");
  }
}
