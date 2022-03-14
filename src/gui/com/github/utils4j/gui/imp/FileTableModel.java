package com.github.utils4j.gui.imp;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Pair;

public class FileTableModel extends AbstractTableModel {
  private static final int COLUMN_NAME = 0;
  private static final int COLUMN_DATE = 1;
  private static final int COLUMN_SIZE = 2;

  private String[] columnNames = {"Nome", "Data", "Tamanho"};
  private List<File> entries;

  public FileTableModel(List<File> entries) {
    this.entries = Args.requireNonNull(entries, "entries is null");
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return entries.size();
  }

  @Override
  public String getColumnName(int columnIndex) {
    return columnNames[columnIndex];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == COLUMN_SIZE)
      return Long.class;
    if (columnIndex == COLUMN_DATE)
      return Date.class;
    return String.class;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    File file = entries.get(rowIndex);
    switch (columnIndex) {
      case COLUMN_NAME:
        return file.getName();
      case COLUMN_DATE:
        return new Date(file.lastModified());
      case COLUMN_SIZE:
        return new Long(file.length());
      default:
        throw new IllegalArgumentException("Invalid column index");
    }
  }
  
  public void clear() {
    entries.clear();
    fireTableDataChanged();
  }

  public Pair<Integer, Integer> sortUp(int[] reordering) {
    int diff = 0;
    for(int i = 0; i < reordering.length; i++) {
      int index = reordering[i];
      if (index == 0) {
        break;
      }
      diff = 1;
      File f = entries.get(index - 1);
      entries.set(index - 1, entries.get(index));
      entries.set(index, f);
    }
    int begin = reordering[0] - diff;
    int end = reordering[reordering.length - 1] - diff;
    Pair<Integer, Integer> r = Pair.of(begin, end);
    fireTableDataChanged();
    return r;
  }

  public Pair<Integer, Integer> sortDown(int[] reordering) {
    int diff = 0;
    for(int i = reordering.length - 1; i >= 0 ; i--) {
      int index = reordering[i];
      if (index == entries.size() - 1) {
        break;
      }
      diff = 1;
      File f = entries.get(index + 1);
      entries.set(index + 1, entries.get(index));
      entries.set(index, f);
    }
    int begin = reordering[0] + diff;
    int end = reordering[reordering.length - 1] + diff;
    Pair<Integer, Integer> r = Pair.of(begin, end);
    fireTableDataChanged();
    return r;
  }
  
  public Pair<Integer, Integer> sortFirst(int[] reordering) {
    int diff = 0, change = 0;
    for(int i = 0; i < reordering.length; i++) {
      int index = reordering[i];
      if (index == 0) {
        break;
      }
      diff = 1;
      File f = entries.get(change);
      entries.set(change, entries.get(index));
      entries.set(index, f);
      change++;
    }
    fireTableDataChanged();
    return Pair.of(0, change > 0 ? change - diff : reordering[reordering.length - 1]);
  }
  
  public Pair<Integer, Integer>  sortLast(int[] reordering) {
    int diff = 0, change = entries.size() - 1;
    for(int i = reordering.length - 1; i >= 0 ; i--) {
      int index = reordering[i];
      if (index == entries.size() - 1) {
        break;
      }
      diff = 1;
      File f = entries.get(change);
      entries.set(change, entries.get(index));
      entries.set(index, f);
      change--;
    }
    fireTableDataChanged();
    return Pair.of(change < entries.size() - 1 ? change + diff : reordering[0], entries.size() - 1);
  }

}