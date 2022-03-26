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


package com.github.utils4j.gui.imp;

import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.TransferHandler;


public abstract class MediaTransferHandler extends TransferHandler {

  private final DataFlavor uriListFlavor;
  private final DataFlavor javaUrlFlavor;
  private final DataFlavor javaFileListFlavor;
  private final DataFlavor stringFlavor;

  public MediaTransferHandler() {
    uriListFlavor = newDataFlavor("text/uri-list;class=java.lang.String");
    javaUrlFlavor = newDataFlavor("application/x-java-url;class=java.net.URL");
    javaFileListFlavor = DataFlavor.javaFileListFlavor;
    stringFlavor = DataFlavor.stringFlavor;
  }

  private DataFlavor newDataFlavor(String mimeType) {
    try {
      return new DataFlavor(mimeType);
    }catch (ClassNotFoundException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public final boolean canImport(TransferSupport support) {
    return getDataFlavor(support) != null;
  }

  @Override
  public final boolean importData(TransferSupport support) {
    DataFlavor flavor = getDataFlavor(support);
    if (flavor != null) {
      try {
        Object transferData = support.getTransferable().getTransferData(flavor);
        if (transferData instanceof String) {
          String value = (String)transferData;
          String[] uris = value.split("\\r\\n");
          if (uris.length > 0) {
            onMediaDropped(uris);
          }
          return true;
        } else if (transferData instanceof URL) {
          URL value = (URL)transferData;
          String uri = value.toExternalForm();
          onMediaDropped(new String[] {uri});
        } else if (transferData instanceof List) {
          List<?> value = (List<?>)transferData;
          if (value.size() > 0) {
            File file = (File)value.get(0);
            String uri = file.getAbsolutePath();
            onMediaDropped(new String[] {uri});
          }
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  private DataFlavor getDataFlavor(TransferSupport support) {
    if (support.isDataFlavorSupported(uriListFlavor)) {
      return uriListFlavor;
    }
    if (support.isDataFlavorSupported(javaUrlFlavor)) {
      return javaUrlFlavor;
    }
    if (support.isDataFlavorSupported(javaFileListFlavor)) {
      return javaFileListFlavor;
    }
    if (support.isDataFlavorSupported(stringFlavor)) {
      return stringFlavor;
    }
    return null;
  }

  protected abstract void onMediaDropped(String[] uris);
}
