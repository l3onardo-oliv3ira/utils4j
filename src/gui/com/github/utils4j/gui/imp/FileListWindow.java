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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Containers;
import com.github.utils4j.imp.Dates;
import com.github.utils4j.imp.Directory;
import com.github.utils4j.imp.Media;
import com.github.utils4j.imp.Pair;
import com.github.utils4j.imp.Sizes;

import net.miginfocom.swing.MigLayout;

public class FileListWindow extends SimpleDialog implements IFileListView {

  private static final int MIN_WIDTH = 600;
  private static final int MIN_HEIGHT = 325;

  private JTable table;
  private File outputFile;
  private File currentDir;
  private Media media;
  private FileTableModel tableModel;

  private final DelayedFileChooser chooser = DelayedFileChooser.DIALOG;
  
  public FileListWindow(Image icon, List<File> files, Media media) {
    this(icon, files, media, null);
  }
  
  public FileListWindow(Image icon, List<File> files, Media media, File currentDir) {
    super("Ordem dos arquivos", icon, true);
    Args.requireNonNull(files, "files is empty");
    Args.requireNonNull(media, "media is null");
    this.media = media;
    this.currentDir = currentDir;
    setLayout(new BorderLayout());
    createCenter(files);
    createEast();
    createSouth();
    setSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setFixedMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    toCenter();
    setAutoRequestFocus(true);
    setAlwaysOnTop(true);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {        
        onEscPressed(null);
      }
    });
  }

  private JButton saveAtButton;
  
  private void createSouth() {
    saveAtButton = new JButton("Salvar...");
    saveAtButton.addActionListener(this::onSave);
    
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener(this::onEscPressed);
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new MigLayout("fillx", "push[][]", "[][]"));
    actionPanel.add(saveAtButton);
    actionPanel.add(cancelButton);

    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());
    southPanel.add(actionPanel, BorderLayout.EAST);
    add(southPanel, BorderLayout.SOUTH);
  }

  private void createEast() {
    JButton firstButton = new JButton(Images.FIRST.asIcon());
    firstButton.addActionListener(this::onFirst);
    firstButton.setToolTipText("Mover para a primeira posição.");
    JButton upButton = new JButton(Images.UP.asIcon());
    upButton.setToolTipText("Mover para uma posição acima.");
    upButton.addActionListener(this::onUp);
    JButton downButton = new JButton(Images.DOWN.asIcon());
    downButton.setToolTipText("Mover para uma posição abaixo.");
    downButton.addActionListener(this::onDown);
    JButton lastButton = new JButton(Images.LAST.asIcon());
    lastButton.addActionListener(this::onLast);
    lastButton.setToolTipText("Mover para a última posição.");
    JButton addButton = new JButton(Images.ADD.asIcon());
    addButton.addActionListener(this::onAdd);
    addButton.setToolTipText("Adicionar um novo arquivo à lista.");
    JButton remButton = new JButton(Images.REM.asIcon());
    remButton.setToolTipText("Remover arquivo(s) selecionado(s) da lista.");
    remButton.addActionListener(this::onRem);

    JPanel eastPane = new JPanel();
    eastPane.setLayout(new MigLayout());
    eastPane.add(firstButton, "wrap");
    eastPane.add(upButton, "wrap");
    eastPane.add(downButton, "wrap");
    eastPane.add(lastButton, "wrap");
    eastPane.add(new JPanel(), "wrap");
    eastPane.add(addButton, "wrap");
    eastPane.add(remButton, "wrap");
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
    panel.add(new JScrollPane(table), "pushx, pushy, growy, growx");
    add(panel, BorderLayout.CENTER);
  }

  @Override
  protected void onEscPressed(ActionEvent e) {
    setAlwaysOnTop(false);
    Dialogs.Choice choice = Dialogs.getBoolean(
      "Deseja mesmo cancelar a operação?",
      "Cancelamento da operação", 
      false
    );
    if (choice == Dialogs.Choice.YES) {
      this.outputFile = null;
      this.close();
    }
  }

  private void onSave(ActionEvent e) {
    chooser.filesOnly().select("onde será salvo o arquivo final", media, currentDir)
      .map(Directory::stringPath)
      .map(s -> s.toLowerCase().endsWith(media.getExtension(true)) ? s : s + media.getExtension(true))
      .map(s -> new File(s))
      .ifPresent(f -> {
        outputFile = f;
        close();
      });
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
  
  private void onAdd(ActionEvent e) {
    chooser.filesOnly().multiSelect("adição de novos arquivos", Media.PDF, currentDir).ifPresent(f -> Stream.of(f).forEach(tableModel::add));
    saveAtButton.setEnabled(tableModel.getRowCount() > 0);
  }
  
  private void onRem(ActionEvent e) {
    if (selectedRows.length > 0)
      tableModel.delete(selectedRows);
    saveAtButton.setEnabled(tableModel.getRowCount() > 0);
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
  public Optional<File> getOutputFile() {
    showToFront();
    return Optional.ofNullable(outputFile);
  }

  private static List<File> createListFiles() {
    return Containers
      .arrayList(new File("D:\\temp\\pdfs").listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.endsWith(".pdf");
        }
      }));
  }

  public static void main(String[] args) {
    invokeLater(() -> {
      List<File> files = createListFiles();
      IFileListView window = new FileListWindow(Images.FIRST.asImage(), files, Media.PDF, null);
      File fileName = window.getOutputFile().orElse(null);
      System.out.println(fileName);
      System.exit(0);
    });
  }
}
