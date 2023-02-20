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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.utils4j.imp.function.IProvider;

public final class Strings {
  
  private static final char SPACE = ' ';

  private static final String EMPTY = "";

  private static final String HTML_SPACE = "%20";

  private static final String SPACE_STRING = EMPTY + SPACE;

  private static final String[] EMPTY_ARRAY = new String[0];

  private static final String[] HTML_SPACE_ARRAY = new String[] {HTML_SPACE};

  private static final String[] SPACE_STRING_ARRAY = new String[] {SPACE_STRING};

  private static final Optional<String> TRUE = Optional.of("true");
  
  private static final Optional<String> FALSE = Optional.of("false");
  
  private Strings() {
  }

  public static <T> Optional<T> get(IProvider<T> p, T defaultIfFail) {
    try {
      return Optional.ofNullable(p.get());
    }catch (Exception e){
      return Optional.ofNullable(defaultIfFail);
    }
  }
  
  public static Optional<String> optional(String text) {
    return Optional.ofNullable(textOrNull(text));
  }

  public static boolean isEmpty(String text) {
    return text == null || text.isEmpty();
  }

  public static boolean isEmpty(String[] text) {
    return text == null || text.length == 0;
  }

  public static boolean isNotEmpty(String[] text) {
    return !isEmpty(text);
  }

  public static String[] emptyArray() {
    return EMPTY_ARRAY;
  }

  public static String empty() {
    return EMPTY;
  }

  public static String space() {
    return SPACE_STRING;
  }
  
  public static Optional<String> trueOptional() {
    return TRUE;
  }
  
  public static Optional<String> falseOptional() {
    return FALSE;
  }

  public static Iterator<String> emptyIterator() {
    return Collections.emptyIterator();
  }

  public static String text(String text) {
    return text(text, EMPTY);
  }

  public static String text(Object o) {
    return o == null ? EMPTY : text(o.toString());
  }

  public static String textOrNull(String text) {
    return needText(text, null);
  }

  public static String textOrNull(Object t) {
    return t == null ? null : textOrNull(t.toString());
  }

  public static String needText(String text, String defaultIfNull) {
    return !hasText(text) ? defaultIfNull : trim(text);
  }

  public static String text(String text, String defaultIfNull) {
    return text == null ? defaultIfNull : text;
  }

  public static String trim(String text) {
    return trim(text, EMPTY);
  }

  public static String trim(String text, String defaultIfNull) {
    return text == null ? defaultIfNull : text.trim();
  }

  public static String trim(Object o) {
    return o == null ? EMPTY : trim(o.toString());
  }

  public static int length(String text) {
    return text == null ? 0 : text.length();
  }

