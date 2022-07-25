package com.github.utils4j.gui.imp;

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.gui.imp.SwingTools.setFixedMinimumSize;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Strings;
import com.github.utils4j.imp.Threads;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import net.miginfocom.swing.MigLayout;

public class EchoFrame extends SimpleFrame {

  private static final Dimension MININUM_SIZE = new Dimension(500, 680);

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
  }

  private void handleRequest(String request) {
    if (request != null) {
      invokeLater(() -> addItem(request));
    }
  }
  
  private void addItem(String item) {
    if (itemCount++ > MAX_ITEM_COUNT) {
      clear(null);
      itemCount = 0;
    }
    textArea.append(String.format(headerItemFormat, itemCount));
    textArea.append(item + "\n\r\n\r");
  }
  
  private void setup() {
    setupLayout();
    setFixedMinimumSize(this, MININUM_SIZE);
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
  
  public static void main(String[] args) throws InterruptedException {
  
    final BehaviorSubject<String> subject = BehaviorSubject.create();
    
    final EchoFrame dialog = new EchoFrame(subject);
    
    invokeLater(() -> dialog.showToFront());
    
    Threads.startAsync(() -> {
      final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
      String line;
      try {
        while(Strings.hasText((line = console.readLine()))) {
          subject.onNext(line);
        }
      } catch (IOException e) {
        subject.onError(e);
      } finally {
        subject.onComplete();
      }
    }).join();
    
    dialog.close();
    
    System.out.println("Fim");
  }

}
