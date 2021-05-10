package com.system.management.job;

import com.system.management.helper.DateHelper;
import java.util.Comparator;

public class JobComparator implements Comparator<Job> {

  @Override
  public int compare(Job o1, Job o2) {
    if (o1 instanceof ImmediateJob job1 && o2 instanceof ImmediateJob job2) {
      return normalizeResponse(job1.priority - job2.priority);
    } else if (o1 instanceof ImmediateJob job1 && o2 instanceof ScheduledJob job2) {
      if (DateHelper.isOverdue(job2.getScheduleDate())) {
        return normalizeResponse(job1.priority - job2.priority);
      } else {
        return -1;
      }
    } else if (o1 instanceof ScheduledJob job1 && o2 instanceof ImmediateJob job2) {
      if (DateHelper.isOverdue(job1.getScheduleDate())) {
        return normalizeResponse(job1.priority - job2.priority);
      } else {
        return 1;
      }
    } else if (o1 instanceof ScheduledJob job1 && o2 instanceof ScheduledJob job2) {
      var job1IsOverdue = DateHelper.isOverdue(job1.getScheduleDate());
      var job2IsOverdue = DateHelper.isOverdue(job2.getScheduleDate());
      if (job1IsOverdue && job2IsOverdue) {
        return normalizeResponse(job1.priority - job2.priority);
      } else if (job1IsOverdue) {
        return -1;
      } else if (job2IsOverdue) {
        return 1;
      } else {
        int diff = job1.getScheduleDate().compareTo(job2.getScheduleDate());
        if (diff == 0) {
          return normalizeResponse(job1.priority - job2.priority);
        } else {
          return normalizeResponse(diff);
        }
      }
    }
    return 0;
  }

  private int normalizeResponse(int diff) {
    if (diff == 0) {
      return 0;
    } else {
      return (diff > 0) ? 1 : -1;
    }
  }
}
