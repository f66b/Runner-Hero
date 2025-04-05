package oop.utils.collections;

import oop.collections.IMap;
import oop.collections.ICollection;

public class HashTable implements IMap{
	private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    private static class Entry {
        Object key;
        Object value;
        Entry next;
        
        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private Entry[] buckets;
    private int size;
    
    public HashTable() {
        buckets = new Entry[INITIAL_CAPACITY];
        size = 0;
    }
    
    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }
    
    private void resize() {
        Entry[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;
        
        for (Entry bucket : oldBuckets) {
            Entry current = bucket;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
    
    @Override
    public Object get(Object key) {
        int index = hash(key);
        Entry current = buckets[index];
        
        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        
        return null;
    }
    
    @Override
    public Object put(Object key, Object value) {
        if ((size + 1.0) / buckets.length > LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        Entry current = buckets[index];
        
        // Check if key already exists
        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                Object oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }
        
        // Add new entry at the beginning of the bucket
        Entry newEntry = new Entry(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        return null;
    }
    
    @Override
    public Object remove(Object key) {
        int index = hash(key);
        Entry current = buckets[index];
        Entry prev = null;
        
        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        
        return null;
    }
    
    @Override
    public boolean contains(Object key) {
        return get(key) != null;
    }
    
    @Override
    public void keysToArray(Object[] elems) {
        if (elems == null) {
            throw new NullPointerException("Target array cannot be null");
        }
        if (elems.length < size) {
            throw new IllegalArgumentException("Target array too small");
        }
        
        int index = 0;
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                elems[index++] = current.key;
                current = current.next;
            }
        }
    }
    
    @Override
    public void valuesToArray(Object[] elems) {
        if (elems == null) {
            throw new NullPointerException("Target array cannot be null");
        }
        if (elems.length < size) {
            throw new IllegalArgumentException("Target array too small");
        }
        
        int index = 0;
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                elems[index++] = current.value;
                current = current.next;
            }
        }
    }
    
    @Override
    public void toArray(Object[] elems) {
        valuesToArray(elems);
    }
    
    @Override
    public int length() {
        return size;
    }
    
    @Override
    public ICollection.Iterator keys() {
        return new HashTableIterator(true);
    }
    
    @Override
    public ICollection.Iterator values() {
        return new HashTableIterator(false);
    }
    
    @Override
    public ICollection.Iterator iterator() {
        return values();
    }
    
    private class HashTableIterator implements ICollection.Iterator {
        private int bucketIndex;
        private Entry current;
        private final boolean isKeyIterator;
        
        HashTableIterator(boolean isKeyIterator) {
            this.isKeyIterator = isKeyIterator;
            bucketIndex = 0;
            current = null;
            advanceToBucket();
        }
        
        private void advanceToBucket() {
            if (current != null && current.next != null) {
                current = current.next;
                return;
            }
            
            current = null;
            while (bucketIndex < buckets.length) {
                if (buckets[bucketIndex] != null) {
                    current = buckets[bucketIndex];
                    break;
                }
                bucketIndex++;
            }
        }
        
        @Override
        public boolean hasNext() {
            return current != null;
        }
        
        @Override
        public Object next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            
            Object result = isKeyIterator ? current.key : current.value;
            
            if (current.next != null) {
                current = current.next;
            } else {
                bucketIndex++;
                current = null;
                advanceToBucket();
            }
            
            return result;
        }
    }

}
