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

import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.utils4j.IConstants;

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
  
  public static String iso2utf8(char[] input, String defaultIfNull) {
    return input != null ? iso2utf8(input) : defaultIfNull;
  }
  
  public static String iso2utf8(char[] input) {
    return fromIso(input, IConstants.UTF_8);
  }
  
  public static String fromIso(char[] input, Charset outputCharset) {
    return new String(new String(input).getBytes(IConstants.ISO_8859_1), outputCharset);
  }

  public static String toString(char[] input, Charset inputCharset, Charset outputCharset) {
    return new String(new String(input).getBytes(inputCharset), outputCharset);
  }

  public static String toJson(Object instance) throws JsonProcessingException {
    return JsonTools.mapper().writeValueAsString(instance);
  }
  
  public static Object[] arrayOf(Object ... objects) {
    return objects;
  }
}
