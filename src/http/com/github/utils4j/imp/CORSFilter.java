package com.github.utils4j.imp;

import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK;
import static com.github.utils4j.ICorsHeaders.ACCESS_CONTROL_MAX_AGE;
import static org.apache.hc.core5.http.HttpHeaders.CACHE_CONTROL;
import static org.apache.hc.core5.http.HttpHeaders.VARY;

import com.github.utils4j.ICorsHeadersProvider;
import com.github.utils4j.IRequestRejectNotifier;
import com.sun.net.httpserver.Headers;

@SuppressWarnings("restriction")
public abstract class CORSFilter extends RejectableFilter  {
  
  private final ICorsHeadersProvider cors;
  
  protected CORSFilter(IRequestRejectNotifier rejector, ICorsHeadersProvider cors) {
    super(rejector);
    this.cors = Args.requireNonNull(cors, "cors is null");
  }
  
  private void noCache(Headers response) {
    response.set(CACHE_CONTROL, "no-store");
    response.set(VARY, "Origin");
  }
  
  protected final void reject(Headers response) {
    response.clear();
    noCache(response);
  }
  
  protected void accept(Headers response, String origin) {
    response.set(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
    noCache(response);
  }
  
  protected void accept(Headers response, String origin, boolean isOptionsVerb) {    
    accept(response, origin);
    if (isOptionsVerb) {
      cors.getAccessControlAllowMethods().ifPresent(h -> response.set(ACCESS_CONTROL_ALLOW_METHODS, h));
      cors.getAccessControlAllowHeaders().ifPresent(h -> response.set(ACCESS_CONTROL_ALLOW_HEADERS, h));
      cors.getAccessControlAllowCredentials().ifPresent(h -> response.set(ACCESS_CONTROL_ALLOW_CREDENTIALS, h));
      cors.getAccessControlAllowPrivateNetwork().ifPresent(h -> response.set(ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK, h));
      cors.getAccessControlMaxAgeHeader().ifPresent(h -> response.set(ACCESS_CONTROL_MAX_AGE, h));
    }
  }
}
