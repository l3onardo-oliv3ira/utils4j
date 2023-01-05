/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


package com.github.utils4j.imp;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Dates {
  private Dates() {}
  
  public static final Locale BRAZIL = new Locale("pt", "BR");
  
  public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

  public static final ZoneId UTC_ZONEID = UTC_TIMEZONE.toZoneId();

  public static final ZoneOffset UTC_ZONEOFFSET = ZoneOffset.UTC;

  public static boolean isBetween(LocalTime target, LocalTime begin, LocalTime end) {
    return target.isAfter(begin) && target.isBefore(end);
  }

  public static LocalDate localDate(long date, ZoneId zoneId) {
    return localDateTime(date, zoneId).toLocalDate();
  }

  public static LocalDateTime localDateTime(long date, ZoneId zoneId) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), zoneId);
  }
  
  public static LocalDateTime LocalDateTime(long date) {
    return localDateTime(date, UTC_ZONEID);
  }
  
  public static String stringNow() {
    return format("yyyy-MM-dd_HH'h'mm'm'ss's'S'ms'", new Date());
  }
  
  public static String timeNow() {
    return format("HH'h'mm'm'ss's'S'ms'", new Date());
  }
  
  public static String defaultFormat(Date date) {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", BRAZIL).format(date);
  }

  public static String format(String format, Date date) {
    return new SimpleDateFormat(format, BRAZIL).format(date);
  }  
}
