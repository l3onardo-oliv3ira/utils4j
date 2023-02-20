package com.github.utils4j;

import java.util.Optional;

public interface ICorsHeadersProvider {
  Optional<String> getAccessControlMaxAgeHeader();
  Optional<String> getAccessControlAllowMethods();
  Optional<String> getAccessControlAllowHeaders();
  Optional<String> getAccessControlAllowCredentials();
  Optional<String> getAccessControlAllowPrivateNetwork();
}
