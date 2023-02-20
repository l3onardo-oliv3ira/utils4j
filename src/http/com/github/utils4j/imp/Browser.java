package com.github.utils4j.imp;

import static java.awt.Toolkit.getDefaultToolkit;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.github.utils4j.gui.imp.MessageAlert;

public enum Browser {
  
  CHROME("Chrome", "Avançado", "Ir para 127.0.0.1 (não seguro)"),

  FIREFOX("Firefox", "Avançado", "Aceitar o risco e continuar"),

  EDGE("MSEdge", "Avançado", "Continue até 127.0.0.1 (não seguro)"),
  
  OPERA("Opera", "Help me understand", "Prosseguir para 127.0.0.1 (inseguro)"),
  
  UNKNOWN("", "", "") {
    @Override
    public final String exceptionExplain() {
      return Stream.of(VALUES).filter(p -> p != this).map(Browser::exceptionExplain).reduce((a, b) -> a + "\n  " + b).get();
    }
  };
  
  private static final Browser[] VALUES = Browser.values();

  private final String id, advanced, link;

  private Browser(String id, String advanced, String link) {
    this.id = id;
    this.link = link;    
    this.advanced = advanced;
  }
  
  public String exceptionExplain() {
    return name() + ": '" + advanced + "' -> '" + link + "'";
  }
  
  public void open(String url) {
    open(url, null);
  }
  
  public static void navigateTo(String endpoint) {
    whoIsDefault().open(endpoint);;
  }
  
  public static void navigateTo(String endpoint, Image icon) {
    whoIsDefault().open(endpoint, icon);
  }
  
  public static Browser whoIsDefault() {
    return Jvms.isWindows() ? defaultOnWindows() : UNKNOWN;
  }

  private static Browser defaultOnWindows() {    
    try {
      String output = Streams.readOutStream(
        Runtime.getRuntime()
        .exec("REG QUERY HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\Shell\\Associations\\UrlAssociations\\http\\UserChoice")
        .getInputStream()
      ) .get(2, TimeUnit.SECONDS);
      return Stream.of(VALUES).filter(b -> output.contains(b.id)).findFirst().orElse(UNKNOWN);
    } catch (Exception e) {
      return UNKNOWN;
    }
  };
  
  public void open(String url, Image icon) {
    url = Args.requireText(url, "url is null").trim();
    Browser self = this;
    try {
      Desktop.getDesktop().browse(new URI(url));
    }catch(Exception e) {
      self = UNKNOWN;
      String ctrl = Jvms.isMac() ? "Command" : "Ctrl";
      getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(url), null);
      MessageAlert.showInfo(
        "No seu navegador acesse o endereço '" + url + "'.\n" + 
        "Este endereço já foi copiado para o seu clipboard '" + 
        ctrl + "+C', aguardando apenas o '" + ctrl + "+V' na "+ 
        "barra de endereços.", 
        icon
      );
    } finally {
      if (url.toLowerCase().startsWith("https:")) {
        MessageAlert.showInfo("Caso se depare com uma página com "
          + "erro no certificado, confirme a exceção de segurança\n" 
          + "clicando em " + (self == UNKNOWN ? "\n  " : "") 
          + self.exceptionExplain(), 
          icon
        );
      }
    }
  }
}
