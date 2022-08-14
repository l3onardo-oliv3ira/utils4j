package com.github.utils4j;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IJsonSupport {
  String asJson() throws JsonProcessingException;
}
