package hello.greetings.tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import hello.greetings.Greetings;

public class TestGreetings {

  public static void main(String[] args) {
    System.out.println("Test0: ...");
    test0();
    System.out.println("  Done.");
    System.out.println("Test1: ...");
    test1();
    System.out.println("  Done.");
    System.out.println("Test2: ...");
    test2();
    System.out.println("  Done.");
    System.out.println("Congrats!");
  }

  static void test0() {

    String[] names = new String[] { "toto", "titi", "tata" };
    
    // Create a new print stream that we will use
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    // Calling your code, with our print stream 
    // and our names.
    Greetings.greetings(ps, names);

    // Checkout the output of your code.
    checkOutput(baos, names);
  }

  static void test1() {
    String[] strings = new String[] {};

    // Create a new print stream that we will use
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    // Calling your code, with our print stream 
    // and with no names.
    Greetings.greetings(ps, strings);

    // Checkout the output of your code.
    checkOutput(baos, strings);
  }

  static void test2() {
    // Create a new print stream that we will use
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    try {
      // Calling your code, with our print stream 
      // and no array of names. Your code is expected 
      // to resist to that.
      String[] strings = null;
      Greetings.greetings(ps, strings);
    } catch (NullPointerException ex) {
      ex.printStackTrace(System.err);
      System.err.println("  Your code has thrown a null-pointer exception.");
      System.err.println("  If you do, fix your code so that it does not happen.");
      System.exit(-1);
    }
  }

  static void checkOutput(ByteArrayOutputStream baos, String names[]) {
    // Transform the given output stream into a string
    // this is the string corresponding to what your code printed
    // via our print stream.
    String output = new String(baos.toByteArray());
    
    // now check that each of our names appears and 
    // appears only once.
    int count = 0;
    for (int j = 0; j < names.length; j++) {
      String word = names[j];
      if (word != null && output.contains(word)) {
        count++;
        names[j] = null;
      }
    }
    if (count != names.length) {
      String msg = "We launched your code with the arguments:\n";
      for (int j = 0; j < names.length; j++) 
        msg+="  "+names[j]+"\n";
      msg += "But we could not find them in the ouput\n";
      System.err.println(msg);
      System.exit(-1);
    }
  }
}
