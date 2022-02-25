package com.github.utils4j.imp;

import java.awt.Image;
import java.io.InputStream;

import javax.swing.ImageIcon;

public interface IPicture {

  InputStream asStream();

  Image asImage();

  ImageIcon asIcon();

}