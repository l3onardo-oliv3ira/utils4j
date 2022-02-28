package com.github.utils4j;

import java.nio.charset.Charset;

public interface IConstants {
  Charset UTF_8 = Charset.forName("UTF-8");

  Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
  
  Charset CP_850 = Charset.forName("cp850");
  
  Charset DEFAULT_CHARSET = UTF_8;
}