  public static boolean isInt(String value) {
    try {
      Integer.parseInt(value.trim());
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  public static boolean isLong(String value) {
    try {
      Long.parseLong(value);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  public static boolean isBoolean(String value) {
    try {
      Boolean.parseBoolean(value);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  public static boolean isFloat(String value) {
    try {
      Float.parseFloat(value);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  public static boolean isDouble(String value) {
    try {
      Double.parseDouble(value);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }
  
  public static int toInt(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static long toLong(String value, long defaultValue) {
    try {
      return Long.parseLong(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static boolean toBoolean(String value, boolean defaultValue) {
    try {
      return Boolean.parseBoolean(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static float toFloat(String value, float defaultValue) {
    try {
      return Float.parseFloat(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static double toDouble(String value, double defaultValue) {
    try {
      return Double.parseDouble(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static List<String> mix(Iterable<Iterable<String>> text, char separator) {
    List<String> r = new ArrayList<>();
    for (Iterable<String> it : text) {
      r.add(merge(it, separator));
    }
    return r;
  }

  public static String merge(Iterable<String> it, char separator) {
    StringBuilder member = new StringBuilder();
    for (String m : it) {
      if (member.length() > 0)
        member.append(separator);
      member.append(m);
    }
    return member.toString();
  }

  public static String replace(String value, char find, char replace) {
    StringBuilder s = new StringBuilder(value.length());
    for (int i = 0; i < value.length(); i++) {
      char chr = value.charAt(i);
      s.append(chr == find ? replace : chr);
    }
    return s.toString();
  }
  
  public static String quotes(String value) {
    return value == null ? empty() : "\"" + value + "\"";
  }

  public static String quotes(String separator, String... value) {
    if (value == null)
      return empty();    
    StringBuilder b = new StringBuilder();
    Arrays.stream(value).map(Strings::quotes).forEach(p -> b.append(p).append(separator));
    return b.toString().trim();
  }
  
  public static StringBuilder computeTabs(int tabSize) {
    StringBuilder b = new StringBuilder(6);
    while(tabSize-- > 0)
      b.append("  ");
    return b;
  }

  public static boolean hasText(String text) {
    if (text == null)
      return false;
    int size = text.length();
    for (int i = 0; i < size; i++)
      switch (text.charAt(i)) {
      case ' ':
      case '\t':
      case '\n':
      case '\r':
        continue;
      default:
        return true;
      }
    return false;
  }

  public static List<String> split(String text, char separator) {
    List<String> r = new ArrayList<>();
    int begin = 0, end = begin + 1;
    text = text(text);
    while (end <= text.length())
      if (text.charAt(end - 1) == separator) {
        if (end - begin > 1) {
          String member = text.substring(begin, end - 1);
          if (hasText(member))
            r.add(member.trim());
        }
        begin = end++;
      } else {
        ++end;
      }
    if (begin < text.length()) {
      String member = text.substring(begin, text.length());
      if (hasText(member))
        r.add(member.trim());
    }
    return r;
  }

  public static String padStart(long in, int minLength) {
    return padStart(Long.toString(in), minLength, '0');
  }

  public static String padStart(String input, int minLength, char padChar) {
    final int length;
    if ((length = input.length()) >= minLength) {
      return input;
    }
    StringBuilder sb = new StringBuilder(minLength);
    for(int i = length; i < minLength; i++) {
      sb.append(padChar);
    }
    sb.append(input);
    return sb.toString();
  }

  public static String padEnd(String input, int minLength, char padChar) {
    final int length;
    if ((length = input.length()) >= minLength) {
      return input;
    }
    StringBuilder sb = new StringBuilder(minLength);
    sb.append(input);
    for (int i = length; i < minLength; i++) {
      sb.append(padChar);
    }
    return sb.toString();
  }

  public static Set<String> stringSet(Collection<?> container) {
    Set<String> out = new HashSet<>(container.size());
    container.forEach(o -> out.add(o.toString()));
    return out;
  }

  public static List<String> stringList(Collection<?> container) {
    List<String> out = new ArrayList<>(container.size());
    container.forEach(o -> out.add(o.toString()));
    return out;
  }

  public static String firstCase(String input) {
    input = trim(input);
    StringBuilder out = new StringBuilder();
    boolean cs = true;
    for (int i = 0; i < input.length(); i++) {
      char chr = input.charAt(i);
      out.append(cs ? Character.toUpperCase(chr) : Character.toLowerCase(chr));
      cs = chr == ' ';
    }
    return out.toString();
  }

  public static String firstChars(String input) {
    input = trim(input);
    StringBuilder out = new StringBuilder();
    boolean cs = true;
    for (int i = 0; i < input.length(); i++) {
      char chr = input.charAt(i);
      if (cs) {
        out.append(chr);
        cs = false;
      }
      cs = chr == ' ';
    }
    return out.toString();
  }

  public static String[] toArray(String... string) {
    return string;
  }

  public static String toString(String[] value, char sep) {
    if (Containers.isEmpty(value))
      return EMPTY;    
    StringBuilder b = new StringBuilder(value.length * 5);
    for(int i = 0; i < value.length; i++) {
      if (b.length() > 0)
        b.append(sep);
      b.append(value[i]);
    }
    return b.toString();
  }

  public static boolean isTrue(Object value) {
    return isTrue(Objects.toString(value, "false"));
  }

  public static boolean isTrue(String value) {
    value = trim(value).toLowerCase();
    if (!hasText(value))
      return false;
    switch(value) {
    case "1":
    case "yes":
    case "on":
    case "sim":
    case "true":
    case "ok":
      return true;
    default:
      return false;
    }
  }

  public static boolean isFalse(String devmode) {
    devmode = trim(devmode).toLowerCase();
    if (!hasText(devmode))
      return false;
    switch(devmode) {
    case "0":
    case "no":
    case "off":
    case "não":
    case "nao":
    case "false":
      return true;
    default:
      return false;
    }
  }

  public static String separate(Set<?> set, char separator, boolean quotes) {
    if (Containers.isEmpty(set))
      return Strings.EMPTY;
    StringBuilder b = new StringBuilder();
    set.forEach(s -> {
      String str = s.toString();
      if (hasText(str)) {
        if (b.length() > 0)
          b.append(separator);
        if (quotes)
          b.append('\'').append(trim(str)).append('\'');
        else
          b.append(trim(str));
      }
    });
    return b.toString();
  }

  private static String replaceEach(final String text, final String[] searchList, 
      final String[] replacementList, final boolean repeat, final int timeToLive) {
    if (timeToLive < 0) {
      final Set<String> searchSet = new HashSet<>(Arrays.asList(searchList));
      final Set<String> replacementSet = new HashSet<>(Arrays.asList(replacementList));
      searchSet.retainAll(replacementSet);
      if (searchSet.size() > 0) {
        throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
      }
    }

    if (isEmpty(text) || isEmpty(searchList) || isEmpty(replacementList) || (isNotEmpty(searchList) && timeToLive == -1)) {
      return text;
    }

    final int searchLength = searchList.length;
    final int replacementLength = replacementList.length;

    if (searchLength != replacementLength) {
      throw new IllegalArgumentException("Search and Replace array lengths don't match: "
          + searchLength
          + " vs "
          + replacementLength);
    }

    final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

    int textIndex = -1;
    int replaceIndex = -1;
    int tempIndex = -1;

    for (int i = 0; i < searchLength; i++) {
      if (noMoreMatchesForReplIndex[i] || isEmpty(searchList[i]) || replacementList[i] == null) {
        continue;
      }
      tempIndex = text.indexOf(searchList[i]);
      if (tempIndex == -1) {
        noMoreMatchesForReplIndex[i] = true;
      } else {
        if (textIndex == -1 || tempIndex < textIndex) {
          textIndex = tempIndex;
          replaceIndex = i;
        }
      }
    }

    if (textIndex == -1) {
      return text;
    }

    int start = 0;
    int increase = 0;

    for (int i = 0; i < searchList.length; i++) {
      if (searchList[i] == null || replacementList[i] == null) {
        continue;
      }
      final int greater = replacementList[i].length() - searchList[i].length();
      if (greater > 0) {
        increase += 3 * greater; // assume 3 matches
      }
    }
    increase = Math.min(increase, text.length() / 5);

    final StringBuilder buf = new StringBuilder(text.length() + increase);

    while (textIndex != -1) {

      for (int i = start; i < textIndex; i++) {
        buf.append(text.charAt(i));
      }
      buf.append(replacementList[replaceIndex]);

      start = textIndex + searchList[replaceIndex].length();

      textIndex = -1;
      replaceIndex = -1;
      tempIndex = -1;
      for (int i = 0; i < searchLength; i++) {
        if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
            searchList[i].isEmpty() || replacementList[i] == null) {
          continue;
        }
        tempIndex = text.indexOf(searchList[i], start);
        if (tempIndex == -1) {
          noMoreMatchesForReplIndex[i] = true;
        } else {
          if (textIndex == -1 || tempIndex < textIndex) {
            textIndex = tempIndex;
            replaceIndex = i;
          }
        }
      }
    }
    final int textLength = text.length();
    for (int i = start; i < textLength; i++) {
      buf.append(text.charAt(i));
    }
    final String result = buf.toString();
    if (!repeat) {
      return result;
    }

    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
  }

  public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
    final int timeToLive = searchList == null ? 0 : searchList.length;
    return replaceEach(text, searchList, replacementList, true, timeToLive);
  }
  
  public static String encodeHtmlSpace(String url) {
    return replaceEach(url, SPACE_STRING_ARRAY, HTML_SPACE_ARRAY);
  }
}

