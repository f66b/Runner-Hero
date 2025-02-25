package streams.task4;

public class DynamicByteArrayOutputStream extends ByteArrayOutputStream {
	 private final int growthDelta;

	    /**
	     * Constructs an output stream with an initial capacity and growth delta.
	     * @param initialCapacity Initial size of the internal buffer
	     * @param growthDelta Amount to grow the buffer by when full
	     */
	    public DynamicByteArrayOutputStream(int initialCapacity, int growthDelta) {
	        super(initialCapacity, growthDelta);
	        this.growthDelta = growthDelta;
	    }

	    @Override
	    public int available() {
	        // We should grow the buffer when there's no space left
	        if (position >= buffer.length) {
	            byte[] newBuffer = new byte[buffer.length + growthDelta];
	            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
	            buffer = newBuffer;
	        }
	        // Now we always have space available
	        return buffer.length - position;
	    }

	    @Override
	    public void write(byte value) {
	        // Make sure we have space before writing
	        if (available() <= 0) {
	            throw new IllegalStateException("Buffer is full and cannot grow further");
	        }
	        buffer[position++] = value;
	    }

	    @Override
	    public byte[] getBytes() {
	        byte[] result = new byte[position];
	        System.arraycopy(buffer, 0, result, 0, position);
	        return result;
	    }

	    @Override
	    public int getSize() {
	        return position;
	    }
}
