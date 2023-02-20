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

import static com.github.utils4j.gui.imp.Dialogs.Choice.CANCEL;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Window.Type;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.utils4j.imp.function.Functions;

public final class Dialogs {
  
  private Dialogs() {}

  static {
    UIManager.put("OptionPane.cancelButtonText", "Cancelar");
    UIManager.put("OptionPane.noButtonText", "Não");
    UIManager.put("OptionPane.okButtonText", "OK");
    UIManager.put("OptionPane.yesButtonText", "Sim"); 
  }
  
  public static enum Choice {
    YES,
    NO,
    CANCEL
  }

  private static <T> T invoke(Function<JFrame, T> function) {
    JFrame top = new JFrame("");
    top.setType(Type.UTILITY);
    top.setAlwaysOnTop(true);
    try { 
      return function.apply(top); 
    } finally { 
      top.dispose(); 
    }
  }

  private static <T> T consume(Consumer<JFrame> consumer) {
    return invoke(Functions.toFunction(consumer));
  }

  public static String input(final String message, final Object defaultValue) {
    return invoke(f -> showInputDialog(f, message, defaultValue));
  }

  public static void warning(final String message) {
    consume(f -> showMessageDialog(f, message, "Informação", WARNING_MESSAGE));
  }
  
  public static void info(final String message) {
    consume(f -> showMessageDialog(f, message, "Informação", INFORMATION_MESSAGE));
  }
  
  public static void error(final String message) {
    consume(f -> showMessageDialog(f, message, "Erro", ERROR_MESSAGE));
  }

  public static Choice yesNo(final String message, final String title, final boolean cancelOption) {
    final int options = cancelOption ? YES_NO_CANCEL_OPTION : YES_NO_OPTION;
    int answer = invoke(f -> showConfirmDialog(f, message, title, options));
    switch (answer) {
    case YES_OPTION:
      return Choice.YES;
    case NO_OPTION:
      return Choice.NO;
    case CANCEL_OPTION:
      return CANCEL;
    }
    return Choice.CANCEL;
  }

