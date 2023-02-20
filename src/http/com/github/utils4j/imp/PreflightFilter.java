package com.github.utils4j.imp;

import static com.github.utils4j.ICorsHeaders.NO_RESPONSE_BODY;
import static com.github.utils4j.ICorsHeaders.ORIGIN;
import static com.github.utils4j.imp.Containers.nonNull;
import static com.github.utils4j.imp.IHttpVerbs.OPTIONS;
import static com.github.utils4j.imp.Strings.optional;
import static org.apache.hc.core5.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.apache.hc.core5.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.apache.hc.core5.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.hc.core5.http.HttpStatus.SC_NO_CONTENT;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.github.utils4j.ICorsHeadersProvider;
import com.github.utils4j.IRequestRejectNotifier;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public abstract class PreflightFilter extends CORSFilter {
  
  protected PreflightFilter(IRequestRejectNotifier rejector, ICorsHeadersProvider provider) {
    super(rejector, provider);
  }
  
  @Override
  public String description() {
    return "Responde por requisições preflight do navegador, rejeitando ou aceitando segundo implementações sobrescritas de isAcceptable";
  }
  
  protected boolean isAcceptable(PreflightPack pck, StringBuilder whyNot) {
    return true; //default to accept all
  }
  
  protected void handleRejected(HttpExchange request, PreflightPack pck, String rejectCause) {
    notifier.notifyReject(request, "Rejeição preflight: " + rejectCause);
  }

  private void reply(HttpExchange request, PreflightPack pck) throws IOException {
    Headers responseHeaders = request.getResponseHeaders();
    StringBuilder whyNot = new StringBuilder();
    if (isAcceptable(pck, whyNot)) {
      accept(responseHeaders, pck.origin, true);
      request.sendResponseHeaders(SC_NO_CONTENT, NO_RESPONSE_BODY);
    } else {
      reject(responseHeaders);
      handleRejected(request, pck, whyNot.toString());
      request.sendResponseHeaders(SC_FORBIDDEN, NO_RESPONSE_BODY);      
    }
  }
  
  @Override
  public final void doFilter(HttpExchange request, Chain chain) throws IOException {
    
    boolean isOptionMethod = OPTIONS.equals(request.getRequestMethod());
    
    //Requisições preflight sempre são realizadas com o verbo OPTIONS    
    if (!isOptionMethod) {   
      chain.doFilter(request);
      return;
    }
    
    //Neste ponto sabemos que é um OPTIONS
    
    Headers reqHeaders = request.getRequestHeaders();
    
    Optional<String> originHeader = optional(reqHeaders.getFirst(ORIGIN));
    boolean hasOriginHeader = originHeader.isPresent();

    //Requisições preflight sempre são acompanhadas do header ORIGIN
    if (!hasOriginHeader) { 
      chain.doFilter(request);
      return;
    }
    
    //Neste ponto sabemos que existe ORIGIN header
    
    Optional<String> acrmHeader = optional(reqHeaders.getFirst(ACCESS_CONTROL_REQUEST_METHOD));
    boolean hasAccessControlRequestMethodHeader = acrmHeader.isPresent();
        
    //Requisições preflight sempre são acompanhadas do header ACCESS_CONTROL_REQUEST_METHOD
    if (!hasAccessControlRequestMethodHeader) {  
      chain.doFilter(request);
      return;
    }
    
    //Neste ponto sabemos que se trata de fato de um preflight request
    
    List<String> acrhHeader = nonNull(reqHeaders.get(ACCESS_CONTROL_REQUEST_HEADERS));
    
    //Vamos responder por este "pacote" de dados preflight
    reply(request, new PreflightPack(
      originHeader.get(), 
      acrmHeader.get(), 
      acrhHeader
    ));
  }
  
  protected static class PreflightPack {
    public final String origin;
    public final String acrmHeader;
    public final List<String> acrhHeaders;
    
    PreflightPack(String origin, String acrmHeader, List<String> acrhHeaders) {
      this.origin = origin;
      this.acrmHeader = acrmHeader;
      this.acrhHeaders = acrhHeaders;
    }
  }  
}