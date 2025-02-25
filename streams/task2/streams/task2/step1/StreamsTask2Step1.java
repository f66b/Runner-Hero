package streams.task2.step1;

public class StreamsTask2Step1 {

  static public void main(String[] args) {
    byte[] buffer = new byte[256];
    OutputStream os = new OutputStream(buffer);
    for (int i = 0; i < 255; i++)
      os.write((byte)i);
    InputStream is = new InputStream(buffer);
    for (int i = 0; i < 255; i++)
      if ((byte)i != is.read())
        throw new Error();
    System.out.println("That's all folks.");
  }

}
