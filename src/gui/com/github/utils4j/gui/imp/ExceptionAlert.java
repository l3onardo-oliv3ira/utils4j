package com.github.utils4j.gui.imp;

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.gui.imp.SwingTools.setFixedMinimumSize;
import static com.github.utils4j.imp.Strings.trim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.border.EtchedBorder;

import com.github.utils4j.imp.Strings;
import com.github.utils4j.imp.Throwables;

import net.miginfocom.swing.MigLayout;

public final class ExceptionAlert extends SimpleFrame {
  
  public static void show(String message, String detail, Throwable cause) {
    show(null, message, detail, cause);
  }
  
  public static void show(Image icon, String message, String detail, Throwable cause) {
    invokeLater(() -> display(icon, message, detail, cause));
  }

  private static void display(Image icon, String message, String detail, Throwable cause) {
    new ExceptionAlert(icon, message, detail, cause).display();
  }
  
  public static void main(String... args) {
    show(null, "leonardo", "url - xpto", new Throwable("mensagem qualquer"));
  }

  private static final int MIN_WIDTH = 420;
  
  private static final int MIN_HEIGHT = 135;
  
  private final JTextArea textArea = new JTextArea();
  
  private final JPanel southPane = new JPanel();
  
  private final JLabel seeDetailsPane = new JLabel("<html><u>Ver detalhes</u></html>");
  
  private ExceptionAlert(Image icon, String message, String detail, Throwable cause) {
    super("Mensagem de erro", icon);
    setupListeners();
    final JPanel contentPane = new JPanel();
    
    contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    contentPane.setLayout(new BorderLayout(0, 0));
    contentPane.add(north(message), BorderLayout.NORTH);
    contentPane.add(center(detail, cause), BorderLayout.CENTER);
    contentPane.add(south(), BorderLayout.SOUTH);

    setFixedMinimumSize(this, new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setContentPane(contentPane);
    setAutoRequestFocus(true);
  }

  private void setupListeners() {
    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent e) {
        if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH){
          seeDetailsPane.setText("<html><u>Esconder detalhes</u></html>");
        } else if ((e.getNewState() & Frame.NORMAL) == Frame.NORMAL) {
          seeDetailsPane.setText("<html><u>Ver detalhes</u></html>");
        }
      }
   });    
 }

  private JPanel south() {
    JButton okButton = new JButton("OK");
    okButton.addActionListener((e) -> onEscPressed(e));
    southPane.setLayout(new MigLayout("center"));
    southPane.add(okButton);
    return southPane;
  }

  private JScrollPane center(String detail, Throwable cause) {
    textArea.setRows(8);
    textArea.setEditable(false);
    JScrollPane centerPane = new JScrollPane();
    centerPane.setViewportView(textArea);
    textArea.setText(trim(detail) + "\n" + Throwables.stackTrace(cause));
    return centerPane;
  }

  private JPanel north(String message) {
    final JPanel northPane = new JPanel();
    northPane.setLayout(new GridLayout(3, 1, 0, 0));
    JLabel activityLabel = new JLabel("<html>&nbsp;" + Strings.trim(message) + "</html>");
    activityLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
    activityLabel.setHorizontalAlignment(SwingConstants.LEFT);
    northPane.add(activityLabel);
    seeDetailsPane.setVerticalAlignment(SwingConstants.BOTTOM);
    seeDetailsPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    seeDetailsPane.setHorizontalAlignment(SwingConstants.CENTER);
    seeDetailsPane.setVerticalAlignment(SwingConstants.CENTER);
    seeDetailsPane.setForeground(Color.BLUE);
    seeDetailsPane.setFont(new Font("Tahoma", Font.ITALIC, 12));
    seeDetailsPane.addMouseListener(new MouseAdapter(){  
      public void mouseClicked(MouseEvent e) {
        setDetail(seeDetailsPane);
      }
    });
    northPane.add(seeDetailsPane);
    return northPane;
  }
  
  private void setDetail(JLabel seeDetailsPane) {
    boolean show = seeDetailsPane.getText().contains("Ver");
    if (show) {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
    }else {
      setExtendedState(JFrame.NORMAL);
    }
  }

  private void display() {
    this.setLocationRelativeTo(null);
    this.showToFront(); 
  }
}


