package oop.contacts.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import oop.contacts.IContacts;
import oop.util.contacts.Contacts;

public class Main {

  /*
   * Default contact database file.
   */
  private static final String DATABASE_EXT = ".db";
  private static final String DATABASE_ARG = "-db=";

  /*
   * The entry point. It will start by loading the database file
   * of contacts, if it exists, then start the command line 
   * interpreter that allows the user to interact with the 
   * contact information. Upon the interpreter exiting,
   * the contact information will be saved in the same database file.
   */
  public static void main(String args[]) throws IOException {
    IContacts contacts = new Contacts();
    String path = null;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith(DATABASE_ARG))
        path = arg.substring(DATABASE_ARG.length());
    }

    Shell s;
    s = new Shell(contacts);
    
    if (path != null)
      load(s, path);

    InputStreamReader reader = new InputStreamReader(System.in);
    s.parse(reader,System.out);
  }

  /*
   * Loads a database file. 
   * Notice that the file is expected to be a sequence of commands,
   * as if they were typed by an end-user. This is another way to save
   * information, saving textual commands to recreate the information rather
   * than saving bytes.
   */
  private static void load(Shell shell, String path) throws IOException {
    PrintStream ps = new PrintStream(PrintStream.nullOutputStream());
    File database = new File(path + DATABASE_EXT);
    if (database.exists()) {
      FileInputStream is = new FileInputStream(database);
      InputStreamReader reader = new InputStreamReader(is);
      shell.parse(reader, ps);
      is.close();
    }
  }

}
