package streams.task4;

import oop.streams.OutputStream;
public class ByteArrayOutputStream implements OutputStream{
	private byte[] buffer;
	private int position;
	private final int delta;

	/**
	 * Constructs a ByteArrayOutputStream from a portion of a byte array.
	 */
	public ByteArrayOutputStream(byte buffer[], int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > buffer.length) {
			throw new IllegalArgumentException("Invalid offset or length.");
		}
		this.buffer = new byte[length];
		System.arraycopy(buffer, offset, this.buffer, 0, length);
		this.position = 0;
		this.delta = 64; // Default growth size
	}

	/**
	 * Constructs a ByteArrayOutputStream from a byte array.
	 */
	public ByteArrayOutputStream(byte buffer[]) {
		this(buffer, 0, buffer.length);
	}

	/**
	 * Constructs an output stream with a specific initial capacity and growth
	 * delta.
	 */
	public ByteArrayOutputStream(int capacity, int delta) {
		this.buffer = new byte[capacity];
		this.position = 0;
		this.delta = delta;
	}

	@Override
	public int available() {
		if (position >= buffer.length) {
			return -1;
		}
		return buffer.length - position ;
	}
	
	@Override
	public void write(byte value) throws IllegalStateException {
		if (available()==-1) {
			throw new IllegalStateException("no more bytes to read");
		}
		if (position >= buffer.length) {
			growBuffer();
		}
		buffer[position++] = value;
	}

	private void growBuffer() {
		byte[] newBuffer = new byte[buffer.length + delta];
		System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
		buffer = newBuffer;
	}

	public byte[] getBytes() {
		byte[] result = new byte[position];
		System.arraycopy(buffer, 0, result, 0, position);
		return result;
	}

	public void getBytes(byte[] bytes, int offset) {
		if (offset < 0 || offset + position > bytes.length) {
			throw new IllegalArgumentException("Invalid offset or insufficient space in destination array.");
		}
		System.arraycopy(buffer, 0, bytes, offset, position);
	}

	public int getSize() {
		return position;
	}

}
