package streams.task3;

import streams.task2.step2.OutputStream;

/**
 * This is an data output stream that wraps an output stream of bytes,
 * allowing to write different Java types such as integers, floats,
 * and strings. The companion class is the class DataInputStream.
 * 
 * @author Pr. Olivier Gruber.
 */

public class DataOutputStream {
  OutputStream os;
  
  public DataOutputStream(OutputStream os) {
    this.os = os;
  }

  /**
   * Writes the given double value.
   * Encoded over 8-bytes, with big-endian encoding.
   */
  public void writeDouble(double value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given float value.
   * Encoded over 4-bytes, with big-endian encoding.
   */
  public void writeFloat(float value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given long value.
   * Encoded over 8-bytes, with big-endian encoding.
   */
  public void writeLong(long value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given integer value
   * Encoded over 4-bytes, with big-endian encoding.
   */
  public void writeInt(int value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }
  
  /**
   * Writes the given short value
   * Encoded over 2-bytes, with big-endian encoding.
   */
  public void writeShort(short value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given byte value
   */
  public void writeByte(byte value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given boolean value
   * Encoded over 1-bytes.
   */
  public void writeBoolean(boolean value) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given character as UTF-8 encoded character. 
   */
  public void writeChar(char c) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

  /**
   * Writes the given string as a sequence of 
   * UTF-8 encoded characters. 
   */
  public void writeUTF(String s) {
    // TODO
    throw new RuntimeException("Not Yet Implemented");
  }

}
