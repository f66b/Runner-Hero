package streams.task3;

import streams.task2.step2.InputStream;

/**
 * This is an data input stream that wraps an input stream of bytes,
 * allowing to read different Java types such as integers, floats,
 * and strings. The companion class is the class DataOutputStream.
 * 
 * @author Pr. Olivier Gruber.
 */

public class DataInputStream {
  InputStream is;

  DataInputStream(InputStream is) {
    this.is = is;
  }

  /**
   * @return true if the end of the stream has been reached,
   *         false otherwise.
   */
  public boolean endOfStream() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @return a double value
   * that was encoded over 8-bytes, with big-endian encoding.
   */
  public double readDouble() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @return a float value
   * that was encoded over 4-bytes, with big-endian encoding.
   */
  public float readFloat() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @return a long value
   * that was encoded over 8-bytes, with big-endian encoding.
   */
  public long readLong() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @returns a signed integer value
   * that was encoded over 4-bytes, with big-endian encoding.
   */
  public int readInt() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @returns a signed short value
   * that was encoded over 2-bytes, with big-endian encoding.
   */
  public short readShort() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @returns a signed byte value
   */
  public byte readByte() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * @returns a boolean value.
   * Encoded over 1-bytes.
   */
  public boolean readBoolean() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Reads a UTF-8 encoded character 
   * @return the read character
   * @throws IllegalStateException if the next bytes
   *         cannot be decoded as a utf8-encoded character.
   */
  public char readChar() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Reads a string of UTF-8 encoded characters. 
   * @return the read string.
   * @throws IllegalStateException if the next bytes
   *         cannot be decoded as a utf8-encoded character.
   */
  public String readUTF() {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

}
