package com.github.utils4j.imp;

import java.io.IOException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.utils4j.IConstants;
import com.github.utils4j.ITextReader;

public class JsonTextReader implements ITextReader {

  private final Class<?> clazz;
  private final Charset charset;
  
  public JsonTextReader(Class<?> clazz) {
    this(clazz, IConstants.DEFAULT_CHARSET);
  }
  
  public JsonTextReader(Class<?> clazz, Charset charset) {
    this.clazz = Args.requireNonNull(clazz, "clazz is null");
    this.charset = Args.requireNonNull(charset, "charset is null");
  }

  
  @Override
  @SuppressWarnings("unchecked")
  public <T> T read(String text) throws IOException {
    return (T) new ObjectMapper().readValue(text.getBytes(charset), clazz); 
  }
}
