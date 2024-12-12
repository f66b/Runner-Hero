package oop.streams;

/*
 * This is the interface for an output stream of bytes.
 * 
 * @author Pr. Olivier Gruber.
 */
public interface OutputStream {

  /**
   * @return the number of bytes available to be written.
   *         Returning 0 means that are no available bytes,
   *         but some might become available later, in which case
   *         a write operation would block.
   *         Returning -1 indicates the end of the stream,
   *         in which case a write operation would fail with
   *         illegal-state exception. 
   */
  public int available();

  /**
   * Writes the given byte into this stream if there is room
   * to do so or blocks until there is room to do so.
   * @throws an IllegalStateException if the end of the
   * stream has been reached.
   */
  public void write(byte value);
}
