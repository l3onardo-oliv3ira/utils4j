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
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Pair;

class FileTableModel extends AbstractTableModel {
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

  public void delete(int[] rows) {
    Arrays.stream(rows).mapToObj(i -> entries.get(i)).collect(toList()).forEach(entries::remove);
    fireTableDataChanged();
  }
  
  public void add(File file) {
    entries.add(file);
    fireTableDataChanged();
  }
}
