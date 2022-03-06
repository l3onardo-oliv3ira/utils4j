package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryRun;

import java.io.File;
import java.io.IOException;
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

public class FilePacker extends ThreadContext implements IFilePacker {
  
  private static final long TIMEOUT_TIMER = 500;

  private final Path folderWatching;

  private WatchService watchService;

  private List<File> pathPool = new LinkedList<File>();

  private final List<List<File>> availableFiles =  new LinkedList<>();
  
  private long startTime;
  
  public FilePacker(Path folderWatching) {
    super("shell-packer");
    this.folderWatching = Args.requireNonNull(folderWatching, "folderWatching is null");
  }
  
  @Override
  public void reset() {
    cleanFolder();
    synchronized(availableFiles) {
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
    Directory.requireFolder(folderWatching);
    watchService = FileSystems.getDefault().newWatchService();
    folderWatching.register(watchService,  StandardWatchEventKinds.ENTRY_CREATE);
  }
  
  private void uninstall() {
    closeService();
    reset();
    pathPool.clear();
  }

  private void cleanFolder() {
    tryRun(() -> Directory.clean(folderWatching, f -> f.isFile()));
  }

  private void closeService() {
    if (watchService != null) {
      tryRun(watchService::close);
      watchService = null;
    }
  }
  
  private void pack(File file) {
    if (!file.isDirectory()) {
      pathPool.add(file);
      startTime = System.currentTimeMillis();
    }
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
  protected void doRun() {
    WatchKey key;
    try {
      do {
        while((key = watchService.poll(250, TimeUnit.MILLISECONDS)) == null) {
          if (!pathPool.isEmpty() && hasTimeout()) {
            makeBlocksAvailable(block());
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
          makeBlocksAvailable(block());

        }finally {
          key.reset();
        }
        
      } while(true);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void makeBlocksAvailable(List<File> block) throws InterruptedException {
    synchronized(availableFiles) {
      availableFiles.add(block);
      availableFiles.notifyAll();
    }
  }
}