package oop.streams;

public class ByteArrayInputStream implements InputStream {
	private final byte[] buffer;
	private final int startOffset;
	private final int length;
	private int offset;

	/**
	 * Constructs a ByteArrayInputStream from a portion of a byte array.
	 */
	public ByteArrayInputStream(byte buffer[], int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > buffer.length) {
			throw new IllegalArgumentException("Invalid offset or length.");
		}
		this.buffer = buffer;
		this.startOffset = offset;
		this.length = length;
		this.offset = offset;
	}

	/**
	 * Constructs a ByteArrayInputStream from a byte array.
	 */
	public ByteArrayInputStream(byte buffer[]) {
		this(buffer, 0, buffer.length);
	}

	/**
	 * Constructs a ByteArrayInputStream from a ByteArrayOutputStream.
	 */
	public ByteArrayInputStream(ByteArrayOutputStream s) {
		this(s.getBytes(), 0, s.getSize());
	}

	@Override
	public int available() {
		int remaining = (startOffset + length) - offset;
		return remaining > 0 ? remaining : -1;
	}

	@Override
	public byte read() throws IllegalStateException {
		if (available() == -1) {
			throw new IllegalStateException("No more bytes available to read.");
		}
		return buffer[offset++];
	}
}
