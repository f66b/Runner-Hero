package oop.utils.collections;

import oop.collections.ICollection;
import oop.collections.IList;

public class ArrayList implements IList{
	
	private static final int INITIAL_CAPACITY=32;
	private static final int GROWTH_FACTOR=32;
	private Object[] elements;
	private int size;
	  /**
	   * Constructs an empty list.
	   */
	  public ArrayList() {
		  elements = new Object[INITIAL_CAPACITY];
		  size=0;
	  }

	  /**
	   * Constructs a list, initialized with
	   * the elements from the given array.
	   */
	  public ArrayList(Object array[]) {
		  int i=0;
		  int new_lenght=INITIAL_CAPACITY+i*GROWTH_FACTOR;
		  if (array==null) {
			  throw new NullPointerException("array is null");
		  }
		  while(array.length>new_lenght) {
			  i++;
		  }
		  elements = new Object[new_lenght];
		  System.arraycopy(array, 0, elements, 0, array.length);
		  size=array.length;
		  
		  
	  }
	  
	  /**
	   * Constructs a list, initialized with
	   * the elements from the given list.
	   */
	  public ArrayList(ArrayList v) {
		  int i=0;
		  int new_lenght=INITIAL_CAPACITY+i*GROWTH_FACTOR;
		  if (v==null) {
			  throw new NullPointerException("list is null");
		  }
		  while(v.size>new_lenght) {
			  i++;
		  }
		  elements = new Object[new_lenght];
		  System.arraycopy(v, 0, elements, 0, v.size);
		  size=v.size;
	  }

	  /**
	   * Constructs a list, initialized with
	   * the elements from the given collection.
	   */
	  public ArrayList(ICollection c) {
		  int i=0;
		  int new_lenght=INITIAL_CAPACITY+i*GROWTH_FACTOR;
		  if (c==null) {
			  throw new NullPointerException("array in null");
		  }
		  while(c.length>new_lenght) {
			  i++;
		  }
		  elements = new Object[new_lenght];
		  ICollection.Iterator it = c.iterator();
		  size = 0;
		  while(it.hasNext()) {
			  elements[size++]=it.next;
		  }
	  }
	  @Override
	 public Object elementAt(int index) {
		  if(index<0 || index>=size) {
			  throw new IndexOutOfBoundsException("index not valid");
		  }
		  return elements[index];
	  }
	  
	  @Override
	  public Object updateAt(int index, Object niu) {
		  if(index<0 || index>=size) {
			  throw new IndexOutOfBoundsException("index not valid");
		  }
		  Object old_value=elements[index];
		  elements[index]=niu;
		  return old_value;
	  }
	 
	  private void ensureCapacity(int minCapacity) {
		  int i=0;
	        if (minCapacity > elements.length) {
	            int newCapacity = elements.length + i*GROWTH_FACTOR;
	            while (newCapacity < minCapacity) {
	                i++;
	            }
	            Object[] newElements = new Object[newCapacity];
	            System.arraycopy(elements, 0, newElements, 0, size);
	            elements = newElements;
	        }
	    }

	  
	  @Override
	  public void insertAt(int index, Object elem) {
		  if(index<0 ) {
			  throw new IndexOutOfBoundsException("index not valid");
		  }
		  if (index > size) {
	            ensureCapacity(index + 1);
	            size = index + 1;
	            elements[index] = elem;
	            return;
	        }

	        ensureCapacity(size + 1);
	        System.arraycopy(elements, index, elements, index + 1, size - index);
	        elements[index] = elem;
	        size++;
	  }
	  
	  @Override
	    public Object removeAt(int index) {
	        if (index < 0 || index >= size) {
	            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	        }
	        Object oldValue = elements[index];
	        int numMoved = size - index - 1;
	        if (numMoved > 0) {
	            System.arraycopy(elements, index + 1, elements, index, numMoved);
	        }
	        elements[--size] = null; // Let GC do its work
	        return oldValue;
	    }
	  
	  @Override
	    public boolean remove(Object elem) {
	        for (int i = 0; i < size; i++) {
	            if (elem == null ? elements[i] == null : elem.equals(elements[i])) {
	                removeAt(i);
	                return true;
	            }
	        }
	        return false;
	    }
	  
	  @Override
	    public boolean contains(Object elem) {
	        for (int i = 0; i < size; i++) {
	            if (elem == null ? elements[i] == null : elem.equals(elements[i])) {
	                return true;
	            }
	        }
	        return false;
	    }
	  
	  @Override
	    public void toArray(Object[] elems) {
	        if (elems == null) {
	            throw new NullPointerException("Target array cannot be null");
	        }
	        if (elems.length < size) {
	            throw new IllegalArgumentException("Target array too small");
	        }
	        System.arraycopy(elements, 0, elems, 0, size);
	    }
	  @Override
	  	public int lenght() {
		  return size;
	  }
	  
	  @Override
	    public ICollection.Iterator iterator() {
	        return new ArrayListIterator();
	    }
	  
	// Static inner class for Iterator implementation
	    private static class ArrayListIterator implements ICollection.Iterator {
	        private final ArrayList list;
	        private int cursor;

	        ArrayListIterator(ArrayList list) {
	            this.list = list;
	            this.cursor = 0;
	        }

	        @Override
	        public boolean hasNext() {
	            return cursor < list.size;
	        }

	        @Override
	        public Object next() {
	            if (!hasNext()) {
	                throw new IllegalStateException("No more elements");
	            }
	            return list.elements[cursor++];
	        }
	    } 
}
	  
	  
