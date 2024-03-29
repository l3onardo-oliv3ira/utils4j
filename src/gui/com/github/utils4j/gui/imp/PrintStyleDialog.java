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

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.imp.Strings.empty;
import static com.github.utils4j.imp.Strings.optional;
import static com.github.utils4j.imp.Strings.trim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.utils4j.gui.IPrintStyleDialog;
import com.github.utils4j.imp.Strings;

import net.miginfocom.swing.MigLayout;

public final class PrintStyleDialog extends SimpleDialog implements IPrintStyleDialog {
  
  private static final String TOOLTIP = "<html><style>.c{ color: 'blue';}</style>"
      + "<table>"
      + "<tr>"
      + "  <td class='c'>1 <b>;</b> 2 <b>;</b> 5</td>"
      + "  <td>Três arquivos: [1], [2] e [5]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td class='c'>10 <b>-</b> 13</td>"
      + "  <td>Um arquivo: [10, 11, 12, 13]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td class='c'>7 <b>;</b> 9 <b>-</b> 12 <b>;</b> 21</td>"
      + "  <td>Três arquivos: [7], [9, 10, 11, 12] e [21]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td class='c'>11 <b>-</b> *</td>"
      + "  <td>Um arquivo: [11, 12, 13.... última página]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td class='c'>3 <b>;</b> 27 <b>-</b> *</td>"
      + "  <td>Dois arquivos: [3], [27, 28... última página]</td>"
      + "</tr>"
      + "</html>";

  private static final Border RED_BORDER = BorderFactory.createLineBorder(Color.RED, 2);
  
  public static void show(Image icon) {
    invokeLater(() -> display(icon));
  }

  private static void display(Image icon) {
    new PrintStyleDialog(icon).display();
  }
  
  public static void main(String... args) {
    show(null);
  }

  private final JButton okButton = new JButton("OK");
  
  private final JTextField textField = new JTextField();
  
  private final Border defaultBorder = textField.getBorder();
  
  public PrintStyleDialog(Image icon) {
    super("Escolha de páginas", icon, true);
    setLayout(new BorderLayout());
    add(north(), BorderLayout.NORTH);
    add(center(), BorderLayout.CENTER);
    add(south(), BorderLayout.SOUTH);
    setupPages();
    setResizable(false);
    setAutoRequestFocus(true);
    setAlwaysOnTop(true);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    pack();
    toCenter();    
  }
  
  private JPanel north() {
    JPanel panel = new JPanel(new MigLayout("fillx"));
    JLabel label = new JLabel("Informe o intervalo de páginas:");
    panel.add(label, "wrap");
    panel.add(textField, "growx");
    return panel;
  }

  private JPanel center() {
    JPanel textPanel = new JPanel(new MigLayout());
    JPanel panelHelp = new JPanel();
    panelHelp.setBorder(BorderFactory.createTitledBorder("Exemplos:"));
    panelHelp.add(new JLabel(TOOLTIP));
    textPanel.add(panelHelp);
    return textPanel;
  }

  private JPanel south() {
    okButton.addActionListener((e) -> onOk(e));
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener((e) -> onEscPressed(e));
    okButton.setPreferredSize(cancelButton.getPreferredSize());
    JPanel southPane = new JPanel();
    southPane.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    southPane.add(okButton);
    southPane.add(cancelButton);
    return southPane;
  }
  
  private void setupPages() {
    textField.setText(empty());
    textField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        textField.setBorder(defaultBorder);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        textField.setBorder(defaultBorder);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        textField.setBorder(defaultBorder);
      }
    });
  }
  
  protected boolean checkSyntax() {
    String text = Strings.trim(textField.getText());
    for(int i = 0; i < text.length(); i++) {
      char chr = text.charAt(i);
      if (Character.isDigit(chr) || Character.isWhitespace(chr))
        continue;
      if (chr != '-' && chr != ';' && chr != '*') {
        return false;
      }
    }
    String[] parts = text.split(";");
    for(String part: parts) {
      if (!Strings.hasText(part)) {
        return false;
      }
      String[] pages = part.split("-");
      if (pages.length > 2) {
        return false;
      }
      int previous = 0;
      for(String page: pages) {
        if ((page = Strings.trim(page)).length() == 0) {
          return false;
        }
        int p = "*".equals(page) ? Integer.MAX_VALUE : Strings.toInt(page, -1);
        if (p < previous || p == 0) {
          return false;
        }
        previous = p; 
      }
    }
    return true;
  }
  
  @Override
  protected void onEscPressed(ActionEvent e) {
    setAlwaysOnTop(false);    
    Dialogs.Choice choice = Dialogs.getBoolean(
      "Deseja mesmo cancelar a operação?",
      "Cancelamento da operação", 
      false
    );
    if (choice == Dialogs.Choice.YES) {
      this.textField.setText(empty());
      this.close();
    }
  }
  

  private void onOk(ActionEvent e) {
    Optional<String> fileName = optional(trim(this.textField.getText()));
    if (fileName.isPresent() && checkSyntax()) {
      close();
    } else {
      this.textField.setBorder(RED_BORDER);
    }
  }
  
  @Override
  public Optional<String> getPagesInterval() {
    setVisible(true);
    return Strings.optional(textField.getText());
  }


  private void display() {
    this.toCenter();
    this.setVisible(true);
  }
}


