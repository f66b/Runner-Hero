package oop.utils.collections;

import oop.collections.ICollection;
import oop.collections.IList;

public class LinkedList implements IList{
	
	private static class Node{
		Object data;
		Node next;
		Node (Object data){
			this.data=data;
			this.next=null;
		}
	}
	
	private Node head;
	private Node tail;
	private int size;
	
	/**
	   * Constructs an empty list.
	   */
	  public LinkedList() {
		  head=null;
		  tail=null;
		  size=0;
	  }

	  /**
	   * Constructs a list, initialized with
	   * the elements from the given array.
	   */
	  
	  private void addToEnd(Object data) {
		  Node new_node=new Node(data);
		  if (tail == null) {
	            head = tail = new_node;
	        } else {
	            tail.next = new_node;
	            tail = new_node;
	        }
	        size++;
	  }
	  public LinkedList(Object array[]) {
		  if (array == null) {
			  throw new NullPointerException("invalid array");  
		  }
		  for(Object element :array) {
			  addToEnd(element);
		  }
		  
	  }
	  
	  /**
	   * Constructs a list, initialized with
	   * the elements from the given list.
	   */
	  public LinkedList(LinkedList v) {
		  if (v == null) {
			  throw new NullPointerException("invalid linked_list");  
		  }
		  Node current = v.head;
	        while (current != null) {
	            addToEnd(current.data);
	            current = current.next;
	        }
	  }

	  /**
	   * Constructs a list, initialized with
	   * the elements from the given collection.
	   */
	  public LinkedList(ICollection c) {
		  if (c == null) {
			  throw new NullPointerException("invalid linked_list");  
		  }
		  ICollection.Iterator it = c.iterator();
	        while (it.hasNext()) {
	            addToEnd(it.next());
	        }
	  }
	  
	  @Override
	    public Object elementAt(int index) {
	        if (index < 0 || index >= size) {
	            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	        }

	        Node current = head;
	        for (int i = 0; i < index; i++) {
	            current = current.next;
	        }
	        return current.data;
	    }

	    @Override
	    public Object updateAt(int index, Object niu) {
	        if (index < 0 || index >= size) {
	            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	        }

	        Node current = head;
	        for (int i = 0; i < index; i++) {
	            current = current.next;
	        }
	        Object oldValue = current.data;
	        current.data = niu;
	        return oldValue;
	    }

	    @Override
	    public void insertAt(int index, Object elem) {
	        if (index < 0) {
	            throw new IndexOutOfBoundsException("invalid index (negative)");
	        }

	        if (index >= size) {
	            for (int i = size; i <= index; i++) {
	                addToEnd(i == index ? elem : null);
	            }
	            return;
	        }

	        Node newNode = new Node(elem);
	        if (index == 0) {
	            newNode.next = head;
	            head = newNode;
	            if (tail == null) {
	                tail = head;
	            }
	        } else {
	            Node current = head;
	            for (int i = 0; i < index - 1; i++) {
	                current = current.next;
	            }
	            newNode.next = current.next;
	            current.next = newNode;
	        }
	        size++;
	    }

	    @Override
	    public Object removeAt(int index) {
	        if (index < 0 || index >= size) {
	            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	        }

	        Object removedData;
	        if (index == 0) {
	            removedData = head.data;
	            head = head.next;
	            if (head == null) {
	                tail = null;
	            }
	        } else {
	            Node current = head;
	            for (int i = 0; i < index - 1; i++) {
	                current = current.next;
	            }
	            removedData = current.next.data;
	            current.next = current.next.next;
	            if (current.next == null) {
	                tail = current;
	            }
	        }
	        size--;
	        return removedData;
	    }

	    @Override
	    public boolean remove(Object elem) {
	        if (head == null) {
	            return false;
	        }

	        if (head.data == elem || (head.data != null && head.data.equals(elem))) {
	            head = head.next;
	            if (head == null) {
	                tail = null;
	            }
	            size--;
	            return true;
	        }

	        Node current = head;
	        while (current.next != null) {
	            if (current.next.data == elem || 
	                (current.next.data != null && current.next.data.equals(elem))) {
	                current.next = current.next.next;
	                if (current.next == null) {
	                    tail = current;
	                }
	                size--;
	                return true;
	            }
	            current = current.next;
	        }
	        return false;
	    }

	    @Override
	    public boolean contains(Object elem) {
	        Node current = head;
	        while (current != null) {
	            if (current.data == elem || 
	                (current.data != null && current.data.equals(elem))) {
	                return true;
	            }
	            current = current.next;
	        }
	        return false;
	    }

	    @Override
	    public void toArray(Object[] elems) {
	        if (elems == null) {
	            throw new NullPointerException("invalid target array");
	        }
	        if (elems.length < size) {
	        	elems = new Object[size];
	        }

	        Node current = head;
	        for (int i = 0; i < size; i++) {
	            elems[i] = current.data;
	            current = current.next;
	        }
	    }

	    @Override
	    public int length() {
	        return size;
	    }

	    @Override
	    public ICollection.Iterator iterator() {
	        return new LinkedListIterator(this);
	    }

	 
	    private static class LinkedListIterator implements ICollection.Iterator {
	        private Node current;

	        LinkedListIterator(LinkedList list) {
	            this.current = list.head;
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
	            Object data = current.data;
	            current = current.next;
	            return data;
	        }
	    }

}
