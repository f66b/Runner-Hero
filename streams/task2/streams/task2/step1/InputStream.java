package streams.task2.step1;

public class InputStream {
	private byte[] buffer;
	private int position;

	/**
	 * Constructs an input stream from an array of bytes. Do NOT copy the buffer,
	 * alias it.
	 */
	public InputStream(byte[] buff) {
		this.buffer = buff;
		this.position = 0;
	}

	/**
	 * Reads the next byte from this input stream. <br>
	 * Notice that this suggests a notion of a current position, where to read from.
	 * Of course, the current position needs to be advanced.
	 * 
	 * @return the read byte
	 */
	public byte read() {
		if (position >= buffer.length) {
			throw new RuntimeException("NYI");
		}
		return buffer[position++]; // Return the current byte and advance the position.
	}
}
