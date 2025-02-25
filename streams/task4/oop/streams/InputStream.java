package oop.streams;

/*
 * This is the interface for an input stream of bytes.
 * 
 * @author Pr. Olivier Gruber.
 */
public interface InputStream {

  /**
   * @return the number of available bytes in this input stream.
   *         Returning 0 means that are no available bytes but 
   *         some might become available later, in which case
   *         a read operation would block.
   *         Returning -1 indicates the end of the stream,
   *         in which case a read operation would fail with
   *         illegal-state exception. 
   */
  public int available();
  
  /**
   * Reads the next byte from this input stream.
   * The method blocks until a byte is available or until
   * the end of the stream has been reached. 
   * @return the read byte
   * @throws an IllegalStateException if the end of the
   * stream has been reached.
   */
  public byte read();
  
}
