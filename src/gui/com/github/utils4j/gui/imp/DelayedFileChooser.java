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

import static com.github.utils4j.imp.Threads.startDaemon;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.utils4j.imp.Media;
import com.github.utils4j.imp.States;


public enum DelayedFileChooser {
  
  DIALOG;  
  
  private volatile DejavuFileChooser cache = null;
  
  private DelayedFileChooser() {
    /**
     * O filechooser com tema Windows é incrivelmente lento para inicialização, então nós inicializamos esta 
     * instância numa 'thread' separada de forma então a NÃO dar ao usuário uma sensação de sistema pesado quando da sua
     * inicialização 
     * */
    startDaemon(() -> cache = new DejavuFileChooser());
  }
  
  public DelayedFileChooser filesOnly() {
    States.requireNonNull(cache, "filechooser ainda não inicializado");
    cache.setFileSelectionMode(JFileChooser.FILES_ONLY);
    return this;
  }
  
  public DelayedFileChooser folderOnly() {
    States.requireNonNull(cache, "filechooser ainda não inicializado");
    cache.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    return this;
  }
  
  public Optional<File> select(String title, Media media) {
    return select(title, media, null);
  }
  
  public Optional<File> select(String title, Media media, File current) {
    States.requireNonNull(cache, "filechooser ainda não inicializado");
    return cache.singleSelect(title, media, current);
  }
  
  public Optional<File[]> multiSelect(String title, Media media) {
    States.requireNonNull(cache, "filechooser ainda não inicializado");
    return cache.multiSelect(title, media, null);
  }
  
  public Optional<File[]> multiSelect(String title, Media media, File current) {
    States.requireNonNull(cache, "filechooser ainda não inicializado");
    return cache.multiSelect(title, media, current);
  }
  
  private static class DejavuFileChooser extends DefaultFileChooser {

    private static final long serialVersionUID = 1L;

    private DejavuFileChooser() {
      setMultiSelectionEnabled(false);
      setAcceptAllFileFilterUsed(false);
      setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private Optional<File> singleSelect(String title, Media media, File current) {
      setMultiSelectionEnabled(false);
      return doSelect(title, media, current, () -> getSelectedFile());
    }
    
    private Optional<File[]> multiSelect(String title, Media media, File current) {
      setMultiSelectionEnabled(true);
      return doSelect(title, media, current, () -> getSelectedFiles());
    }

    private <T> Optional<T> doSelect(String title, Media media, File current, Supplier<T> supplier) {
      resetChoosableFileFilters();
      setCurrentDirectory(current);
      setSelectedFile(new File("")); //clean selection list!
      setDialogTitle("Selecione para " + title.toLowerCase());
      setFileFilter(new FileNameExtensionFilter("Arquivos " + media.name(), media.getExtension()));
      if (showOpenDialog(null) == CANCEL_OPTION)
        return Optional.empty();
      return Optional.of(supplier.get());
    }
  }  
}