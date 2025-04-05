package oop.contacts.shell;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import oop.collections.IList;

public class Parser {
  public interface Listener {
    void add(IList fields);

    void update(IList fields);

    void remove(IList fields);

    void select(IList fields);

    void malformed(IList words);

    void unrecognized(String cmd, IList args);

    void thrown(Throwable th);

    IList newList();
  }

  private InputStreamReader m_reader;
  private Listener m_listener;

  Parser(InputStreamReader reader, PrintStream ps, Listener listener) {
    m_reader = reader;
    if (ps == null)
      ps = new PrintStream(PrintStream.nullOutputStream());
    m_listener = listener;
    m_words = m_listener.newList();
  }

  /*
   * Run the command interpreter, parsing command lines
   * and executing the parsed commands.
   */
  public void parse() {
    int r;
    try {
      r = m_reader.read();
      while (r != -1) {
        if (!step((char) r))
          break;
        r = m_reader.read();
      }
    } catch (IOException ex) {
      m_listener.thrown(ex);
    }
  }

  private boolean _eval() {
    try {
      String cmd = (String) m_words.removeAt(0);
      if (cmd.equals("add")) {
        m_listener.add(m_words);
      } else if (cmd.equals("update")) {
        m_listener.update(m_words);
      } else if (cmd.equals("remove")) {
        m_listener.remove(m_words);
      } else if (cmd.equals("select")) {
        m_listener.select(m_words);
      } else if (cmd.equals("exit")) {
        return false;
      } else
        m_listener.unrecognized(cmd, m_words);
    } catch (Throwable ex) {
      m_listener.thrown(ex);
    }
    m_words = m_listener.newList();
    return true;
  }

  private static final int SEP = 0;
  private static final int WORD = 1;
  private static final int STR = 2; // string between ""
  private int m_state = SEP;
  private StringBuffer m_token;
  private IList m_words;

  private boolean step(char c) throws IOException {
    switch (c) {
    // spaces and tabs separate words.
    // in accordance to the manual page on the "word count" program.
    case ' ':
      _space();
      break;
    case '\t':
    case '\n':
      _sep();
      break;
    case ';':
      _sep();
      if (!_eval())
        return false;
      break;
    default:
      _char(c);
      break;
    }
    return true;
  }

  private void newWord() {
    String word = m_token.toString();
    m_words.insertAt(m_words.length(), word);
    m_token = null;
  }

  private void _space() {
    switch (m_state) {
    case SEP:
      break;
    case WORD:
      newWord();
      m_state = SEP;
      break;
    case STR:
      m_token.append(' ');
      break;
    }
  }

  private void _sep() {
    switch (m_state) {
    case SEP:
      break;
    case WORD:
    case STR:
      newWord();
      m_state = SEP;
      break;
    }
  }

  private void _char(char c) {
    switch (m_state) {
    case SEP:
      m_token = new StringBuffer();
      if (c == '"')
        m_state = STR;
      else {
        m_token.append(c);
        m_state = WORD;
      }
      break;
    case WORD:
      if (c == '"')
        m_state = STR;
      else
        m_token.append(c);
      break;
    case STR:
      if (c == '"')
        m_state = WORD;
      else
        m_token.append(c);
      break;
    }
  }

}
