package streams.task3;

import java.io.IOException;

import streams.task2.step2.OutputStream;

/**
 * This is an data output stream that wraps an output stream of bytes, allowing
 * to write different Java types such as integers, floats, and strings. The
 * companion class is the class DataInputStream.
 * 
 * @author Pr. Olivier Gruber.
 */

public class DataOutputStream {
	OutputStream os;

	public DataOutputStream(OutputStream os) {
		this.os = os;
	}

	/**
	 * Writes the given float value.
	 * 
	 * @throws IOException if an internal error occurs
	 */
	public void writeFloat(float value) throws IOException {
		int ivalue = Float.floatToIntBits(value);
		writeInt(ivalue);
	}

	/**
	 * Writes the given integer value
	 */
	public void writeInt(int value) throws IOException {
		byte[] toWrite = intToByte(value);
		os.write(toWrite[0]);
		os.write(toWrite[1]);
		os.write(toWrite[2]);
		os.write(toWrite[3]);
	}

	/**
	 * Writes the given short value
	 */
	public void writeShort(short value) throws IOException {
		byte[] toWrite = shortToByte(value);
		os.write(toWrite[0]);
		os.write(toWrite[1]);
	}

	/**
	 * Writes the given byte value
	 */
	public void writeByte(byte value) throws IOException {
		os.write(value);
	}

	/**
	 * Writes the given boolean value
	 */
	public void writeBoolean(boolean value) throws IOException {
		os.write(booleanToByte(value));
	}

	/**
	 * Writes the given character
	 */
	public void writeChar(char c) throws IOException {
		writeShort((short) c);
	}

	/**
	 * Writes the given string
	 */
	public void writeUTF(String s) throws IOException {
		writeInt(s.length());
		for (int i = 0; i < s.length(); i++) {
			writeChar(s.charAt(i));
		}
	}

	private byte booleanToByte(boolean b) {
		return b ? (byte) 1 : 0;
	}

	private byte[] shortToByte(short s) {
		byte b1 = (byte) ((s >> 8) & 0xff);
		byte b2 = (byte) ((s >> 0) & 0xff);
		byte[] bytes = new byte[2];
		bytes[0] = b1;
		bytes[1] = b2;
		return bytes;
	}

	private byte[] intToByte(int i) {
		short s1 = (short) ((i >> 16) & 0xffff);
		short s2 = (short) ((i >> 0) & 0xffff);
		byte[] b12 = shortToByte(s1);
		byte[] b34 = shortToByte(s2);
		byte[] bytes = new byte[4];
		bytes[0] = b12[0];
		bytes[1] = b12[1];
		bytes[2] = b34[0];
		bytes[3] = b34[1];
		return bytes;
	}

}
