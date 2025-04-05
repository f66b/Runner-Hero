package oop.collections.tests;

import oop.collections.IList;
import oop.collections.ICollection;
import java.util.ArrayList;
import java.util.List;

public abstract class ListTest {
    protected final String implementationName;
    protected IList list;  // Changed from final to allow recreation
    protected final List<TestResult> results;

    public ListTest(String implementationName, IList list) {
        this.implementationName = implementationName;
        this.list = list;
        this.results = new ArrayList<>();
    }

    protected abstract IList createEmptyList();  // New method to create empty list

    public List<TestResult> runTests() {
        results.clear();
        
        try {
            testEmptyConstructor();
            testArrayConstructor();
            testInsertAndRemove();
            testIterator();
            testContainsAndRemoveElement();
            testGrowth();
            testToArray();
        } catch (Exception e) {
            addResult(
                "Unexpected Error",
                false,
                "Exception occurred: " + e.getMessage()
            );
        }
        
        return results;
    }

    protected void resetList() {
        list = createEmptyList();  // Reset list before each test
    }

    protected void addResult(String testName, boolean condition, String errorMessage) {
        results.add(new TestResult(
            testName,
            condition,
            condition ? "Passed" : implementationName + ": " + errorMessage
        ));
    }

    protected void testEmptyConstructor() {
        resetList();
        addResult(
            "Empty Constructor",
            list.length() == 0,
            "Empty constructor should create list of length 0"
        );
    }

    protected void testArrayConstructor() {
        resetList();
        Object[] array = {"A", "B", "C"};
        IList newList = createList(array);
        
        addResult(
            "Array Constructor Length",
            newList.length() == 3,
            "Array constructor should create list of length 3"
        );
        
        addResult(
            "Array Constructor Elements",
            newList.elementAt(0).equals("A") &&
            newList.elementAt(1).equals("B") &&
            newList.elementAt(2).equals("C"),
            "Array constructor elements don't match"
        );
    }

    protected void testInsertAndRemove() {
        resetList();
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(1, "C");
        
        addResult(
            "Insert Elements",
            list.length() == 3 &&
            list.elementAt(0).equals("A") &&
            list.elementAt(1).equals("C") &&
            list.elementAt(2).equals("B"),
            "Insert elements failed"
        );
        
        Object removed = list.removeAt(1);
        addResult(
            "Remove Element",
            removed.equals("C") &&
            list.length() == 2 &&
            list.elementAt(0).equals("A") &&
            list.elementAt(1).equals("B"),
            "Remove element failed"
        );
    }

    protected void testIterator() {
        resetList();
        Object[] array = {"A", "B", "C"};
        IList newList = createList(array);
        ICollection.Iterator it = newList.iterator();
        
        StringBuilder result = new StringBuilder();
        while (it.hasNext()) {
            result.append(it.next());
        }
        
        addResult(
            "Iterator",
            result.toString().equals("ABC"),
            "Iterator traversal failed"
        );
    }

    protected void testContainsAndRemoveElement() {
        resetList();
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(2, "C");
        
        addResult(
            "Contains Element",
            list.contains("B") && !list.contains("D"),
            "Contains check failed"
        );
        
        boolean removed = list.remove("B");
        addResult(
            "Remove By Value",
            removed && !list.contains("B") && list.length() == 2,
            "Remove by value failed"
        );
    }

    protected void testGrowth() {
        resetList();
        for (int i = 0; i < 40; i++) {
            list.insertAt(i, i);
        }
        
        boolean allMatch = true;
        for (int i = 0; i < 40; i++) {
            if (!list.elementAt(i).equals(i)) {
                allMatch = false;
                break;
            }
        }
        
        addResult(
            "Growth",
            list.length() == 40 && allMatch,
            "Growth test failed"
        );
    }

    protected void testToArray() {
        resetList();
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(2, "C");
        
        Object[] array = new Object[3];
        list.toArray(array);
        
        try {
            addResult(
                "ToArray",
                array[0].equals("A") &&
                array[1].equals("B") &&
                array[2].equals("C"),
                "ToArray conversion failed"
            );
        } catch (NullPointerException e) {
            addResult(
                "ToArray",
                false,
                "ToArray failed: array contains null values"
            );
        }
    }

    protected abstract IList createList(Object[] elements);
}