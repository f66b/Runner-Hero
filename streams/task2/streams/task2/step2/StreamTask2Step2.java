package streams.task2.step2;

public class StreamTask2Step2 {

  public static void main(String[] args) {
    System.out.println("StreamStep2...");
    OutputStream os = new OutputStream(32, 16);
    for (int i = 0; i < 500; i++)
      os.write((byte)i);
    InputStream is = new InputStream(os);
    for (int i = 0; i < 255; i++)
      if ((byte)i != is.read())
        throw new Error();
    System.out.println("That's all folks.");
  }

}
