package streams.task2.step2;

import java.io.EOFException;

/*
 * This is an implementation of an input stream that
 * is based on the use of a byte array to hold the 
 * bytes that will be read via the method "read" on this stream. 
 * The close operation has not effect on this implementation,
 * it is a non-operation (no-op). 
 * 
 * @author Pr. Olivier Gruber.
 */

public class InputStream {
	int size;

	byte[] buffer;
	int offset = 0;

	/**
	 * Constructs an input stream from the given output stream
	 */
	public InputStream(OutputStream s) {
		size = s.getSize();
		buffer = s.getBytes();
	}

	/**
	 * @return the number of available bytes in this input stream. Returning 0 means
	 *         that are no available bytes but some might become available later.
	 *         Returning -1 indicates the end of the stream.
	 */
	public int available() {
		if (size - offset == 0)
			return -1;
		return size - offset;
	}

	/**
	 * Reads the next byte from this input stream. <br>
	 * 
	 * @return the read byte
	 * @throws EOFException if there are no more byte to read
	 */
	public byte read() throws EOFException {
		if (available() > 0) {
			byte input = buffer[offset];
			offset++;
			return input;
		}
		throw new EOFException();
	}

}
