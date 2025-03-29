package oop.contacts.shell;

import java.io.InputStreamReader;
import java.io.PrintStream;

import oop.collections.ICollection;
import oop.collections.IList;
import oop.contacts.IContacts;
import oop.contacts.IContacts.IContact;
import oop.contacts.IContacts.IName;
import oop.contacts.IContacts.IPhoneNumber;
import oop.contacts.IContacts.IValue;
import oop.utils.collections.LinkedList;

public class Shell implements Parser.Listener {

  private IContacts m_contacts;
  private PrintStream m_ps;
  private Parser m_parser;

  Shell(IContacts contacts) {
    m_contacts = contacts;
  }

  void parse(InputStreamReader reader, PrintStream ps) {
    if (ps == null)
      ps = new PrintStream(PrintStream.nullOutputStream());
    m_ps = ps;
    prompt();
    m_parser = new Parser(reader, m_ps, this);
    m_parser.parse();
  }

  private String getValue(IList args, String key) {
    IList.Iterator i = args.iterator();
    while (i.hasNext()) {
      String arg = (String) i.next();
      int index = arg.indexOf("=");
      if (index == -1)
        continue;
      String name = arg.substring(0, index);
      if (name.equals(key)) {
        args.remove(arg);
        String val = arg.substring(index + 1);
        return val;
      }
    }
    return null;
  }

  @Override
  public void add(IList fields) {
    String n = getValue(fields, "name");
    String p = getValue(fields, "phone");
    if (p != null && n != null) {
      IName name = parseName(n);
      IPhoneNumber phone = parsePhone(p);
      IContact c = m_contacts.add(name, phone);
      update(c, fields);
    } else
      m_ps.println("Adding a contact requires a phone number and a name");
    prompt();
  }

  @Override
  public void update(IList fields) {    
    String p = getValue(fields, "phone");
    if (p != null) {
      IPhoneNumber phone = parsePhone(p);
      IContact c = m_contacts.get(phone);
      if (c != null)
        update(c, fields);
      else
        m_ps.println("Did not found a contact for phone=" + phone);
    } else
      m_ps.println("Updates require a phone number");
    prompt();
  }
  private void update(IContact c, IList fields) {    
    IList names = new LinkedList();
    IList values = new LinkedList();
    ICollection.Iterator i = fields.iterator();
    while (i.hasNext()) {
      String update = (String)i.next();
      parse(update,names,values);
    }
    m_contacts.update(c, names, values);
  }
  
  private void parse(String update, IList names, IList values) {
    int index = update.indexOf('=');
    if (index == -1)
      throw new IllegalArgumentException("invalid query: " + update);
    String name = update.substring(0, index);
    names.insertAt(names.length(), name);
    
    String value = update.substring(index + 1);
    IValue val;
    if (name.equals("name"))
      val = parseName(value);
    else if (name.equals("phone"))
      val = parsePhone(value);
    else
      val = m_contacts.newValue(value);
    values.insertAt(values.length(),val);    
  }
  
  @Override
  public void remove(IList fields) {
    String p = getValue(fields, "phone");
    if (p != null) {
      IPhoneNumber phone = parsePhone("phone");
      IContact c = m_contacts.get(phone);
      if (c != null)
        m_contacts.remove(c);
      else
        m_ps.println("Did not found a contact for phone=" + phone);
    } else
      m_ps.println("Removal requires a phone number");
    prompt();
  }

  @Override
  public void select(IList fields) {
    String arg = (String) fields.elementAt(0);
    int index = arg.indexOf("=");
    String field = arg.substring(0, index);
    String value = arg.substring(index + 1);
    IList.Iterator contacts = m_contacts.select(field, value);
    while (contacts.hasNext()) {
      IContact c = (IContact) contacts.next();
      print(c);
    }
    prompt();
  }

  @Override
  public IList newList() {
    return new LinkedList();
  }

  @Override
  public void malformed(IList words) {
    m_ps.print("Malformed input: ");
    IList.Iterator i = words.iterator();
    while (i.hasNext()) {
      String w = (String) i.next();
      m_ps.print(w);
      m_ps.print(" ");
    }
    m_ps.println("");
    prompt();
  }

  @Override
  public void thrown(Throwable th) {
    m_ps.print("The parsing failed: ");
    th.printStackTrace(m_ps);
    prompt();
  }

  @Override
  public void unrecognized(String cmd, IList args) {
    m_ps.println("Unrecognized command: " + cmd);
    IList.Iterator i = args.iterator();
    if (i.hasNext()) {
      m_ps.print(" args: ");
      while (i.hasNext()) {
        String w = (String) i.next();
        m_ps.print(w);
        m_ps.print(" ");
      }
      m_ps.println("");
    }
    prompt();
  }

  private void prompt() {
    m_ps.printf("\n> ");
  }

  private void print(IContact c) {
    m_ps.println(c.name());
    m_ps.println("  phone= " + c.phone());
    IList.Iterator fields = c.fields();
    while (fields.hasNext()) {
      String name = (String) fields.next();
      if (name.equals("phone") || name.equals("name"))
        continue;
      Object value = c.field(name);
      m_ps.println("  " + name + "= \"" + value + "\"");
    }
  }

  private IName parseName(String s) {
    if (s.contains("*"))
      throw new IllegalArgumentException("Bad name, '*' are not allowed.");
    int index = s.indexOf(' ');
    if (index == -1)
      return m_contacts.newName(s,null);
    String last = s.substring(0, index);
    String first = s.substring(index + 1);
    return m_contacts.newName(last.trim(), first.trim());
  }

  private IPhoneNumber parsePhone(String s) {
    int country = 0;
    StringBuffer number = new StringBuffer();
    s = s.trim();
    if (s.startsWith("(")) {
      int i = s.indexOf(')');
      country = Integer.parseInt(s.substring(1, i));
      s = s.substring(i + 1).trim();
    }
    s = s.replace('-', ' ');
    s = s.replace('.', ' ');
    while (true) {
      String t = s.replace("  ", " ");
      if (t.equals(s))
        break;
      s = t;
    }
    s = s.trim();
    return m_contacts.newPhoneNumber(country, s);
  }

}
