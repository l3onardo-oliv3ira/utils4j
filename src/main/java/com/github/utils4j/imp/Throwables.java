package com.github.utils4j.imp;

import static com.github.utils4j.imp.Strings.text;
import static java.util.Collections.newSetFromMap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.utils4j.imp.function.Executable;
import com.github.utils4j.imp.function.Procedure;

public final class Throwables {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Throwables.class);
  
  private static final String UNKNOWN_CAUSE = "Causa desconhecida";
  
  private Throwables() {}
  
  public static boolean tryRun(Executable<?> e) {
    return tryRun(e, false);
  }
  
  public static <E extends Exception> boolean tryRun(Procedure<Boolean, E> procedure) { 
    return tryRun(procedure, false);
  }

  public static boolean tryRun(Executable<?> e, boolean defaultIfFail) {
    return tryRun(e, defaultIfFail, false);
  }

  public static boolean tryRun(Executable<?> e, boolean defaultIfFail, boolean logQuietly) {
    try {
      e.execute();
      return true;
    }catch(Exception ex) {
      if (!logQuietly) {
        LOGGER.warn("tryRun fail", ex);
      }
      return defaultIfFail;
    }
  }
  
  public static <E extends Exception> boolean tryRun(Procedure<Boolean, E> procedure, boolean logQuietly) {
    try {
      return procedure.call();
    }catch(Exception ex) {
      if (!logQuietly) {
        LOGGER.warn("tryRun fail", ex);
      }
      return false;
    }
  }
  
  public static Optional<Exception> tryCatch(Executable<?> e) {
    try {
      e.execute();
      return Optional.empty();
    }catch(Exception ex) {
      return Optional.of(ex);
    }
  }
  
  public static void tryCatch(Executable<?> e, Consumer<Exception> catchBlock) {
    try {
      e.execute();
    }catch(Exception ex) {
      catchBlock.accept(ex);
    }
  }
  
  public static Optional<Exception> tryCatch(Procedure<?, Exception> p) {
    try {
      p.call();
      return Optional.empty();
    }catch(Exception ex) {
      return Optional.of(ex);
    }
  }
  
  public static void tryCatch(Procedure<?, Exception> p, Consumer<Exception> catchBlock) {
    try {
      p.call();
    }catch(Exception ex) {
      catchBlock.accept(ex);
    }
  }
  
  public static void tryRuntime(Executable<?> e) {
    try {
      e.execute();
    }catch(RuntimeException rte) {
      throw rte;
    }catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  public static void tryRuntime(Executable<?> e, String message) {
    try {
      e.execute();
    }catch(Exception ex) {
      throw new RuntimeException(message, ex);
    }
  }

  public static <T, E extends Exception> T tryRuntime(Procedure<T, E> procedure) {
    return tryRuntime(procedure, "");
  }

  public static <T, E extends Exception> T tryRuntime(Procedure<T, E> procedure, String throwMessageIfFail) {
    return tryRuntime(procedure, () -> throwMessageIfFail);
  }
  
  public static <T, E extends Exception> T tryRuntime(Procedure<T, E> procedure, Supplier<String> throwMessageIfFail) {
    return tryRuntime(procedure, (ex) -> new RuntimeException(Strings.needText(throwMessageIfFail.get(), "tryRuntime fail"), ex));
  }
  
  @SuppressWarnings("unchecked")
  public static <T, E extends Exception> T tryRuntime(Procedure<T, E> procedure, Function<E, RuntimeException> wrapper) {
    try {
      return procedure.call();
    }catch(Exception ex) {
      throw wrapper.apply((E)ex);
    }
  }
  
  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, T defaultIfFail) {
    return tryCall(procedure, defaultIfFail, false);
  }
  
  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, T defaultIfFail, Runnable finallyBlock) {
    return tryCall(procedure, defaultIfFail, false, finallyBlock);
  }
  
  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, T defaultIfFail, boolean logQuietly, Runnable finallyBlock) {
    return tryCall(procedure, (Supplier<T>)() -> defaultIfFail, logQuietly, finallyBlock);
  }

  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, Supplier<T> defaultIfFail) {
    return tryCall(procedure, defaultIfFail, false, () -> {});
  }

  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, T defaultIfFail, boolean logQuietly) {
    return tryCall(procedure, (Supplier<T>)() -> defaultIfFail, logQuietly, () -> {});
  }
  
  public static <T, E extends Exception> T tryCall(Procedure<T, E> procedure, Supplier<T> defaultIfFail, boolean logQuietly, Runnable finallyBlock) {
    try {
      return procedure.call();
    }catch(Exception e) {
      if (!logQuietly) {
        LOGGER.warn("tryCall fail", e);
      }
      return defaultIfFail.get();
    }finally {
      finallyBlock.run();
    }
  }
  
  public static Throwable rootCause(Throwable throwable) {
    return rootCause(throwable, (t) -> {});
  }

  private static Throwable rootCause(Throwable throwable, Consumer<Throwable> visitor) {
    if (throwable == null)
      return null;
    Throwable rootCause;
    //preventy circular causes
    Set<Throwable> dejaVu = newSetFromMap(new IdentityHashMap<Throwable, Boolean>()); 
    do {
      dejaVu.add(rootCause = throwable);
      throwable = throwable.getCause();
    }while(throwable != null && !dejaVu.contains(throwable));
    return rootCause;
  }
  
  public static Stream<Throwable> traceStream(Throwable throwable) {
    List<Throwable> list = new ArrayList<>();
    rootCause(throwable, list::add);
    return list.stream();
  }
  
  public static String rootMessage(Throwable throwable) {
    Throwable rootCause = rootCause(throwable);
    if (rootCause == null)
      return UNKNOWN_CAUSE;
    String message = rootCause.getClass().getName();
    return message + ": " + text(rootCause.getMessage(), UNKNOWN_CAUSE);
  }
  
  public static String rootTrace(Throwable throwable) {
    Throwable rootCause = rootCause(throwable);
    if (rootCause == null)
      return UNKNOWN_CAUSE;
    StringWriter w = new StringWriter();
    try(PrintWriter p = new PrintWriter(w)){
      rootCause.printStackTrace(p);
      return w.toString();
    }
  }

  public static String stackTrace(Throwable throwable) {
    if (throwable == null)
      return UNKNOWN_CAUSE;
    StringWriter w = new StringWriter();
    try(PrintWriter p = new PrintWriter(w)){
      throwable.printStackTrace(p);
      return w.toString();
    }
  }

  public static boolean hasCause(Throwable throwable, Class<?> clazz) {
    if (throwable == null || clazz == null)
      return false;
    Set<Throwable> dejaVu = null;
    do {
      if (clazz.isInstance(throwable))
        return true;
      if (dejaVu == null) {
        //preventy circular causes
        dejaVu = newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
      }
      dejaVu.add(throwable);
      throwable = throwable.getCause();
    } while(throwable != null && !dejaVu.contains(throwable));
    return false;
  }
}
