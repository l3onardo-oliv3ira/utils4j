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

import static com.github.utils4j.gui.IThrowableTracker.orNothing;
import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.imp.Strings.empty;
import static com.github.utils4j.imp.Strings.padStart;
import static com.github.utils4j.imp.Strings.trim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.gui.IThrowableTracker;
import com.github.utils4j.imp.Strings;
import com.github.utils4j.imp.Throwables;

import net.miginfocom.swing.MigLayout;

public final class ExceptionAlert extends SimpleFrame {
  
  public static void show(String message, Throwable cause) {
    show(message, empty(), cause);
  }

  public static void show(String message, String detail, Throwable cause) {
    show(null, message, detail, cause);
  }
  
  public static void show(Image icon, String message, String detail, Throwable cause) {
    show(icon, message, detail, cause, IThrowableTracker.NOTHING);
  }

  public static void show(String message, String detail, Throwable cause, IThrowableTracker tracker) {
    show(null, message, detail, cause, tracker);
  }

  public static void show(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
    invokeLater(() -> display(icon, message, detail, cause, tracker));
  }
  
  private static void display(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
    new ExceptionAlert(icon, message, detail, cause, tracker).display();
  }
  
  private static final Dimension MININUM_SIZE = new Dimension(420, 160);
      
  private final JPanel southPane = new JPanel();

  private final JTextArea textArea = new JTextArea();
  
  private final JScrollPane centerPane = new JScrollPane();
  
  private final JLabel detailLabel = new JLabel("<html><u>Ver detalhes</u></html>");
  
  private ExceptionAlert(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
    super("Mensagem de erro", icon);
    setupLayout(message, detail, cause, tracker);
    setupListeners();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setFixedMinimumSize(MININUM_SIZE);
    setAutoRequestFocus(true);
  }

  @Override
  protected void onEscPressed(ActionEvent e) {
    super.onEscPressed(e);    
  }

  private void display() {
    this.toCenter();
    this.showToFront(); 
  }
  
  private void setupLayout(String message, String detail, Throwable cause, IThrowableTracker tracker) {
    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    contentPane.setLayout(new BorderLayout(0, 0));
    contentPane.add(north(message), BorderLayout.NORTH);
    contentPane.add(center(detail, cause, tracker), BorderLayout.CENTER);
    contentPane.add(south(), BorderLayout.SOUTH);
    setContentPane(contentPane);
  }

  private JScrollPane center(String detail, Throwable cause, IThrowableTracker tracker) {
    textArea.setRows(8);
    textArea.setEditable(false);
    centerPane.setViewportView(textArea);
    centerPane.setVisible(false);
    textArea.setText(causes(detail, cause, tracker));
    textArea.setCaretPosition(0);
    return centerPane;
  }

  private JPanel south() {
    JButton okButton = new JButton("OK");
    okButton.addActionListener(this::onEscPressed);
    southPane.setLayout(new MigLayout("center"));
    southPane.add(okButton);
    southPane.setVisible(false);
    return southPane;
  }

  private void setDetail(JLabel seeDetailsPane) {
    boolean show = seeDetailsPane.getText().contains("Ver");
    setExtendedState(show ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
    centerPane.setVisible(show);
    southPane.setVisible(show);
  }

  private void setupListeners() {
    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent e) {
        if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH){
          detailLabel.setText("<html><u>Esconder detalhes</u></html>");
        } else if ((e.getNewState() & Frame.NORMAL) == Frame.NORMAL) {
          detailLabel.setText("<html><u>Ver detalhes</u></html>");
        }
      }
    });    
  }

  private void setDetailLabel() {
    detailLabel.setVerticalAlignment(SwingConstants.BOTTOM);
    detailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    detailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    detailLabel.setVerticalAlignment(SwingConstants.CENTER);
    detailLabel.setForeground(Color.BLUE);
    detailLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
    detailLabel.addMouseListener(new MouseAdapter(){  
      public void mouseClicked(MouseEvent e) {
        setDetail(detailLabel);
      }
    });
  }
  
  private JPanel north(String message) {
    JLabel errorLabel = new JLabel("<html>&nbsp;" + Strings.trim(message) + "</html>");
    errorLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
    errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
    errorLabel.setHorizontalAlignment(SwingConstants.LEFT);
    
    setDetailLabel();

    JPanel detailPanel = new JPanel();
    detailPanel.setLayout(new MigLayout("center"));
    detailPanel.add(detailLabel);

    JPanel northPane = new JPanel();
    northPane.setLayout(new MigLayout());
    northPane.add(errorLabel, "wrap");
    northPane.add(detailPanel, "pushx, growx");
    return northPane;
  }

  private String causes(String detail, Throwable cause, IThrowableTracker tracker) {
    final StringBuilder sb = new StringBuilder()
      .append(detail = trim(detail))
      .append(detail.length() > 0 ? "\n\n" : empty());
    final String[] messages = orNothing(tracker).track(cause);
    final int size = messages.length;
    if (size > 0) {
      sb.append("IDENTIFICADA(S) A(S) SEGUINTE(S) CAUSA(S):\n");
      int i = 0, n = 1;
      do {
        sb.append("\t")
          .append(padStart(n++, 2))
          .append(") ")
          .append(messages[i])
          .append('\n');
      } while(++i < size);
      sb.append("\n********************************************************************\n");
    };
    sb.append("INFORMAÇÕES TÉCNICAS:\n")
      .append(Throwables.stackTrace(cause));
    return sb.toString();
  }
}
