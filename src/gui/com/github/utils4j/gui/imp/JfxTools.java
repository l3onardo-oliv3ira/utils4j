package com.github.utils4j.gui.imp;

import static com.github.utils4j.imp.Throwables.tryRuntime;

import java.util.concurrent.FutureTask;

import javafx.application.Platform;

public final class JfxTools {
  
  private JfxTools() {}
  
  public static void runAndWait(Runnable runnable) {
    tryRuntime(() -> {
      if (Platform.isFxApplicationThread()) {
        runnable.run();
      } else {
        FutureTask<Object> futureTask = new FutureTask<>(runnable, null);
        Platform.runLater(futureTask);
        futureTask.get();
      }
    });
  }
}
