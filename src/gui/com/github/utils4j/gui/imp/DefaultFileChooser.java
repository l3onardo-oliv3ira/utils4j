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

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

public class DefaultFileChooser extends JFileChooser {
  private static final long serialVersionUID = 1L;
  
  static {
    UIManager.put("FileChooser.lookInLabelMnemonic", 69);
    UIManager.put("FileChooser.lookInLabelText", "Pesquisar em");
    UIManager.put("FileChooser.saveInLabelText", "Salvar em");
    UIManager.put("FileChooser.openButtonToolTipText", "Abrir");
    UIManager.put("FileChooser.openButtonAccessibleName", "Abrir");
    UIManager.put("FileChooser.openButtonText", "Abrir");
    UIManager.put("FileChooser.fileNameLabelMnemonic", 78);
    UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo");
    UIManager.put("FileChooser.filesOfTypeLabelMnemonic", 84);
    UIManager.put("FileChooser.filesOfTypeLabelText", "Arquivos do Tipo");
    UIManager.put("FileChooser.upFolderToolTipText", "Um n\u00edvel acima");
    UIManager.put("FileChooser.upFolderAccessibleName", "Um n\u00edvel acima");
    UIManager.put("FileChooser.homeFolderToolTipText", "Desktop");
    UIManager.put("FileChooser.homeFolderAccessibleName", "Desktop");
    UIManager.put("FileChooser.newFolderToolTipText", "Criar nova pasta");
    UIManager.put("FileChooser.newFolderAccessibleName", "Criar nova pasta");
    UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
    UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");
    UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
    UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Detalhes");
    UIManager.put("FileChooser.fileNameHeaderText", "Nome");
    UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");
    UIManager.put("FileChooser.fileTypeHeaderText", "Tipo");
    UIManager.put("FileChooser.fileDateHeaderText", "Data");
    UIManager.put("FileChooser.fileAttrHeaderText", "Atributos");    
  }  
  
  @Override
  protected JDialog createDialog(final Component parent) throws HeadlessException {
    final JDialog dialog = super.createDialog(parent);
    dialog.setLocation(100, 100);
    dialog.setAlwaysOnTop(true);
    return dialog;
  }
}
