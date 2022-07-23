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


package com.github.utils4j.imp.function;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class SwitchConsumer<T> {

  private static <T> Predicate<T> testAndConsume(Predicate<T> pred, Consumer<T> cons) {
    return t -> {
      boolean result = pred.test(t);
      if (result) cons.accept(t);
      return result;
    };
  }

  private Predicate<T> conditionalConsumer;

  private SwitchConsumer(Predicate<T> pred) {
    conditionalConsumer = pred;
  }

  public static <C> SwitchConsumer<C> inCase(Predicate<C> pred, Consumer<C> cons) {
    return new SwitchConsumer<>(testAndConsume(pred, cons));
  }

  public SwitchConsumer<T> elseIf(Predicate<T> pred, Consumer<T> cons) {
    return new SwitchConsumer<>(conditionalConsumer.or(testAndConsume(pred,cons)));
  }

  public Consumer<T> elseDefault(Consumer<T> cons) {
    return testAndConsume(conditionalConsumer.negate(),cons)::test;
  }
}
