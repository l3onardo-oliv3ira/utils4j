package com.github.utils4j.gui.imp;

import java.awt.Window.Type;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Dialogs {

  static {
    UIManager.put("OptionPane.cancelButtonText", "Cancelar");
    UIManager.put("OptionPane.noButtonText", "Não");
    UIManager.put("OptionPane.okButtonText", "OK");
    UIManager.put("OptionPane.yesButtonText", "Sim"); 
  }
  
  private static final JFrame ON_TOP_FRAME = new JFrame("");
  
  static {
    ON_TOP_FRAME.setType(Type.UTILITY);
    ON_TOP_FRAME.setAlwaysOnTop(true);        
  }
  
  private Dialogs() {}

  public static String input(final String message, final Object defaultValue) {
    return JOptionPane.showInputDialog(ON_TOP_FRAME, message, defaultValue);
  }

  public static void info(final String message) {
    JOptionPane.showMessageDialog(ON_TOP_FRAME, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void error(final String message) {
    JOptionPane.showMessageDialog(ON_TOP_FRAME, message, "Erro", JOptionPane.ERROR_MESSAGE);
  }

  public static Boolean yesNo(final String message, final String title, final boolean cancelOption) {
    final int options = cancelOption ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION;
    final int answer = JOptionPane.showConfirmDialog(ON_TOP_FRAME, message, title, options);

    switch (answer) {
    case JOptionPane.YES_OPTION:
      return true;
    case JOptionPane.NO_OPTION:
      return false;
    case JOptionPane.CANCEL_OPTION:
      return null;
    }
    return null;
  }

  public static String fileDialog(final String extensionDescription, final String extension, final boolean openFile) {
    final JFileChooser chooser = new JFileChooser();
    final FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionDescription + " (*." + extension + ")", extension);
    chooser.setFileFilter(filter);
    String fileName = null;
    for (;;) {
      final int result = openFile ? chooser.showOpenDialog(null) : chooser.showSaveDialog(null);
      if (result != JFileChooser.APPROVE_OPTION) {
        return null;
      }
      fileName = chooser.getSelectedFile().getAbsolutePath();
      final File f = new File(fileName);
      if (f.exists()) {
        if (!openFile) {
          final String shortFileName = chooser.getSelectedFile().getName();
          final Boolean replaceFile = getBoolean(shortFileName + " já existe. Gosaria de substituí-lo?", "Escolha um arquivo", false);
          if (replaceFile) {
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

  public static String getOption(final String message, final String[] options, final String defaultValue) {
    return (String) JOptionPane.showInputDialog(ON_TOP_FRAME, message, "Escolha uma", JOptionPane.QUESTION_MESSAGE, null, options, defaultValue);
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

  public static Float getFloat(final String message, final Float defaultValue, final Float min, final Float max) {
    for (;;) {
      final String textInput = input(message, defaultValue);
      if (textInput == null) {
        return null;
      }
      Float input;
      try {
        input = Float.parseFloat(textInput);
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

  public static Boolean getBoolean(final String message, String title) {
    return getBoolean(message, title, true);
  }

  public static Boolean getBoolean(final String message, String title, final boolean cancelOption) {
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
    Dialogs.error("deu pau");
  }
}