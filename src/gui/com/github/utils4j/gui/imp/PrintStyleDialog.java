package com.github.utils4j.gui.imp;

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
  
  private static final String TOOLTIP = "<html><b>Exemplos:</b> <br>"
      + "<table>"
      + "<tr>"
      + "  <td>1 <b>;</b> 2 <b>;</b> 5</td>"
      + "  <td>Três arquivos: [1], [2] e [5]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td>10 <b>-</b> 13</td>"
      + "  <td>Um arquivo: [10, 11, 12, 13]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td>7 <b>;</b> 9 <b>-</b> 12 <b>;</b> 21</td>"
      + "  <td>Três arquivos: [7], [9, 10, 11, 12] e [21]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td>11 <b>-</b> *</td>"
      + "  <td>Um arquivo: [11, 12, 13.... última página]</td>"
      + "</tr>"
      + "<tr>"
      + "  <td>3 <b>;</b> 27 <b>-</b> *</td>"
      + "  <td>Dois arquivos: [3], [27, 28... última página]</td>"
      + "</tr>"
      + "</html>";

  private static final Border RED_BORDER = BorderFactory.createLineBorder(Color.RED, 2);
  
  public static void show(Image icon) {
    SwingTools.invokeLater(() -> display(icon));
  }

  private static void display(Image icon) {
    new PrintStyleDialog(icon).display();
  }
  
  public static void main(String... args) {
    show(null);
  }

  private final JButton okButton = new JButton("OK");
  
  private final JTextField pages = new JTextField();
  
  private final Border defaultBorder = pages.getBorder();
  
  public PrintStyleDialog(Image icon) {
    super("Escolha de páginas", icon, true);
    setLayout(new BorderLayout());
    add(center(), BorderLayout.CENTER);
    add(south(), BorderLayout.SOUTH);
    setupPages();
    setResizable(false);
    setSize(300, 140);
    setLocationRelativeTo(null);
    setAutoRequestFocus(true);
    setAlwaysOnTop(true);
  }

  private JPanel center() {
    JPanel panel = new JPanel();
    panel.setLayout(new MigLayout());
    JLabel label = new JLabel("Informe o intervalo de páginas:");
    label.setToolTipText(TOOLTIP);
    CustomTooltipDelayer.attach(label, 60000);
    panel.add(label, "wrap");
    pages.setToolTipText(TOOLTIP);
    CustomTooltipDelayer.attach(pages, 60000);
    panel.add(pages, "push, growx");
    return panel;
  }

  private JPanel south() {
    okButton.addActionListener((e) -> onOk(e));
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener((e) -> onEscPressed(e));
    JPanel southPane = new JPanel();
    southPane.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    southPane.add(okButton);
    southPane.add(cancelButton);
    return southPane;
  }
  
  private void setupPages() {
    pages.setText(empty());
    pages.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        pages.setBorder(defaultBorder);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        pages.setBorder(defaultBorder);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        pages.setBorder(defaultBorder);
      }
    });
  }
  
  protected boolean checkSyntax() {
    String text = Strings.trim(pages.getText());
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
    boolean cancell = Dialogs.getBoolean(
      "Deseja mesmo cancelar a operação?",
      "Cancelamento da operação", 
      false
    );
    if (cancell) {
      this.pages.setText(empty());
      this.close();
    }
  }
  

  private void onOk(ActionEvent e) {
    Optional<String> fileName = optional(trim(this.pages.getText()));
    if (fileName.isPresent() && checkSyntax()) {
      close();
    } else {
      this.pages.setBorder(RED_BORDER);
    }
  }
  
  @Override
  public Optional<String> getPagesInterval() {
    setVisible(true);
    return Strings.optional(pages.getText());
  }


  private void display() {
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}


