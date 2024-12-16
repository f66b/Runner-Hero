package streams.task4;

import streams.task4.ByteArrayInputStream;
import streams.task4.ByteArrayOutputStream;

public class StreamTest {
    public static void main(String[] args) {
        // Step 1: Test ByteArrayOutputStream
        System.out.println("Testing ByteArrayOutputStream...");
        ByteArrayOutputStream output = new ByteArrayOutputStream(8, 64);

        // Write some bytes to the output stream
        for (int i = 0; i < 20; i++) {
            output.write((byte) (i + 1));
            System.out.println("Wrote: " + (i + 1));
        }

        // Verify buffer growth
        System.out.println("Output stream size: " + output.getSize());
        System.out.println("Bytes written to the stream: ");
        for (byte b : output.getBytes()) {
            System.out.print(b + " ");
        }
        System.out.println("\n");

        // Step 2: Test ByteArrayInputStream
        System.out.println("Testing ByteArrayInputStream...");
        ByteArrayInputStream input = new ByteArrayInputStream(output);

        // Read all bytes from the input stream
        try {
            while (input.available() > 0) {
                byte data = input.read();
                System.out.println("Read: " + data);
            }

            // Try reading beyond the end of the stream
            System.out.println("Attempting to read past the end of the stream...");
            input.read(); // This should throw an exception
        } catch (IllegalStateException ex) {
            System.out.println("Caught expected exception: " + ex.getMessage());
        }

        // Final confirmation
        System.out.println("Stream test completed successfully.");
    }
}

