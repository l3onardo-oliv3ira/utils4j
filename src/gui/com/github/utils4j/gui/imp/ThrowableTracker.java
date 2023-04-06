package com.github.utils4j.gui.imp;

import static com.github.utils4j.imp.Strings.emptyArray;
import static com.github.utils4j.imp.Strings.trim;
import static java.util.Arrays.stream;
import static java.util.Collections.newSetFromMap;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.github.utils4j.gui.IThrowableTracker;
import com.github.utils4j.imp.Args;
import com.github.utils4j.imp.Containers;
import com.github.utils4j.imp.Strings;

public class ThrowableTracker implements IThrowableTracker {

  private static final String BEGIN_TAG = "<code-response-fail>";

  private static final String END_TAG = "</code-response-fail>";
  
  public static ThrowableTracker DEFAULT = new ThrowableTracker(); 

  private final String beginTag, endTag;
  
  protected ThrowableTracker() {
    this(BEGIN_TAG, END_TAG);
  }

  protected ThrowableTracker(String beginTag, String endTag) {
    this.beginTag = Args.requireText(beginTag, "beginTag is empty");
    this.endTag = Args.requireText(endTag, "beginTag is empty");
  }
  
  public String mark(String message) {
    return beginTag + trim(message) + endTag;
  }
  
  private void unmark(Throwable throwable, Consumer<String> consumer) {
    Args.requireNonNull(consumer, "consumer is null");
    //preventy circular causes
    if (throwable != null) {
      traverse(throwable, consumer, newSetFromMap(new IdentityHashMap<Throwable, Boolean>()));
    }
  }

  private void traverse(Throwable throwable, Consumer<String> consumer, Set<Throwable> dejaVu) {
    while(throwable != null && !dejaVu.contains(throwable)){
      dejaVu.add(throwable);
      unmark(throwable.getMessage()).ifPresent(consumer);
      stream(throwable.getSuppressed()).forEach(t -> traverse(t, consumer, dejaVu));
      throwable = throwable.getCause();
    }
  }

  public Optional<String> unmark(String message) {
    message = trim(message);
    int s = message.indexOf(beginTag);
    if (s < 0)
      return Optional.empty();
    s += beginTag.length();
    int e = message.indexOf(endTag, s);
    if (e < 0) //this should never happen
      e = message.length();
    return Strings.optional(message.substring(s, e));
  }
  
  @Override
  public String[] track(Throwable cause) {
    if (cause == null)
      return emptyArray();
    Set<String> out = new HashSet<>();
    unmark(cause, out::add);
    return Containers.arrayOf(out);
  }
}
