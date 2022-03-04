package com.github.utils4j;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

public interface IDurationProvider {

  Duration getDuration();

  long getDuration(TemporalUnit unit);

}