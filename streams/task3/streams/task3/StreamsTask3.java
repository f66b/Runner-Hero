package streams.task3;

import java.io.EOFException;
import java.util.Random;

import streams.task2.step2.InputStream;
import streams.task2.step2.OutputStream;

public class StreamsTask3 {

  static public void main(String[] args) throws EOFException {
    System.out.println("StreamStep3...");
    test00();
    test01();
    test02();
    test03();
    System.out.println("That's all folks.");
    System.exit(0);
  }
  
  static void test00() {

    Random rand = new Random();
    OutputStream os = new OutputStream(32,16);
    DataOutputStream dos = new DataOutputStream(os);
    int size = rand.nextInt(1000) + 64;
    for (int i = 0; i < size; i++)
      dos.writeByte((byte)i);
    System.out.printf(" - wrote %d bytes\n",size);
    
    InputStream is = new InputStream(os); 
    int length = is.available();
    if (length != size)
      throw new Error("");
    DataInputStream dis = new DataInputStream(is);
    for (int i = 0; i < length; i++) {
      byte b = dis.readByte();
      if ((byte)i != b)
        throw new Error("");
    }
    System.out.printf(" - read %d bytes\n",length);
  }

  static void test01() {

    Random rand = new Random();
    OutputStream os = new OutputStream(32,16);
    DataOutputStream dos = new DataOutputStream(os);
    int size = rand.nextInt(1000) + 64;
    for (int i = 0; i < size; i++)
      dos.writeInt(i);
    System.out.printf(" - wrote %d integers\n",size);
    
    InputStream is = new InputStream(os); 
    DataInputStream dis = new DataInputStream(is);
    int length = is.available();
    if (length != 4*size)
      throw new Error("");
    for (int i = 0; i < size; i++) {
      int value = dis.readInt();
      if (i != value)
        throw new Error("");
    }
    System.out.printf(" - read %d integers\n",size);
  }

  static void test02() {

    Random rand = new Random();
    OutputStream os = new OutputStream(32,16);
    DataOutputStream dos = new DataOutputStream(os);
    int size = rand.nextInt(1000) + 64;
    for (int i = 0; i < size; i++)
      dos.writeFloat((float)i);
    System.out.printf(" - wrote %d floats\n",size);
    
    InputStream is = new InputStream(os); 
    DataInputStream dis = new DataInputStream(is);
    int length = is.available();
    if (length != 4*size)
      throw new Error();
    for (int i = 0; i < size; i++) {
      float value = dis.readFloat();
      if ((float)i != value)
        throw new Error();
    }
    System.out.printf(" - read %d floats\n",size);
  }

  static void test03() {

    OutputStream os = new OutputStream(16,32);
    DataOutputStream dos = new DataOutputStream(os);
    String lines[] = new String[] {
      "Un été à la plage.",
      "L'hiver à la montage.",
      "L'écologie et l'économie doivent s'associer à l'avenir."
    };
    int i;
    for (i=0;i<100;i++)
      dos.writeUTF(lines[i%3]);
    System.out.println(" - wrote "+i+" lines");
    
    InputStream is = new InputStream(os); 
    DataInputStream dis = new DataInputStream(is);
    for (i=0; !dis.endOfStream();i++) {
      String s = dis.readUTF();
      if (s==null ||  !s.equals(lines[i%3]))
        throw new Error();
    }
    System.out.println(" - read "+i+" lines");
  }

}
