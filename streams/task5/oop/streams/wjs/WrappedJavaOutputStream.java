package oop.streams.wjs;

import oop.streams.OutputStream;

public class WrappedJavaOutputStream implements OutputStream {

  java.io.OutputStream m_os;
  
  public WrappedJavaOutputStream(java.io.OutputStream os) {
    m_os = os;
  }

  @Override
  public int available() {
	  return Integer.MAX_VALUE;
  }

  @Override
  public void write(byte value) {
	  try {
	      m_os.write(value);
	    } catch (java.io.IOException e) {
	      throw new IllegalStateException("Error writing to output stream", e);
	    }
  }

}
