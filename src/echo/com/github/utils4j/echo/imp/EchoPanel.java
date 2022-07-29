package com.github.utils4j.echo.imp;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.echo.IEcho;
import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Strings;

public class EchoPanel extends JPanel implements IEcho {
  
  private static final Dimension DEFAULT_SIZE = new Dimension(500, 680);

  private static final int MAX_ITEM_COUNT = 800;
  
  private final JTextArea textArea = new JTextArea();

  private final String headerItemFormat;

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

  @Override
  public JPanel asPanel() {
    return this;
  }

  @Override
  public final void clear() {
    textArea.setText(Strings.empty()); //auto clean
    itemCount = 0;
    onNewItem(Strings.empty(), itemCount);
  }

  @Override
  public final void addRequest(String request) {
    if (request != null) {
      addItem(request);
    }
  }
  
  private void addItem(String item) {
    if (itemCount >= MAX_ITEM_COUNT) {
      clear();      
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
  }
}
