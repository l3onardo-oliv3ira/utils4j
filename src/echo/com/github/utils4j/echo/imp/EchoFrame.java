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


package com.github.utils4j.echo.imp;

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.utils4j.echo.IEcho;
import com.github.utils4j.gui.imp.Images;
import com.github.utils4j.gui.imp.SimpleFrame;
import com.github.utils4j.imp.Args;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import net.miginfocom.swing.MigLayout;

public class EchoFrame extends SimpleFrame {

  private static final Dimension MININUM_SIZE = new Dimension(300, 350);
  
  private final IEcho panel;

  private final Disposable ticket;

  public EchoFrame(Observable<String> echoCallback) {
    this(echoCallback, "Echo", new EchoPanel());
  }

  public EchoFrame(Observable<String> echoCallback, IEcho panel) {
    this(echoCallback, "Echo", panel);
  }
  
  public EchoFrame(Observable<String> echoCallback, String title) {
    this(echoCallback, title, new EchoPanel());
  }
  
  public EchoFrame(Observable<String> echoCallback, String title, IEcho panel) {
    super(title);
    echoCallback = Args.requireNonNull(echoCallback, "requestCallback is null");
    this.panel = Args.requireNonNull(panel, "panel is null");    
    this.ticket = echoCallback.subscribe(this::handleRequest);
    setup();
  }

  @Override
  public void dispose() {
    ticket.dispose();
    super.dispose();
  }
  
  private void handleRequest(String request) {
    if (request != null) {
      invokeLater(() -> panel.addRequest(request));
    }
  }
  
  private void setup() {
    setupLayout();
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setIconImage(Images.ECHO.asImage());    
    setFixedMinimumSize(MININUM_SIZE);
    pack();
    toCenter();
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {        
        onEscPressed(null);
      }
    });
  }
  
  private void setupLayout() {
    JPanel content = new JPanel();
    content.setLayout(new BorderLayout(0, 0));
    content.add(panel.asPanel(), BorderLayout.CENTER);
    content.add(south(), BorderLayout.SOUTH);
    setContentPane(content);
  }

  private Component south() {
    JButton cleanButton = new JButton("Limpar");
    cleanButton.addActionListener((e) -> panel.clear());
    JButton closeButton = new JButton("Fechar");
    closeButton.addActionListener(this::onEscPressed);
    
    JPanel southPane = new JPanel();
    southPane.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    southPane.add(cleanButton);
    southPane.add(closeButton);
    return southPane;
  }


  public static void main(String[] args) {
  }
}
