package com.system.management.job;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.system.management.helper.DateHelper;
import java.util.Date;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JobComparatorTest {

  private JobComparator jobcomparator = new JobComparator();

  @ParameterizedTest
  @CsvSource({"1,2", "1,5", "1,10"})
  public void TEST_COMPARETO_IMMEDIATEJOB_SHOULD_RETURN_1_AND_MINUS_1(int p1, int p2) {
    Job job1 = new ImmediateJob("Job 1", p1);
    Job job2 = new ImmediateJob("Job 2", p2);
    assertEquals(-1, jobcomparator.compare(job1, job2));
    assertEquals(1, jobcomparator.compare(job2, job1));
  }

  @ParameterizedTest
  @CsvSource({"1,1", "1,1", "1,1"})
  public void TEST_COMPARETO_IMMEDIATEJOB_SHOULD_RETURN_0(int p1, int p2) {
    Job job1 = new ImmediateJob("Job 1", p1);
    Job job2 = new ImmediateJob("Job 2", p2);
    assertEquals(0, jobcomparator.compare(job1, job2));
  }

  @ParameterizedTest
  @CsvSource({"1,2", "4,5", "1,10"})
  public void TEST_COMPARETO_IMMEDIATEJOB_AND_OVERDUEJOB_SHOULD_RETURN_HIGHER_PRIORITY(int p1,
      int p2) {
    Date now = new Date();
    Job overdueJob = new ScheduledJob("Job 1", now, p1);
    Job immediateJob = new ImmediateJob("Job 2", p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(0l);
      assertEquals(-1, jobcomparator.compare(overdueJob, immediateJob));
      assertEquals(1, jobcomparator.compare(immediateJob, overdueJob));
    }
  }

  @ParameterizedTest
  @CsvSource({"1,1", "2,2", "10,10"})
  public void TEST_COMPARETO_IMMEDIATEJOB_AND_OVERDUEJOB_AND_SAME_PRIORITY_SHOULD_RETURN_0(int p1,
      int p2) {
    Date now = new Date();
    Job overdueJob = new ScheduledJob("Job 1", now, p1);
    Job immediateJob = new ImmediateJob("Job 2", p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(0l);
      assertEquals(0, jobcomparator.compare(overdueJob, immediateJob));
      assertEquals(0, jobcomparator.compare(immediateJob, overdueJob));
    }
  }

  @ParameterizedTest
  @CsvSource({"1,2", "2,1", "4,4"})
  public void TEST_COMPARETO_IMMEDIATEJOB_AND_SCHEDULEDJOB_SHOULD_RETURN_IMMEDIATE_JOB(int p1,
      int p2) {
    Date date1 = new Date(new Date().getTime() + 1000);
    Job scheduledJob = new ScheduledJob("Job 1", date1, p1);
    Job immediateJob = new ImmediateJob("Job 2", p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(date1)).thenReturn(1000l);
      assertEquals(1, jobcomparator.compare(scheduledJob, immediateJob));
      assertEquals(-1, jobcomparator.compare(immediateJob, scheduledJob));
    }
  }

  @ParameterizedTest
  @CsvSource({"1,2", "4,5", "1,10"})
  public void TEST_COMPARETO_SCHEDULEDJOB_SAME_DATE_SHOULD_HIGHER_PRIORITY(int p1, int p2) {
    Date now = new Date();
    Job job1 = new ScheduledJob("Job 1", now, p1);
    Job job2 = new ScheduledJob("Job 2", now, p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(0l);
      assertEquals(-1, jobcomparator.compare(job1, job2));
      assertEquals(1, jobcomparator.compare(job2, job1));
    }

  }

  @ParameterizedTest
  @CsvSource({"1,1", "4,4", "10,10"})
  public void TEST_COMPARETO_SCHEDULEDJOB_SAME_DATE_SHOULD_RETURN_0(int p1, int p2) {
    Date now = new Date();
    Job job1 = new ScheduledJob("Job 1", now, p1);
    Job job2 = new ScheduledJob("Job 2", now, p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(0l);
      assertEquals(0, jobcomparator.compare(job1, job2));
    }
  }

  @ParameterizedTest
  @CsvSource({"1,2", "4,4", "2,1"})
  public void TEST_COMPARETO_OVERDUEJOB_SCHEDULEDJOB_SHOULD_RETURN_OVERDUEJOB(int p1, int p2) {
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 1000);
    Job job1 = new ScheduledJob("Job 1", date1, p1);
    Job job2 = new ScheduledJob("Job 2", date2, p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(date1)).thenReturn(0l);
      mockedDataHelper.when(() -> DateHelper.dateDiff(date2)).thenReturn(1000l);
      assertEquals(-1, jobcomparator.compare(job1, job2));
      assertEquals(1, jobcomparator.compare(job2, job1));
    }
  }

  @ParameterizedTest
  @CsvSource({"1,2", "4,4", "2,1"})
  public void TEST_COMPARETO_SCHEDULEDJOB_SHOULD_RETURN_FIRST_SCHEDULEJOB(int p1, int p2) {
    Date now = new Date();
    Date date1 = new Date(now.getTime() + 1000);
    Date date2 = new Date(now.getTime() + 2000);
    Job job1 = new ScheduledJob("Job 1", date1, p1);
    Job job2 = new ScheduledJob("Job 2", date2, p2);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(date1)).thenReturn(1000l);
      mockedDataHelper.when(() -> DateHelper.dateDiff(date2)).thenReturn(2000l);
      assertEquals(-1, jobcomparator.compare(job1, job2));
      assertEquals(1, jobcomparator.compare(job2, job1));
    }
  }
}
