package com.github.utils4j.echo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Strings;

import net.miginfocom.swing.MigLayout;

public class EchoPanel extends JPanel {
  
  private static final Dimension DEFAULT_SIZE = new Dimension(500, 680);

  private static final int MAX_ITEM_COUNT = 800;
  
  private final JTextArea textArea = new JTextArea();

  private final String headerItemFormat;

  private final JButton close = new JButton("Fechar");
  
  private int itemCount = 0;

  public EchoPanel() {
    this("%s\n");
  }
  
  public EchoPanel(String headerItemFormat) {  
    this.headerItemFormat = Args.requireNonNull(headerItemFormat, "headerItemFormat is null");
    setup();
  }

  protected JPanel north() {
    return new JPanel();
  }  
  
  protected void clear(ActionEvent e) {
    textArea.setText(Strings.empty()); //auto clean
    itemCount = 0;
    onNewItem(Strings.empty(), itemCount);
  }

  public void addRequest(String request) {
    if (request != null) {
      addItem(request);
    }
  }
  
  private void addItem(String item) {
    if (itemCount >= MAX_ITEM_COUNT) {
      clear(null);      
    }
    onNewItem(item, ++itemCount);
    textArea.append(String.format(headerItemFormat, itemCount));
    textArea.append(item + "\n\r\n\r");
    textArea.getCaret().setDot(Integer.MAX_VALUE);
  }
  
  protected void onNewItem(String item, int count) {  
    
  }

  private void setup() {
    setBounds(getBounds().x, getBounds().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
    setupLayout();
  }
  
  private JScrollPane center() {
    JScrollPane centerPane = new JScrollPane();
    textArea.setRows(8);
    textArea.setEditable(false);
    centerPane.setViewportView(textArea);
    centerPane.setVisible(true);
    return centerPane;
  }

  private void setupLayout() {    
    setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    setLayout(new BorderLayout(0, 0));
    add(north(), BorderLayout.NORTH);
    add(center(), BorderLayout.CENTER);
    add(south(), BorderLayout.SOUTH);
  }

  void addCloseListener(ActionListener listener) {
    close.addActionListener(listener);  
  }
    
  private JPanel south() {
    JPanel southPane = new JPanel();
    JButton cleanButton = new JButton("Limpar");
    cleanButton.setPreferredSize(close.getPreferredSize());
    cleanButton.addActionListener(this::clear);    
    southPane.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    southPane.add(cleanButton);
    southPane.add(close);
    return southPane;
  }
}
