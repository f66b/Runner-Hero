package oop.collections.tests;

import oop.collections.IMap;
import oop.collections.ICollection;
import oop.utils.collections.HashTable;
import java.util.ArrayList;
import java.util.List;

public class HashTableTest {
    private final IMap map;
    private final List<TestResult> results;

    public HashTableTest() {
        this.map = new HashTable();
        this.results = new ArrayList<>();
    }

    public List<TestResult> runTests() {
        results.clear();
        
        testEmptyConstructor();
        testPutAndGet();
        testRemove();
        testContains();
        testIterators();
        testArrayConversions();
        testNullHandling();
        
        return results;
    }

    private void addResult(String testName, boolean condition, String errorMessage) {
        results.add(new TestResult(
            testName,
            condition,
            condition ? "Passed" : "HashTable: " + errorMessage
        ));
    }

    private void testEmptyConstructor() {
        addResult(
            "Empty Constructor",
            map.length() == 0,
            "Empty constructor should create map of length 0"
        );
    }

    private void testPutAndGet() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        addResult(
            "Put and Get",
            map.get("key1").equals("value1") &&
            map.get("key2").equals("value2") &&
            map.length() == 2,
            "Put and Get operations failed"
        );
        
        Object oldValue = map.put("key1", "newValue1");
        addResult(
            "Update Value",
            oldValue.equals("value1") &&
            map.get("key1").equals("newValue1"),
            "Value update failed"
        );
    }

    private void testRemove() {
        map.put("key1", "value1");
        Object removed = map.remove("key1");
        
        addResult(
            "Remove",
            removed.equals("value1") &&
            map.get("key1") == null &&
            !map.contains("key1"),
            "Remove operation failed"
        );
    }

    private void testContains() {
        map.put("key1", "value1");
        
        addResult(
            "Contains",
            map.contains("key1") &&
            !map.contains("nonexistent"),
            "Contains check failed"
        );
    }

    private void testIterators() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        boolean keysFound = false;
        boolean valuesFound = false;
        
        ICollection.Iterator keyIterator = map.keys();
        while (keyIterator.hasNext()) {
            Object key = keyIterator.next();
            if (key.equals("key1") || key.equals("key2")) keysFound = true;
        }
        
        ICollection.Iterator valueIterator = map.values();
        while (valueIterator.hasNext()) {
            Object value = valueIterator.next();
            if (value.equals("value1") || value.equals("value2")) valuesFound = true;
        }
        
        addResult(
            "Iterators",
            keysFound && valuesFound,
            "Iterator traversal failed"
        );
    }

    private void testArrayConversions() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        Object[] keys = new Object[2];
        Object[] values = new Object[2];
        
        map.keysToArray(keys);
        map.valuesToArray(values);
        
        boolean keysValid = false;
        boolean valuesValid = false;
        
        for (Object key : keys) {
            if (key.equals("key1") || key.equals("key2")) keysValid = true;
        }
        
        for (Object value : values) {
            if (value.equals("value1") || value.equals("value2")) valuesValid = true;
        }
        
        addResult(
            "Array Conversions",
            keysValid && valuesValid,
            "Array conversion failed"
        );
    }

    private void testNullHandling() {
        map.put(null, "nullValue");
        
        addResult(
            "Null Key Handling",
            map.get(null).equals("nullValue") &&
            map.contains(null),
            "Null key handling failed"
        );
    }
}