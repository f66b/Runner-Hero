package oop.collections.tests;

import oop.utils.collections.ArrayList;
import oop.utils.collections.LinkedList;
import oop.collections.IList;
import oop.collections.ICollection;

public class AllTests {
	private static void runTestsForImplementation(String implName, IList list) {
	    System.out.println("\nTesting " + implName + "...\n");

	    runTest(() -> testEmptyConstructor(implName, list), "Empty Constructor Test", implName);
	    runTest(() -> testArrayConstructor(implName), "Array Constructor Test", implName);
	    runTest(() -> testInsertAndRemove(implName, list), "Insert and Remove Test", implName);
	    runTest(() -> testIterator(implName), "Iterator Test", implName);
	    runTest(() -> testContainsAndRemoveElement(implName, list), "Contains and Remove Element Test", implName);
	    runTest(() -> testGrowth(implName, list), "Growth Test", implName);
	    runTest(() -> testToArray(implName, list), "ToArray Test", implName);
	}

	/**
	 * Runs a test and catches errors to allow all tests to execute.
	 */
	private static void runTest(Runnable testMethod, String testName, String implName) {
	    try {
	        testMethod.run();
	        System.out.println("✅ PASSED: " + testName);
	    } catch (AssertionError e) {
	        System.err.println("❌ FAILED: " + testName + " for " + implName + " - " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("❌ ERROR: " + testName + " for " + implName + " - Unexpected exception: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


    private static void testEmptyConstructor(String implName, IList list) {
        assert list.length() == 0 : implName + ": Empty constructor should create list of length 0";
        System.out.println("OK: Empty constructor test passed");
    }

    private static void testArrayConstructor(String implName) {
        Object[] array = {"A", "B", "C"};
        IList list = implName.equals("ArrayList") ? new ArrayList(array) : new LinkedList(array);
        assert list.length() == 3 : implName + ": Array constructor should create list of length 3";
        assert list.elementAt(0).equals("A") : implName + ": First element should be 'A'";
        assert list.elementAt(1).equals("B") : implName + ": Second element should be 'B'";
        assert list.elementAt(2).equals("C") : implName + ": Third element should be 'C'";
        System.out.println("OK: Array constructor test passed");
    }

    private static void testInsertAndRemove(String implName, IList list) {
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(1, "C");
        
        assert list.length() == 3 : implName + ": List should have length 3 after insertions";
        assert list.elementAt(0).equals("A") : implName + ": First element should be 'A'";
        assert list.elementAt(1).equals("C") : implName + ": Second element should be 'C'";
        assert list.elementAt(2).equals("B") : implName + ": Third element should be 'B'";
        
        Object removed = list.removeAt(1);
        assert removed.equals("C") : implName + ": Removed element should be 'C'";
        assert list.length() == 2 : implName + ": List should have length 2 after removal";
        assert list.elementAt(0).equals("A") : implName + ": First element should still be 'A'";
        assert list.elementAt(1).equals("B") : implName + ": Second element should now be 'B'";
        
        System.out.println("OK: Insert and remove test passed");
    }

    private static void testIterator(String implName) {
        Object[] array = {"A", "B", "C"};
        IList list = implName.equals("ArrayList") ? new ArrayList(array) : new LinkedList(array);
        ICollection.Iterator it = list.iterator();
        
        assert it.hasNext() : implName + ": Iterator should have next element";
        assert it.next().equals("A") : implName + ": First element should be 'A'";
        assert it.hasNext() : implName + ": Iterator should have second element";
        assert it.next().equals("B") : implName + ": Second element should be 'B'";
        assert it.hasNext() : implName + ": Iterator should have third element";
        assert it.next().equals("C") : implName + ": Third element should be 'C'";
        assert !it.hasNext() : implName + ": Iterator should not have more elements";
        
        System.out.println("OK: Iterator test passed");
    }

    private static void testContainsAndRemoveElement(String implName, IList list) {
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(2, "C");
        
        assert list.contains("B") : implName + ": List should contain 'B'";
        assert !list.contains("D") : implName + ": List should not contain 'D'";
        
        boolean removed = list.remove("B");
        assert removed : implName + ": Remove should return true for existing element";
        assert !list.contains("B") : implName + ": List should not contain 'B' after removal";
        assert list.length() == 2 : implName + ": List should have length 2 after removal";
        
        System.out.println("OK: Contains and remove element test passed");
    }

    private static void testGrowth(String implName, IList list) {
        // Insert 40 elements to test growth behavior
        for (int i = 0; i < 40; i++) {
            list.insertAt(i, i);
        }
        
        assert list.length() == 40 : implName + ": List should have length 40";
        for (int i = 0; i < 40; i++) {
            assert list.elementAt(i).equals(i) : implName + ": Element at " + i + " should be " + i;
        }
        
        System.out.println("OK: Growth test passed");
    }

    private static void testToArray(String implName, IList list) {
        list.insertAt(0, "A");
        list.insertAt(1, "B");
        list.insertAt(2, "C");
        
        Object[] array = new Object[3];
        list.toArray(array);
        
        assert array[0].equals("A") : implName + ": First array element should be 'A'";
        assert array[1].equals("B") : implName + ": Second array element should be 'B'";
        assert array[2].equals("C") : implName + ": Third array element should be 'C'";
        
        System.out.println("OK: ToArray test passed");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Running List Implementation Tests...\n");
            
            // Test ArrayList
            runTestsForImplementation("ArrayList", new ArrayList());
            
            // Test LinkedList
            runTestsForImplementation("LinkedList", new LinkedList());
            
            System.out.println("\nAll tests passed successfully! ");
        } catch (AssertionError e) {
            System.err.println("\n KO: Test failed: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n KO: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}