package streams.task2.step2;

/*
 * This is an implementation of an output stream that
 * is based on the use of a byte array to hold the 
 * bytes that will be written via the method "write" on this stream. 
 * The method "write" must never fail, meaning the internal array
 * must be grown as necessary.
 * The close operation has not effect on this implementation,
 * it is a non-operation (no-op). 
 * 
 * @author Pr. Olivier Gruber.
 */

public class OutputStream {
	private byte[] buffer; // Dynamic byte array
	private int position; // Current writing position
	private final int delta;

	/**
	 * Constructs an output stream with an initial capacity of the given number of
	 * bytes.
	 * 
	 * @param capacity is the initial length of the byte array
	 * @param delta    is the number of bytes to grow the array by.
	 */
	public OutputStream(int capacity, int delta) {
		this.buffer = new byte[capacity];
		this.position = 0;
		this.delta = delta;
	}

	/**
	 * Returns an array containing the bytes written to this output stream.
	 */
	public byte[] getBytes() {
		byte[] result = new byte[position]; // Only return the bytes written
		System.arraycopy(buffer, 0, result, 0, position);
		return result;
	}

	/**
	 * @return the number of bytes written to this output stream,
	 */
	public int getSize() {
		return position;
	}

	/**
	 * Writes the given byte into this stream. This method must never fail, the
	 * array must be grown if full, by adding 64 bytes each time the array is grown.
	 */
	public void write(byte value) {
		if (position >= buffer.length) {
			growBuffer(); // Grow the buffer if it's full
		}
		buffer[position++] = value;
	}

	private void growBuffer() {
		byte[] newBuffer = new byte[buffer.length + delta];
		System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
		buffer = newBuffer; // Update the buffer reference
	}
}
