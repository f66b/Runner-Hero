package streams.task2.step2;

import java.io.EOFException;

/*
 * This is an implementation of an input stream that
 * is based on the use of a byte array to hold the 
 * bytes that will be read via the method "read" on this stream. 
 * The close operation has not effect on this implementation,
 * it is a non-operation (no-op). 
 * 
 * @author Pr. Olivier Gruber.
 */

public class InputStream {

  /**
   * Constructs an input stream from the given output stream
   */
  public InputStream(OutputStream s) {
    // TODO
    throw new RuntimeException("NYI");
  }

  /**
   * @return the number of available bytes in this input stream.
   *         Returning 0 means that are no available bytes but 
   *         some might become available later.
   *         Returning -1 indicates the end of the stream.
   */
  public int available() {
    // TODO
    throw new RuntimeException("NYI");
  }
  
  /**
   * Reads the next byte from this input stream. <br>
   * 
   * @return the read byte
   * @throws IllegalStateException if there are no more byte to read
   */
  public byte read() {
    // TODO
    throw new RuntimeException("NYI");
  }
  
}
