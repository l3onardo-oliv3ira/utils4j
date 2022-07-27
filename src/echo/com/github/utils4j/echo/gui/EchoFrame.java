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

package com.github.utils4j.echo.gui;

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.gui.imp.SimpleFrame;
import com.github.utils4j.imp.Args;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import net.miginfocom.swing.MigLayout;

public class EchoFrame extends SimpleFrame {

  private static final Dimension MININUM_SIZE = new Dimension(300, 350);
  
  private static final Dimension DEFAULT_SIZE = new Dimension(500, 680);

  private static final int MAX_ITEM_COUNT = 800;
  
  private final JTextArea textArea = new JTextArea();

  private final String headerItemFormat;

  private final Disposable ticket;
  
  private int itemCount = 0;

  public EchoFrame(Observable<String> echoCallback) {
    this("Echo", "%s\n", echoCallback);
  }
  
  public EchoFrame(String title, String headerItemFormat, Observable<String> echoCallback) {
    super(title);
    Args.requireNonNull(echoCallback, "requestCallback is null");
    this.ticket = echoCallback.subscribe(this::handleRequest);
    this.headerItemFormat = Args.requireNonNull(headerItemFormat, "headerItemFormat is null");
    setup();
  }

  @Override
  public void dispose() {
    ticket.dispose();
    super.dispose();
  }
  
  protected JPanel north() {
    return new JPanel();
  }

  protected void clear(ActionEvent e) {
    textArea.setText(""); //auto clean
    itemCount = 0;
  }

  private void handleRequest(String request) {
    if (request != null) {
      invokeLater(() -> addItem(request));
    }
  }
  
  private void addItem(String item) {
    if (itemCount++ > MAX_ITEM_COUNT) {
      clear(null);      
    }
    textArea.append(String.format(headerItemFormat, itemCount));
    textArea.append(item + "\n\r\n\r");
  }
  
  private void setup() {
    setupLayout();
    setFixedMinimumSize(MININUM_SIZE);
    setBounds(getBounds().x, getBounds().y, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
    setupLocation();
  }
  
  protected void setupLocation() {
    setLocationRelativeTo(null);  
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
    JPanel contentPane = new JPanel();    
    contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    contentPane.setLayout(new BorderLayout(0, 0));
    contentPane.add(north(), BorderLayout.NORTH);
    contentPane.add(center(), BorderLayout.CENTER);
    contentPane.add(south(), BorderLayout.SOUTH);
    setContentPane(contentPane);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    pack();
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {        
        onEscPressed(null);
      }
    });
  }

  private JPanel south() {
    JPanel southPane = new JPanel();
    JButton fechar = new JButton("Fechar");
    fechar.addActionListener(this::onEscPressed);
    JButton btnLimpar = new JButton("Limpar");
    btnLimpar.setPreferredSize(fechar.getPreferredSize());
    btnLimpar.addActionListener(this::clear);    
    southPane.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    southPane.add(btnLimpar);
    southPane.add(fechar);
    return southPane;
  }
}
