package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryCall;
import static com.github.utils4j.imp.Throwables.tryRuntime;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import com.github.utils4j.imp.function.Procedure;
import com.github.utils4j.imp.function.Supplier;

public class SwingTools {
  
  private SwingTools() {}
  
  public static boolean isTrue(Supplier<Boolean> supplier){
    return invokeAndWait(supplier).orElse(Boolean.FALSE);
  }
  
  public static boolean invokeAndWait(Runnable code) {
    return invokeAndWait(code, false);
  }

  public static boolean invokeAndWait(Runnable code, boolean defaultIfFail) {
    Args.requireNonNull(code, "code is null");    
    Procedure<Boolean, ?> p = SwingUtilities.isEventDispatchThread() ? 
      () -> { code.run(); return true; } : 
      () -> { SwingUtilities.invokeAndWait(code); return true;};
    return tryCall(p, defaultIfFail);
  }

  public static void invokeLater(Runnable code) {
    Args.requireNonNull(code, "code is null");
    if (SwingUtilities.isEventDispatchThread()) {
      code.run();
    } else {
      SwingUtilities.invokeLater(code);
    }
  }

  public static <T> Optional<T> invokeAndWait(Supplier<T> supplier){
    Args.requireNonNull(supplier, "supplier is null");
    Procedure<Optional<T>, ?> p = SwingUtilities.isEventDispatchThread() ? 
      () -> ofNullable(supplier.get()) : 
      () -> {
        final AtomicReference<T> ref = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> tryRuntime(() -> ref.set(supplier.get())));
        return ofNullable(ref.get());
      };
    return tryCall(p, empty());
  }
  
  public static void setFixedMinimumSize(Window window, Dimension dimension) {
    Args.requireNonNull(window, "panel is null");
    Args.requireNonNull(dimension, "dimension is null");
    window.setMinimumSize(dimension);
    window.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        Dimension windowDimension = window.getSize();
        Dimension minimumDimension = window.getMinimumSize();
        windowDimension.width = Math.max(windowDimension.width, minimumDimension.width);
        windowDimension.height =  Math.max(windowDimension.height, minimumDimension.height);
        window.setSize(windowDimension);
      }
    });    
  }
}
