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