package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryRun;

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

  private final Path folderWatching;
  
  private WatchService watchService;

  private List<File> pathPool = new LinkedList<File>();

  private final List<List<File>> availableFiles =  new LinkedList<>();
  
  private long startTime;
  
  private final File lockFile;
  
  private RandomAccessFile lock = null;
  
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
    Directory.requireFolder(folderWatching);
    lockFile.createNewFile();
    lock = new RandomAccessFile(lockFile, "r");
    watchService = FileSystems.getDefault().newWatchService();
    folderWatching.register(watchService,  StandardWatchEventKinds.ENTRY_CREATE);
  }
  
  private void uninstall() {
    closeService();
    reset();
    releaseLock();
    pathPool.clear();
  }

  private void cleanFolder() {
    tryRun(() -> Directory.clean(folderWatching, f -> f.isFile() && !f.equals(lockFile)));
  }

  private void closeService() {
    if (watchService != null) {
      tryRun(watchService::close);
      watchService = null;
    }
  }
  
  private void releaseLock() {
    if (lock != null) {
      tryRun(lock::close);
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
  }

  private void makeBlocksAvailable(List<File> block) throws InterruptedException {
    synchronized(availableFiles) {
      availableFiles.add(block);
      availableFiles.notifyAll();
      onAvailable(block);
    }
  }

  protected void onAvailable(List<File> block) {
    
  }
}