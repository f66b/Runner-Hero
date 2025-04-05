package oop.util.contacts;

import oop.collections.IList;
import oop.collections.IMap;
import oop.contacts.IContacts;
import oop.utils.collections.HashTable;
import oop.utils.collections.LinkedList;

public class Contacts implements IContacts{
	
    private final IMap contacts;

    public Contacts() {
        contacts = new HashTable();
    }

    private static class Name implements IName {
        private final String lastName;
        private final String firstName;

        public Name(String last, String first) {
            this.lastName = last != null ? last : "";
            this.firstName = first != null ? first : "";
        }

        
        @Override
        public String toString() {
            return value();
        }

        
        @Override
        public String last() {
            return lastName;
        }

        @Override
        public String first() {
            return firstName;
        }

        @Override
        public boolean equals(IValue o) {
            if (!(o instanceof IName)) return false;
            IName other = (IName) o;
            return lastName.equals(other.last()) && firstName.equals(other.first());
        }

        @Override
        public String value() {
            return firstName.isEmpty() ? lastName : lastName + " " + firstName;
        }
    }

    private static class PhoneNumber implements IPhoneNumber {
        private final int countryCode;
        private final String number;

        public PhoneNumber(int country, String number) {
            this.countryCode = country;
            this.number = number;
        }

        @Override
        public String toString() {
            return countryCode > 0 ? "(" + countryCode + ") " + number : number;
        }

        @Override
        public int country() {
            return countryCode;
        }

        @Override
        public String number() {
            return number;
        }

        @Override
        public boolean equals(IPhoneNumber o) {
            if (o == null) return false;
            if (countryCode == 0 || o.country() == 0) {
                return number.equals(o.number());
            }
            return countryCode == o.country() && number.equals(o.number());
        }

        @Override
        public boolean equals(IValue o) {
            return o instanceof IPhoneNumber && equals((IPhoneNumber) o);
        }

        @Override
        public String value() {
            return toString();
        }
    }

    private static class Value implements IValue {
        private final String value;

        public Value(String value) {
            this.value = value != null ? value : "";
        }

        @Override
        public String toString() {
            return value();
        }

        
        @Override
        public boolean equals(IValue o) {
            return o != null && value.equals(o.value());
        }

        @Override
        public String value() {
            return value;
        }
    }

    private static class Contact implements IContact {
        private final IMap fields;

        public Contact(IName name, IPhoneNumber phone) {
            fields = new HashTable();
            fields.put("name", name);
            fields.put("phone", phone);
        }

        @Override
        public IPhoneNumber phone() {
            return (IPhoneNumber) fields.get("phone");
        }

        @Override
        public IName name() {
            return (IName) fields.get("name");
        }

        @Override
        public IList.Iterator fields() {
            return fields.keys();
        }

        @Override
        public IValue field(String name) {
            return (IValue) fields.get(name);
        }

        @Override
        public void field(String name, IValue value) {
            fields.put(name, value);
        }
    }

    @Override
    public IContact get(IPhoneNumber phone) {
        IMap.Iterator it = contacts.iterator();
        while (it.hasNext()) {
            IContact contact = (IContact) it.next();
            if (contact.phone().equals(phone)) {
                return contact;
            }
        }
        return null;
    }
    
    private String normalizePhone(String phone) {
        // Apply the same normalization as in parsePhone
        return phone.replace('-', ' ').replace('.', ' ').replaceAll("  ", " ").trim();
    }
    
    @Override
    public void remove(IContact c) {
        // Find the matching key in the contacts map
        IMap.Iterator keys = contacts.keys();
        while (keys.hasNext()) {
            IMap keyMap = (IMap) keys.next();
            String phoneString = (String) keyMap.get("phone");
            String contactPhoneString = c.phone().value().toString();
            
            // Normalize phone strings before comparison
            String normalizedKeyPhone = normalizePhone(phoneString);
            String normalizedContactPhone = normalizePhone(contactPhoneString);
            
            if (normalizedKeyPhone.equals(normalizedContactPhone)) {
                contacts.remove(keyMap);
                return;
            }
        }
    }

    @Override
    public IContact add(IName name, IPhoneNumber phone) {
        IMap phoneMap = new HashTable();
        phoneMap.put("phone", phone.toString());
        
        if (contacts.contains(phoneMap)) {
            throw new IllegalArgumentException("Contact with phone " + phone + " already exists");
        }

        Contact contact = new Contact(name, phone);
        contacts.put(phoneMap, contact);
        return contact;
    }

    @Override
    public void update(IContact c, IList names, IList values) {
        if (names.length() != values.length()) {
            throw new IllegalArgumentException("Names and values lists must have same length");
        }

        for (int i = 0; i < names.length(); i++) {
            String name = (String) names.elementAt(i);
            IValue value = (IValue) values.elementAt(i);
            c.field(name, value);
        }
    }

    @Override
    public IList.Iterator select(String name, String filter) {
        IList results = new LinkedList();
        IMap.Iterator it = contacts.iterator();
        
        while (it.hasNext()) {
            IContact contact = (IContact) it.next();
            IValue fieldValue = contact.field(name);
            
            if (fieldValue != null && matchesFilter(fieldValue.value(), filter)) {
                results.insertAt(results.length(), contact);
            }
        }
        
        return results.iterator();
    }

    private boolean matchesFilter(String value, String filter) {
        if (filter.equals("*")) return true;
        
        if (filter.startsWith("*") && filter.endsWith("*")) {
            String substr = filter.substring(1, filter.length() - 1);
            return value.contains(substr);
        }
        
        if (filter.startsWith("*")) {
            String suffix = filter.substring(1);
            return value.endsWith(suffix);
        }
        
        if (filter.endsWith("*")) {
            String prefix = filter.substring(0, filter.length() - 1);
            return value.startsWith(prefix);
        }
        
        return value.equals(filter);
    }

    @Override
    public IName newName(String last, String first) {
        return new Name(last, first);
    }

    @Override
    public IPhoneNumber newPhoneNumber(int country, String number) {
        return new PhoneNumber(country, number);
    }

    @Override
    public IValue newValue(String value) {
        return new Value(value);
    }

}
