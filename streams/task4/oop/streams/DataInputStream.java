package oop.streams;

import java.io.IOException;
import java.util.Arrays;
public class DataInputStream {
	InputStream is;

	public DataInputStream(InputStream is) {
		this.is = is;

	}

	/**
	 * @return true if the end of the stream has been reached, false otherwise.
	 */
	public boolean endOfStream() throws IOException {
		return is.available() < 1;
	}

	/**
	 * @return a float value
	 * @throws IOException if an internal error occurs
	 */
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(byteToInt(readNBytes(4)));
	}

	/**
	 * @returns a signed integer value
	 * @throws IOException if an internal error occurs
	 */
	public int readInt() throws IOException {
		return byteToInt(readNBytes(4));
	}

	/**
	 * @returns a signed short value
	 * @throws IOException if an internal error occurs
	 */
	public short readShort() throws IOException {
		return byteToShort(readNBytes(2));
	}

	/**
	 * @returns a signed short value
	 * @throws IOException if an internal error occurs
	 */
	public byte readByte() throws IOException {
		return is.read();
	}

	/**
	 * @returns a boolean value.
	 * @throws IOException if an internal error occurs
	 */
	public boolean readBoolean() throws IOException {
		return byteToBoolean(is.read());
	}

	/**
	 * @returns a character
	 * @throws IOException if an internal error occurs
	 */
	public char readChar() throws IOException {
		return byteToChar(readNBytes(2));
	}

	/**
	 * @returns a string.
	 * @throws IOException if an internal error occurs
	 */
	public String readUTF() throws IOException {
		int size = readInt();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(readChar());
		}
		return sb.toString();
		
		/* StringBuilder : utile en cas d'opérations nombreuses sur le même string */
	}

	private byte[] readNBytes(int n) throws IOException {
		byte[] buffer = new byte[n];
		for (int i = 0; i < n; i++) {
			buffer[i] = is.read();
		}
		return buffer;
	}

	private short byteToShort(byte[] b) {
		int b1 = ((b[0]) << 8) & 0xff00;
		int b2 = ((b[1]) << 0) & 0x00ff;
		short s = (short) (b1 | b2);
		return s;
	}

	private int byteToInt(byte[] b) {
		byte[] b1 = Arrays.copyOfRange(b, 0, 2);
		byte[] b2 = Arrays.copyOfRange(b, 2, 4);
		short s1 = byteToShort(b1);
		short s2 = byteToShort(b2);
		int i1 = (s1 << 16) & 0xffff0000;
		int i2 = (s2 << 0) & 0x0000ffff;
		int i = i1 | i2;
		return i;
	}

	private char byteToChar(byte[] b) {
		return (char) byteToShort(b);
	}

	private boolean byteToBoolean(byte b) {
		return b == 1 ? true : false;
	}
}
