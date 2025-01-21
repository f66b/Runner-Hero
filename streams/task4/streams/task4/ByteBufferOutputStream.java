package streams.task4;

public class ByteBufferOutputStream extends DynamicByteArrayOutputStream{
	private static class Chunk {
        final byte[] bytes;
        Chunk next;
        int position;

        Chunk(int size) {
            this.bytes = new byte[size];
            this.position = 0;
            this.next = null;
        }
    }

    private final int chunkSize;
    private Chunk head;    // First chunk in the list
    private Chunk tail;    // Current chunk being written to
    private int totalSize; // Total bytes written

    /**
     * Constructs a ByteBufferOutputStream with a specified chunk size.
     * @param chunkSize Size of each chunk buffer
     */
    public ByteBufferOutputStream(int chunkSize) {
        super(chunkSize, chunkSize);
        this.chunkSize = chunkSize;
        this.head = new Chunk(chunkSize);
        this.tail = head;
        this.totalSize = 0;
    }

    @Override
    public int available() {
        // If current chunk is full, create a new one
        if (tail.position >= chunkSize) {
            Chunk newChunk = new Chunk(chunkSize);
            tail.next = newChunk;
            tail = newChunk;
        }
        // Always return positive number since we can create new chunks
        return chunkSize - tail.position;
    }

    @Override
    public void write(byte value) {
        // Check if we need a new chunk before writing
        if (tail.position >= chunkSize) {
            available(); // This will create a new chunk
        }
        
        tail.bytes[tail.position++] = value;
        totalSize++;
    }

    @Override
    public byte[] getBytes() {
        byte[] result = new byte[totalSize];
        int offset = 0;
        Chunk current = head;
        
        while (current != null) {
            int size = current.next == null ? current.position : chunkSize;
            System.arraycopy(current.bytes, 0, result, offset, size);
            offset += size;
            current = current.next;
        }
        
        return result;
    }

    @Override
    public int getSize() {
        return totalSize;
    }

}
