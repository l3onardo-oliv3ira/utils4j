package com.github.utils4j.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class JsonTools {
  
  private JsonTools() {}
  
  private static class JsonToolsHolder {
    static final ObjectMapper MAPPER = new ObjectMapper();
    static {
      MAPPER.registerModule(new Jdk8Module());
    }
  }
  
  public static ObjectMapper mapper() {
    return JsonToolsHolder.MAPPER;
  }
}
