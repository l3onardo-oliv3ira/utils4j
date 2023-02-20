package com.github.utils4j.imp;

import static com.github.utils4j.IConstants.DEFAULT_CHARSET;

import org.apache.hc.core5.http.ContentType;

public class ContentTypes {
  
  public static final ContentType TEXT_HTML = ContentType.create("text/html", DEFAULT_CHARSET);
  public static final ContentType IMAGE_ICO = ContentType.create("image/x-icon");
  public static final ContentType TEXT_JS = ContentType.create("text/javascript", DEFAULT_CHARSET);
  public static final ContentType TEXT_CSS = ContentType.create("text/css", DEFAULT_CHARSET);
  
  private ContentTypes() {}
  
  public static String fromExtension(String fileName) {
    String name = Strings.trim(fileName).toLowerCase();
    if (name.endsWith(".html")) {
      return TEXT_HTML.toString();
    } else if (name.endsWith(".ico")) {
      return IMAGE_ICO.toString();
    } else if (name.endsWith(".js")) {
      return TEXT_JS.toString();
    } else if (name.endsWith(".css")) {
      return TEXT_CSS.toString();
    } else if (name.endsWith(".json") || name.endsWith(".map")) {
      return ContentType.APPLICATION_JSON.toString();
    } else if (name.endsWith(".pdf")){
      return ContentType.APPLICATION_PDF.toString();
    } else if (name.endsWith(".bmp")) {
      return ContentType.IMAGE_BMP.toString();
    } else if (name.endsWith(".gif")) {
      return ContentType.IMAGE_GIF.toString();
    } else if (name.endsWith(".jpeg")) {
      return ContentType.IMAGE_JPEG.toString();
    } else if (name.endsWith(".png")) {
      return ContentType.IMAGE_PNG.toString();
    } else if (name.endsWith(".svg")) {
      return ContentType.IMAGE_SVG.toString();
    } else if (name.endsWith(".tiff")) {
      return ContentType.IMAGE_TIFF.toString();
    } else {
      return ContentType.APPLICATION_OCTET_STREAM.toString();
    }
  }
}
