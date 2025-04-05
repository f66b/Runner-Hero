package oop.collections.tests;

public class TestResult {
    private final String testName;
    private final boolean passed;
    private final String message;

    public TestResult(String testName, boolean passed, String message) {
        this.testName = testName;
        this.passed = passed;
        this.message = message;
    }

    public String getTestName() { return testName; }
    public boolean isPassed() { return passed; }
    public String getMessage() { return message; }
}