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


package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.runQuietly;
import static com.sun.nio.file.SensitivityWatchEventModifier.HIGH;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.utils4j.IFilePacker;

public class FilePacker<E extends Exception> extends ThreadContext<E> implements IFilePacker<E> {
  
  private static final long TIMEOUT_TIMER = 2000;

  private long startTime;
  
  private final File lockFile;
  
  private final Path folderWatching;
  
  private WatchService watchService;

  private RandomAccessFile lock = null;

  private List<File> pathPool = new LinkedList<File>();

  private final List<List<File>> availableFiles =  new LinkedList<>();

  public FilePacker(Path folderWatching) {
    super("shell-packer");
    this.folderWatching = Args.requireNonNull(folderWatching, "folderWatching is null");
    this.lockFile = folderWatching.resolve(".lock").toFile();
  }
  
  @Override
  public void reset() {
    cleanFolder();
    synchronized(availableFiles) {
      availableFiles.forEach(List::clear);
      availableFiles.clear();
    }
  }

  @Override
  protected void beforeRun() throws IOException {
    install();
  }

  @Override
  protected void afterRun() {
    uninstall();
  }
  
  private void install() throws IOException {
    Directory.mkDir(folderWatching);
    lockFile.createNewFile();
    lock = new RandomAccessFile(lockFile, "r");
    watchService = FileSystems.getDefault().newWatchService();
    folderWatching.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE}, HIGH);
  }
  
  private void uninstall() {
    closeService();
    reset();
    releaseLock();
    pathPool.clear();
  }

  private void cleanFolder() {
    runQuietly(() -> Directory.rmDir(folderWatching, f -> f.isFile() && !f.equals(lockFile)));
  }

  private void closeService() {
    if (watchService != null) {
      runQuietly(watchService::close);
      watchService = null;
    }
  }
  
  private void releaseLock() {
    if (lock != null) {
      runQuietly(lock::close);
      lockFile.delete();      
      lock = null;
    }
  }
  
  private void pack(File file) {
    if (!file.isDirectory()) {
      startTime = System.currentTimeMillis();
      pathPool.add(file);
      onPacked(file);
    }
  }

  protected void onPacked(File file) {
  }

  @Override
  public List<File> filePackage() throws InterruptedException {
    checkIsAlive();
    synchronized(availableFiles) {
      while (availableFiles.isEmpty()) {
        availableFiles.wait();
      }
      return availableFiles.remove(0);
    }
  }
  
  private List<File> block() {
    List<File> newBlock = pathPool;
    pathPool = new LinkedList<File>();
    return newBlock;
  }
  
  private boolean hasTimeout() {
    return System.currentTimeMillis() - startTime > TIMEOUT_TIMER;
  }

  @Override
  protected void doRun() throws Exception {
    WatchKey key;
    do {
      while((key = watchService.poll(250, TimeUnit.MILLISECONDS)) == null) {
        if (!pathPool.isEmpty() && hasTimeout()) {
          offer(block());
        }
      }
      try {
        for (WatchEvent<?> e : key.pollEvents()) {
          if (e.count() <= 1) {
            @SuppressWarnings("unchecked")
            final WatchEvent<Path> event = (WatchEvent<Path>) e;
            final Path folder = (Path)key.watchable();
            final Path fileName = event.context();
            final Path file = folder.resolve(fileName);
            pack(file.toFile());
          }         
        }
       
        if (pathPool.isEmpty() || !hasTimeout()) {
          continue;
        }
        
        offer(block());

      }finally {
        key.reset();
      }
      
    } while(true);
  }

  public final void offer(List<File> block) throws InterruptedException {
    if (block != null) {
      synchronized(availableFiles) {
        availableFiles.add(block);
        availableFiles.notifyAll();
        onAvailable(block);
      }
    }
  }

  protected void onAvailable(List<File> block) {
    
  }
}
