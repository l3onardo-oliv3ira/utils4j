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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import com.github.utils4j.IConstants;

public class SetupLicense {

  private SetupLicense() {}
  
  public static void main(String[] args) throws IOException {
    Path root = Paths.get("../");
    List<String> licenseLines = Files.readAllLines(Paths.get("./LICENSE"));
    try (Stream<Path> walk = Files.walk(root)) {        
      walk.map(Path::toFile)
        .filter(f -> f.getName().endsWith(".java"))
        .forEach(java -> {
          List<String> javaLines;
          try {
            javaLines = Files.readAllLines(java.toPath(), IConstants.UTF_8);
          } catch (IOException e1) {
            return;
          }
          if (javaLines.stream().anyMatch(l -> l.contains("MIT License")))
            return;
          File original = java;
          java.renameTo(java = new File(java.getParent(), java.getName() + ".bkp"));
          try(PrintWriter w = new PrintWriter(original, "UTF-8")) {
            w.println("/*");
            licenseLines.forEach(l -> w.println("* " + l));
            w.println("*/\n");
            javaLines.forEach(w::println);
            java.delete();
          } catch (Exception e) {
            java.renameTo(original);
          }
        });
    }
  }
}