  public static String fileDialog(final String extensionDescription, final String extension, final boolean openFile) {
    final JFileChooser chooser = new JFileChooser();
    final FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionDescription + " (*." + extension + ")", extension);
    chooser.setFileFilter(filter);
    String fileName = null;
    for (;;) {
      final int result = openFile ? chooser.showOpenDialog(null) : chooser.showSaveDialog(null);
      if (result != APPROVE_OPTION) {
        return null;
      }
      fileName = chooser.getSelectedFile().getAbsolutePath();
      final File f = new File(fileName);
      if (f.exists()) {
        if (!openFile) {
          final String shortFileName = chooser.getSelectedFile().getName();
          final Choice replaceFile = getBoolean(shortFileName + " já existe. Gosaria de substituí-lo?", "Escolha um arquivo", false);
          if (replaceFile == Choice.YES) {
            break;
          }
        } else {
          break;
        }
      } else {
        if (openFile) {
          error("Arquivo não existe. Por favor tente novamente!");
        } else {
          break;
        }
      }
    }
    return fileName;
  }

  public static <T> Optional<T> getOption(String message, final T[] options) {
    return getOption(message, options, null);
  }
  
  @SuppressWarnings("unchecked")
  public static <T> Optional<T> getOption(String message, final T[] options, final T defaultOption) {
    return invoke(f -> (Optional<T>)Optional.ofNullable(
      showInputDialog(
        f, 
        message, 
        "Opções", 
        QUESTION_MESSAGE, 
        null, 
        options, 
        defaultOption)
      )
    );
  }

  public static Integer getInteger(final String message, final Integer defaultValue) {
    return getInteger(message, defaultValue, null, null);
  }

  public static Integer getInteger(final String message, final Integer defaultValue, final Integer min, final Integer max) {
    for (;;) {
      final String textInput = input(message, defaultValue);
      if (textInput == null) {
        return null;
      }
      Integer input;
      try {
        input = Integer.parseInt(textInput);
      } catch (NumberFormatException ex) {
        if (textInput.equals("")) {
          error("Informe um número");
        } else {
          error("Valor inválido. Digite um número!");
        }
        continue;
      }

      if (min != null && input < min) {
        error("O número deve ser maior ou igual a " + min);
        continue;
      }

      if (max != null && input > max) {
        error("O número deve ser menor ou igual a " + max);
        continue;
      }

      return input;
    }
  }

  public static Double getDouble(final String message, final Double defaultValue, final Double min, final Double max) {
    for (;;) {
      final String textInput = input(message, defaultValue);
      if (textInput == null) {
        return null;
      }
      Double input;
      try {
        input = Double.parseDouble(textInput);
      } catch (NumberFormatException ex) {
        if (textInput.equals("")) {
          error("Informe um número");
        } else {
          error("Valor inválido. Digite um número");
        }
        continue;
      }

      if (min != null && input < min) {
        error("O número deve ser maior ou igual a " + min);
        continue;
      }

      if (max != null && input > max) {
        error("O número deve ser menor ou igual a " + max);
        continue;
      }

      return input;
    }
  }


  public static String getString(final String message, final String defaultValue) {
    return getString(message, defaultValue, false);
  }

  public static String getString(final String message, final String defaultValue, final boolean required) {
    for (;;) {
      final String textInput = input(message, defaultValue);
      if (textInput == null) {
        return null;
      }
      if (required && textInput.equals("")) {
        error("Digite a informação.");
        continue;
      }
      return textInput;
    }
  }

  public static Choice getBoolean(final String message, String title) {
    return getBoolean(message, title, true);
  }

  public static Choice getBoolean(final String message, String title, final boolean cancelOption) {
    return yesNo(message, title, cancelOption);
  }

  public static boolean isValidEmailAddress(final String email) {
    final String[] tokens = email.split("@");
    if (tokens.length != 2) {
      return false;
    }

    for (int token = 0; token < 2; token++) {
      final int tokenLength = tokens[token].length();
      if (tokens[token].charAt(0) == '.' || tokens[token].charAt(tokenLength - 1) == '.') {
        return false;
      }
      final String validCharacters = (token == 0)
          ? "abcdefghijklmnopqrstuvwxyz0123456789!#$%&'*+-/=?^_`{|}~."
              : "abcdefghijklmnopqrstuvwxyz0123456789-.";
      tokens[token].toLowerCase();
      for (int charNum = 0; charNum < tokenLength; charNum++) {
        if (validCharacters.indexOf(tokens[token].charAt(charNum)) == -1) {
          return false; 
        }
      }
    }

    return true;
  }

  public static String getEmailAddress(final String message, final String defaultValue) {
    return getEmailAddress(message, defaultValue, false);
  }

  public static String getEmailAddress(final String message, final String defaultValue, final boolean required) {
    for (;;) {
      final String textInput = getString(message, defaultValue, required);
      if (textInput == null) {
        return null;
      }
      if (textInput.equals("") && !required) {
        return "";
      }
      if (!isValidEmailAddress(textInput)) {
        error("Please enter a valid email address.");
        continue;
      }
      return textInput;
    }
  }

  public static boolean isValidTelephoneNumber(final String telephoneNumber) {
    final int stringLength = telephoneNumber.length();
    for (int charNum = 0; charNum < stringLength; charNum++) {
      final char character = telephoneNumber.charAt(charNum);
      if (Character.isDigit(character)) {
        continue;
      } else if (character == ' ') {
        continue;
      }
      return false;
    }
    return true;
  }

  public static String getTelephoneNumber(final String message, final String defaultValue) {
    return getTelephoneNumber(message, defaultValue, false);
  }

  public static String getTelephoneNumber(final String message, final String defaultValue, final boolean required) {
    for (;;) {
      final String textInput = getString(message, defaultValue, required);
      if (textInput == null) {
        return null;
      }
      if (textInput.equals("") && !required) {
        return "";
      }
      if (!isValidTelephoneNumber(textInput)) {
        error("Informe um número de telefone válido");
        continue;
      }
      return textInput;
    }
  }  
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Choice c = yesNo("LEONARDO OLIVEIRA", "titulo", false); 
      System.out.println(c.toString());
    });
  }
}
