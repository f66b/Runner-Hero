package oop.streams.wjs;

import oop.streams.InputStream;

public class WrappedJavaInputStream implements InputStream {

  java.io.InputStream m_is;

  public WrappedJavaInputStream(java.io.InputStream is) {
    m_is = is;
  }

  @Override
  public int available() {
	  throw new RuntimeException("NYI");
  }

  @Override
  public byte read() {
    throw new RuntimeException("NYI");
  }

}
