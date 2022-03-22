package com.github.utils4j.gui.imp;

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.gui.imp.SwingTools.setFixedMinimumSize;
import static com.github.utils4j.imp.Strings.empty;
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
    invokeLater(() -> display(icon, message, detail, cause));
  }

  private static void display(Image icon, String message, String detail, Throwable cause) {
    new ExceptionAlert(icon, message, detail, cause).display();
  }
  
  private static final int MIN_WIDTH = 420;
  
  private static final int MIN_HEIGHT = 160;
  
  private final JTextArea textArea = new JTextArea();
  
  private final JPanel southPane = new JPanel();
  
  private final JLabel seDetailLabel = new JLabel("<html><u>Ver detalhes</u></html>");
  
  private ExceptionAlert(Image icon, String message, String detail, Throwable cause) {
    super("Mensagem de erro", icon);
    setupLayout(message, detail, cause);
    setupListeners();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setFixedMinimumSize(this, new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setAutoRequestFocus(true);
  }

  private void setupLayout(String message, String detail, Throwable cause) {
    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    contentPane.setLayout(new BorderLayout(0, 0));
    contentPane.add(north(message), BorderLayout.NORTH);
    contentPane.add(center(detail, cause), BorderLayout.CENTER);
    contentPane.add(south(), BorderLayout.SOUTH);
    setContentPane(contentPane);
  }

  private void setupListeners() {
    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent e) {
        if ((e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH){
          seDetailLabel.setText("<html><u>Esconder detalhes</u></html>");
        } else if ((e.getNewState() & Frame.NORMAL) == Frame.NORMAL) {
          seDetailLabel.setText("<html><u>Ver detalhes</u></html>");
        }
      }
   });    
  }
  
  @Override
  protected void onEscPressed(ActionEvent e) {
    super.onEscPressed(e);    
  }

  private JPanel south() {
    JButton okButton = new JButton("OK");
    okButton.addActionListener((e) -> {
      onEscPressed(e);      
    });
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
    northPane.setLayout(new MigLayout());
    JLabel activityLabel = new JLabel("<html>&nbsp;" + Strings.trim(message) + "</html>");
    activityLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
    activityLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
    activityLabel.setHorizontalAlignment(SwingConstants.LEFT);
    northPane.add(activityLabel, "wrap");
    
    JPanel detailPanel = new JPanel();
    detailPanel.setLayout(new MigLayout("center"));
    seDetailLabel.setVerticalAlignment(SwingConstants.BOTTOM);
    seDetailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    seDetailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    seDetailLabel.setVerticalAlignment(SwingConstants.CENTER);
    seDetailLabel.setForeground(Color.BLUE);
    seDetailLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
    seDetailLabel.addMouseListener(new MouseAdapter(){  
      public void mouseClicked(MouseEvent e) {
        setDetail(seDetailLabel);
      }
    });
    detailPanel.add(seDetailLabel);
    northPane.add(detailPanel, "pushx, growx");
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


