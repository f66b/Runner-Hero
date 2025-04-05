package oop.collections.tests;

import java.util.List;

public class AllTests {
    private static void printResults(String className, List<TestResult> results) {
        int passed = 0;
        int total = results.size();
        
        System.out.println("\nTesting " + className + "...\n");
        
        for (TestResult result : results) {
            if (result.isPassed()) {
                System.out.println("✓ " + result.getTestName() + " passed");
                passed++;
            } else {
                System.out.println("✗ " + result.getTestName() + " failed: " + result.getMessage());
            }
        }
        
        double percentage = (passed * 100.0) / total;
        System.out.println(String.format("\nResults for %s: %d/%d tests passed (%.1f%%)\n",
            className, passed, total, percentage));
    }

    public static void main(String[] args) {
        try {
            System.out.println("Running All Implementation Tests...\n");
            
            // Test ArrayList
            ArrayListTest arrayListTest = new ArrayListTest();
            printResults("ArrayList", arrayListTest.runTests());
            
            // Test LinkedList
            LinkedListTest linkedListTest = new LinkedListTest();
            printResults("LinkedList", linkedListTest.runTests());
            
            // Test HashTable
            HashTableTest hashTableTest = new HashTableTest();
            printResults("HashTable", hashTableTest.runTests());
            
            System.out.println("All test suites completed!");
            
        } catch (Exception e) {
            System.err.println("\n❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}