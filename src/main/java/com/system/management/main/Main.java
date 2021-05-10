package com.system.management.main;

import com.system.management.dummy.DummyJobProducer;
import com.system.management.jobmanagementsystem.JobManagementSystem;

public class Main {

  private static final int NUMBER_JOBS = 100;

  public static void main(String[] args) {
    var jms = new JobManagementSystem();
    var thread1 = new Thread(jms);
    thread1.start();

    var thread2 = new Thread(new DummyJobProducer(jms, NUMBER_JOBS));
    thread2.start();
  }
}
