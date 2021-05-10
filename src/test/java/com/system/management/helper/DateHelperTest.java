package com.system.management.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DateHelperTest {

  private Date now = new Date();

  @BeforeEach
  public void beforeEach() {
    now = new Date();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1000, -1000, -1000000, 1000000})
  public void TEST_DATEDIFF_SHOULD_RETURN_OK(int diff) {
    Date startedDate = new Date(now.getTime() - diff);
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(DateHelper::now).thenReturn(now.getTime());
      assertEquals(diff, DateHelper.dateDiff(startedDate));
    }
  }

  @ParameterizedTest
  @ValueSource(longs = {0, -1, -1000, -1000000})
  public void TEST_ISOVERDUE_SHOULD_RETURN_TRUE(long value) {
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(value);
      assertTrue(DateHelper.isOverdue(now));
    }
  }

  @ParameterizedTest
  @ValueSource(longs = {1, 1000, 1000000})
  public void TEST_ISOVERDUE_SHOULD_RETURN_FALSE(long value) {
    try (MockedStatic<DateHelper> mockedDataHelper = Mockito
        .mockStatic(DateHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedDataHelper.when(() -> DateHelper.dateDiff(now)).thenReturn(value);
      assertFalse(DateHelper.isOverdue(now));
    }
  }
}
