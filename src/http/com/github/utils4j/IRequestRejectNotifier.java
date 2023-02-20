package com.github.utils4j;

import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public interface IRequestRejectNotifier {
  
  void notifyReject(HttpExchange request, String cause);
}
