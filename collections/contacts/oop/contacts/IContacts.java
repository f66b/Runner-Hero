package oop.contacts;

import oop.collections.IList;

/*
 * This is the interface to define the management of contacts,
 * typical of the contact management found in our smart phones.
 * There is the model part of that application, managing the
 * contact information using object, there is no graphical user 
 * interface. 
 */
public interface IContacts {

  public interface IValue {
    /*
     * @returns true if two values are equal, 
     *          otherwise false.
     */
    boolean equals(IValue o);
   
    String value();
  }

  /*
   * An interface modeling a simplified name of a person,
   * modeled as a last name and a first name, 
   * which is a simplification of what can a person's name
   * be in real life.
   * When built from a string, a person's name is supposed
   * to be given as "last first", that is, the last name 
   * and then the first name.  
   */
  public interface IName extends IValue {
    /*
     * Returns the last name
     */
    String last();
    /*
     * Returns the first name
     */
    String first();
  }

  /*
   * An interface modeling a simplified phone number, 
   * which in real life is a complicated object because
   * of the many format around the world. 
   * 
   * A phone object is built from a string, that can have many formats. 
   * We apply the following simple cleaning-up rules:
   * 
   *   - Any '-' is replaced with ' '
   *   - Any '.' is replaced with ' '
   *   - Any '  ' is replaced with ' '
   *   
   * If there is a country number, it is given between parenthesis, 
   * like this: 
   *   (1) 212 555 1212 
   *   (33) 06 34 56 78 90
   */
  public interface IPhoneNumber extends IValue {
    /*
     * Returns the phone number as complete string:
     *   String s= "("+country()+") "+local();
     */
    String toString();
    
    /*
     * Returns the country code. 
     */
    int country();
    
    /*
     * Returns the local part of the phone number
     * as an long, only retaining the number digits,
     * it may be used to dialup this number.  
     */
    String number();
    
    /*
     * @returns true if two phone numbers are equal, 
     *          otherwise false.
     * The normal value-equality case:
     *   - same country codes (compared as integers)
     *   - same local numbers (compared as strings)
     * But if any of the two phone numbers does not have 
     * a country code, then only the local numbers must be 
     * compared, as strings.
     */
    boolean equals(IPhoneNumber o);
  }
  
  /*
   * A contact is defined at least by a name and a phone number,
   * phone numbers being unique but not names. Names can be 
   * any string, but without any '*'.
   * 
   * A contact may also have other fields than name and phone,
   * each field being a pair (name,value). Each field must have
   * a unique name per contact.
   */
  public interface IContact {
    /*
     * @returns the primary phone number of this contact
     *          corresponding to the field named "phone"
     */
    public IPhoneNumber phone();

    /*
     * @returns the name of this contact
     *          corresponding to the field named "name"
     */
    public IName name();

    /*
     * @returns an iterator on the field names, 
     */
    public IList.Iterator fields();

    /*
     * @returns the value of the field that has the
     *          given name. 
     */
    public IValue field(String name);

    /*
     * Updates the value of the field that has the
     * given name, with the given value.
     */
    public void field(String name, IValue value);
  }

  /*
   * @returns the contact that has the given name
   */
  IContact get(IPhoneNumber phone);

  /*
   * Removes the given contact. 
   */
  void remove(IContact c);

  /*
   * Adds a new contact with the given name and phone number
   * @returns the newly contact 
   * @throws IllegalArgumentException if there is already a contact
   *         with the given phone. 
   */
  IContact add(IName name, IPhoneNumber phone);

  /*
   * Applies the given updates on the given contact.
   * The two lists must be of the same length, obviously.
   * The list of names is a list of java.lang.String.
   * The list of values is a list of oop.contacts.IName.
   */
  void update(IContact c, IList names, IList values);
//
//  /*
//   * Applies the given update on the given contact.
//   * The update is a string formated as name=value
//   * where the name is the name of a field and the
//   * value is the value that field must be set to. 
//   * If there is no field with the given name, a new
//   * field is added to the given contact.
//   * @throws IllegalArgumentException if the update 
//   *         syntax is malformed.
//   */
//  void update(IContact c, String update);

  /*
   * This is a method that select contacts that
   * have a field with the given name and whose value 
   * matches the given filter.
   * For each field that has the given name,
   * its value is said to match the given filter
   * in the following cases:
   *   - If the filter is a plain value, containing no '*'
   *     the field matches the filter if 
   *     the field value is value-equal to the filter.
   *   - If the filter is a prefix filter, ending with an '*',
   *     the field matches the filter if 
   *     the field value starts with to the given filter,
   *     without the '*'.
   *   - If the filter is a suffix filter, starting with an '*',
   *     the field matches the filter if 
   *     the field value ends with to the given filter,
   *     without the '*'.
   *   - If the filter is a substring filter, 
   *     starting and ending with an '*',
   *     the field matches the filter if 
   *     the field value contains the given filter,
   *     without the two '*'.
   *   - If the filter is "*", 
   *     the field is always a match.
   */
  IList.Iterator select(String name, String filter);

  /*
   * Factory methods for values.
   */  
  IName newName(String last, String first);
  IPhoneNumber newPhoneNumber(int country, String number);
  IValue newValue(String value);
  
  
}