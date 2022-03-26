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

import static com.github.utils4j.gui.imp.SwingTools.invokeLater;
import static com.github.utils4j.gui.imp.SwingTools.setFixedMinimumSize;
import static com.github.utils4j.imp.Strings.empty;
import static com.github.utils4j.imp.Strings.optional;
import static com.github.utils4j.imp.Strings.trim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Containers;
import com.github.utils4j.imp.Dates;
import com.github.utils4j.imp.Pair;
import com.github.utils4j.imp.Sizes;
import com.github.utils4j.imp.Strings;

import net.miginfocom.swing.MigLayout;

public class FileListWindow extends SimpleDialog implements IFileListView {

  private static final int MIN_WIDTH = 500;
  private static final int MIN_HEIGHT = 250;

  private JTextField fileName = new JTextField();
  private Border defaultBorder = fileName.getBorder();
  private JTable table;
  private FileTableModel tableModel;

  public FileListWindow(Image icon, List<File> files) {
    super("Ordem dos arquivos", icon, true);
    Args.requireNonEmpty(files, "files is empty");
    setLayout(new BorderLayout());
    setupFieldName();
    createCenter(files);
    createEast();
    createSouth();
    setSize(new Dimension(650, 300));
    setFixedMinimumSize(this, new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLocationRelativeTo(null);
    setAutoRequestFocus(true);
    setAlwaysOnTop(true);
  }

  private void createSouth() {
    JButton okButton = new JButton("Salvar");
    okButton.addActionListener(this::onSave);
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener(this::onEscPressed);
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    actionPanel.add(okButton);
    actionPanel.add(cancelButton);

    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());
    JPanel textPanel = new JPanel();
    textPanel.setLayout(new MigLayout());
    textPanel.add(new JLabel("Nome do arquivo gerado: "));
    textPanel.add(fileName, "pushx, growx");
    southPanel.add(textPanel, BorderLayout.CENTER);
    southPanel.add(actionPanel, BorderLayout.EAST);
    add(southPanel, BorderLayout.SOUTH);
  }

  private void createEast() {
    JButton firstButton = new JButton(Images.FIRST.asIcon().get());
    firstButton.addActionListener(this::onFirst);
    JButton upButton = new JButton(Images.UP.asIcon().get());
    upButton.addActionListener(this::onUp);
    JButton downButton = new JButton(Images.DOWN.asIcon().get());
    downButton.addActionListener(this::onDown);
    JButton lastButton = new JButton(Images.LAST.asIcon().get());
    lastButton.addActionListener(this::onLast);
    JPanel eastPane = new JPanel();
    eastPane.setLayout(new MigLayout());
    eastPane.add(firstButton, "wrap");
    eastPane.add(upButton, "wrap");
    eastPane.add(downButton, "wrap");
    eastPane.add(lastButton);
    add(eastPane, BorderLayout.EAST);
  }

  private int[] selectedRows = new int[0];

  private void createCenter(List<File> files) {
    tableModel = new FileTableModel(files);
    table = new JTable(tableModel);
    table.setAutoCreateRowSorter(false);
    table.setFillsViewportHeight(true);
    table.setPreferredScrollableViewportSize(table.getPreferredSize());
    table.getColumnModel().getColumn(0).setPreferredWidth(250);
    table.getColumnModel().getColumn(1).setCellRenderer(new DateTableCellRenderer());
    table.getColumnModel().getColumn(2).setCellRenderer(new SizeTableCellRenderer());
    table.getColumnModel().getColumn(2).setPreferredWidth(50);
    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    table.getSelectionModel().addListSelectionListener((e) -> {
      if (!e.getValueIsAdjusting()) {
        selectedRows = table.getSelectedRows();
      }
    });

    JPanel panel = new JPanel();
    panel.setLayout(new MigLayout());
    panel.add(new JScrollPane(table), "pushx, growy, growx");
    add(panel, BorderLayout.CENTER);
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
      this.fileName.setText(empty());
      this.close();
    }
  }

  private void onSave(ActionEvent e) {
    Optional<String> fileName = optional(trim(this.fileName.getText()).replaceAll("[\\\\/:*?\"<>|]", empty()));
    if (fileName.isPresent()) {
      close();
    } else {
      this.fileName.setText("");
      this.fileName.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
  }

  private void scrollToVisible(int rowIndex, int vColIndex) {
    table.scrollRectToVisible(new Rectangle(table.getCellRect(rowIndex, 0, true)));
  }

  private void onUp(ActionEvent e) {
    if (selectedRows.length > 0) {
      Pair<Integer, Integer> p = tableModel.sortUp(selectedRows);
      table.setRowSelectionInterval(p.getKey(), p.getValue());
      scrollToVisible(p.getKey(), 0);
    }
  }

  private void onDown(ActionEvent e) {
    if (selectedRows.length > 0) {
      Pair<Integer, Integer> p = tableModel.sortDown(selectedRows);
      table.setRowSelectionInterval(p.getKey(), p.getValue());
      scrollToVisible(p.getValue(), 0);
    }
  }

  private void onFirst(ActionEvent e) {
    while(selectedRows.length > 0 && selectedRows[0] != 0){
      onUp(e);
    };
  }
  
  private void onLast(ActionEvent e) {
    while(selectedRows.length > 0 && selectedRows[selectedRows.length - 1] != table.getRowCount() - 1) {
      onDown(e);
    };
  }
  
  private void setupFieldName() {
    fileName.setText(empty());
    fileName.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        fileName.setBorder(defaultBorder);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        fileName.setBorder(defaultBorder);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        fileName.setBorder(defaultBorder);
      }
    });
  }

  private static class SizeTableCellRenderer extends DefaultTableCellRenderer {
    public SizeTableCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      if (value instanceof Long) {
        value = Sizes.defaultFormat((Long) value);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  }

  private static class DateTableCellRenderer extends DefaultTableCellRenderer {
    public DateTableCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      if (value instanceof Date) {
        value = Dates.defaultFormat((Date) value);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  }

  @Override
  public Optional<String> getFileName() {
    this.showToFront();
    return Strings.optional(fileName.getText());
  }

  public static List<File> createListFiles() {
    return Containers
      .arrayList(new File("C:\\Users\\Leonardo\\Documents\\TEMP").listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".pdf");
        }
      }));
  }

  public static void main(String[] args) {
    invokeLater(() -> {
      List<File> files = createListFiles();
      IFileListView window = new FileListWindow(Images.FIRST.asImage().get(), files);
      String fileName = window.getFileName().orElse("nada informado");
      System.out.println(fileName);
      System.exit(0);
    });
  }
}
