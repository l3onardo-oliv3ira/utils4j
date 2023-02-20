package com.github.utils4j.imp;

import static com.github.utils4j.imp.Strings.empty;
import static com.github.utils4j.imp.Strings.text;
import static java.lang.System.currentTimeMillis;

import java.security.SecureRandom;
import java.util.Base64;

public class Randomizer {
  
  private static final SecureRandom RANDOM = new SecureRandom();

  private Randomizer() {}
  
  public static String random() {
    return random(empty());
  }
  
  public static String nocache() {
    return random(Long.toString(currentTimeMillis()));
  }
  
  public static String random(String suffix) {
    byte[] randomBytes = new byte[48];
    RANDOM.nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes) + text(suffix);
  }
  
  public static void main(String[] args) {
    System.out.println(nocache());
  }
}
