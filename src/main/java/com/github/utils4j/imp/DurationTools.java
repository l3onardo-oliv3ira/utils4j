package com.github.utils4j.imp;

import static com.github.utils4j.imp.Strings.padStart;
import static com.github.utils4j.imp.Strings.toInt;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

import java.time.Duration;
import java.util.Optional;

public class DurationTools {
  
  public static final Duration ONE_HOUR         = Duration.ofHours(1);
  public static final Duration ONE_MINUTE       = Duration.ofMinutes(1);
  public static final Duration ONE_SECOND       = Duration.ofSeconds(1);
  public static final Duration ONE_MILLISECOND  = Duration.ofMillis(1);
  
  private DurationTools() {}
  
  public static String stringNow() {
    return toHmsString(System.currentTimeMillis());
  }

  public static String toString(long timeMillis) {
    return toString(timeMillis, ':', ':');
  }
  
  public static String toHmsString(long timeMillis) {
    return toString(timeMillis, 'h', 'm') + 's';
  }
  
  public static String toString(long timeMillis, char hseparator, char mseparator) {
    Args.requireZeroPositive(timeMillis, "timiMillis < 0");
    long div = timeMillis / ONE_HOUR.toMillis();
    long mod = timeMillis % ONE_HOUR.toMillis();
    if (mod == 0) {
      return padStart(div, 2) + hseparator + "00" + mseparator + "00";
    }
    long hours = div;
    timeMillis -= hours * ONE_HOUR.toMillis();
    div = timeMillis / ONE_MINUTE.toMillis();
    mod = timeMillis % ONE_MINUTE.toMillis();
    if (mod == 0) {
      return padStart(hours, 2) + hseparator + padStart(div, 2) + mseparator + "00";
    }
    long minutes = div;
    timeMillis -= minutes * ONE_MINUTE.toMillis();
    div = timeMillis / ONE_SECOND.toMillis();
    return padStart(hours, 2) + hseparator + padStart(minutes, 2) + mseparator + padStart(div, 2);
  }
  
  public static Optional<Duration> parse(String duration) {
    if (!Strings.hasText(duration))
      return Optional.empty();
    final int length = duration.length();
    int start = 0;
    int next = duration.indexOf(':');
    if (next <= 0)
      return Optional.empty();
    int hour = toInt(duration.substring(start, next), -1);
    if (hour < 0)
      return Optional.empty();
    Duration h = ofHours(hour);
    start = next + 1;
    if (start >= length)
      return Optional.empty();
    next = duration.indexOf(':', start);
    if (next < 0)
      return Optional.empty();
    int minutes = toInt(duration.substring(start, next), -1);
    if (minutes < 0)
      return Optional.empty();
    Duration m = ofMinutes(minutes);
    start = next + 1;
    if (start >= length)
      return Optional.empty();
    int sec = toInt(duration.substring(start), -1);
    if (sec < 0)
      return Optional.empty();
    Duration s = ofSeconds(sec);
    return Optional.of(ofMillis(h.toMillis() + m.toMillis() + s.toMillis()));
  }
}
