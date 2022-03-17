package com.github.utils4j.imp;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class Objects {
  
  private Objects() {}
  
  public static Object getOfDefault(Object input, Object defaultIfNull) {
    return input == null ? defaultIfNull : input;
  }
  
  public static String toString(Object input, String defaultIfNull) {
    return input != null ? input.toString() : defaultIfNull;
  }
  
  public static String toString(char[] input, String defaultIfNull) {
    return input != null ? new String(input) : defaultIfNull;
  }

  public static String toJson(Object instance) throws JsonProcessingException {
    return JsonTools.mapper().writeValueAsString(instance);
  }
  
  public static Object[] arrayOf(Object ... objects) {
    return objects;
  }
}
