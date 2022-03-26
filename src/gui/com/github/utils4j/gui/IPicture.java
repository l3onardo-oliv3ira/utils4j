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


package com.github.utils4j.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.utils4j.imp.Throwables;

public interface IPicture {

  String path();
  
  default Optional<InputStream> asStream() {
    return Optional.ofNullable(getClass().getResourceAsStream(path()));
  }
  
  default Optional<Image> asImage() {
    return Optional.ofNullable(Toolkit.getDefaultToolkit().createImage(getClass().getResource(path())));
  }

  default Optional<ImageIcon> asIcon() {
    return Optional.ofNullable(new ImageIcon(getClass().getResource(path())));
  }

  default Optional<BufferedImage> asBuffer() {
    return Throwables.tryCall(() -> Optional.of(ImageIO.read(asStream().get())), Optional.empty());
  }
}
