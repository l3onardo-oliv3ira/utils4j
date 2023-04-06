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

import static com.github.utils4j.gui.imp.Images.SHIELD;
import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static javax.swing.BorderFactory.createLineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.github.utils4j.imp.Args;

import net.miginfocom.swing.MigLayout;

public class AdminAlert extends SimpleDialog {

  private static final long serialVersionUID = 1L;
  
  public static void show(String message, ImageIcon icon) {
    invokeLater(() -> new AdminAlert(message, icon).display());
  }
  
  private final String message;
  
  private final ImageIcon animation;
  
  private JPanel bodyPanel;
  
  private AdminAlert(String message, ImageIcon animation) {        
    super("Execução como administrador", SHIELD.asImage());
    this.message = Args.requireText(message, "message is empty");
    this.animation = Args.requireNonNull(animation, "animation is null");
    setWindowState();
    setupLayout();
  }

  private void display() {
    setVisible(true);
    toCenter();
    showToFront();
  }
  
  private void setWindowState() {
    setResizable(false);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }  
  
  private void setupLayout() {
    setLayout(new MigLayout());
    add(createMessage(), "wrap");
    add(createBody(), "growx,wrap");
    setSize(426, 150);
  }

  private JLabel createMessage() {
    return new JLabel("<html>" + message + "<br>&nbsp;</html>");
  }
  
  private JPanel createBody() {
    bodyPanel = new JPanel();
    bodyPanel.setLayout(new BorderLayout(0, 5));
    bodyPanel.add(createSee(), BorderLayout.NORTH);    
    return bodyPanel;
  }

  private JPanel createUnderstand() {
    JPanel south = new JPanel();
    JButton ok = new JButton("Entendi");
    ok.addActionListener(e -> dispose());
    south.add(ok);
    return south;
  }

  private JLabel createGif() {
    JLabel animation = new JLabel(this.animation);    
    animation.setBorder(createLineBorder(new Color(0, 34, 61)));
    return animation;
  }

  private JLabel createSee() {
    JLabel help = new JLabel("<html><u>Veja como</u></html>");
    help.setVerticalAlignment(SwingConstants.BOTTOM);
    help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    help.setHorizontalAlignment(SwingConstants.CENTER);
    help.setVerticalAlignment(SwingConstants.CENTER);
    help.setForeground(Color.BLUE);
    help.setFont(new Font("Tahoma", Font.ITALIC, 12));
    help.addMouseListener(new MouseAdapter(){  
      public void mouseClicked(MouseEvent e) {
        bodyPanel.remove(help);
        bodyPanel.add(createGif(), BorderLayout.CENTER);
        bodyPanel.add(createUnderstand(), BorderLayout.SOUTH);
        bodyPanel.updateUI();
        setSize(426, 350);
        help.removeMouseListener(this);
      }
    });
    return help;
  }  
}
