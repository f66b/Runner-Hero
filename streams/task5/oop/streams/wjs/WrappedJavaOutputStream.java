package oop.streams.wjs;

import oop.streams.OutputStream;

public class WrappedJavaOutputStream implements OutputStream {

  java.io.OutputStream m_os;
  
  public WrappedJavaOutputStream(java.io.OutputStream os) {
    m_os = os;
  }

  @Override
  public int available() {
    throw new RuntimeException("NYI");
  }

  @Override
  public void write(byte value) {
    throw new RuntimeException("NYI");
  }

}
