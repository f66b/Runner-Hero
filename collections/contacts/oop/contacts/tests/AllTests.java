package oop.contacts.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class AllTests {
    
    private final String TEST_DB_PATH = "test";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayInputStream inContent;
    
    // Test commands to simulate user input
    private final String testCommands = 
        "add phone=\"212 555 5678\" name=\"Droopy \" email=\"droopy@google.com\";\n" +
        "add phone=\"212 555 6732\" name=\"Tom \" email=\"tom@google.com\";\n" +
        "select name=D*;\n" + 
        "update phone=\"212-555-5678\" country=\"US\";\n" +
        "select name=D*;\n" +
        "update phone=\"212-555-5678\" phone=\"(1) 212-555-5678\";\n" +
        "select name=D*;\n" +
        "update phone=212-555-5678 country=\"USA\";\n" +
        "select name=D*;\n" +
        "select name=\"Tom\";\n" +
        "remove phone=\"212 555 6732\";\n" +
        "select name=Tom;\n" +
        "select name=*;\n" +
        "select phone=\"06 2*\";\n" +
        "select email=*google*;\n" +
        "exit;\n";
    
    public AllTests() {
        inContent = new ByteArrayInputStream(testCommands.getBytes());
    }
    
    private void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
        
        // Delete test database if it exists
        File dbFile = new File(TEST_DB_PATH + ".db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
    
    private void restoreStreams() {
        System.setOut(originalOut);
        
        // Clean up - delete test database
        File dbFile = new File(TEST_DB_PATH + ".db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
    
    private void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    public void testContactsOperations() throws IOException {
        try {
            setUpStreams();
            
            // Run the main program with our test DB path
            oop.contacts.shell.Main.main(new String[]{"-db=" + TEST_DB_PATH});
            
            String output = outContent.toString();
            
            // Verify all the expected outputs
            
            // Verify contacts were added
            assertTrue("Should show Droopy was added", 
                    output.contains("Droopy") && output.contains("212 555 5678") && output.contains("droopy@google.com"));
            assertTrue("Should show Tom was added", 
                    output.contains("Tom") && output.contains("212 555 6732") && output.contains("tom@google.com"));
            
            // Verify update operations
            assertTrue("Should show country US was added", output.contains("country= \"US\""));
            assertTrue("Should show phone number was updated with country code", 
                    output.contains("phone= (1) 212 555 5678"));
            assertTrue("Should show country was updated to USA", output.contains("country= \"USA\""));
            
            
            // Verify select with wildcards
            int emailSelectIndex = output.lastIndexOf("select email=*google*");
            int lastDroopyIndex = output.lastIndexOf("Droopy");
            assertTrue("Should find Droopy with google email", lastDroopyIndex > emailSelectIndex);
            
            System.out.println("testContactsOperations: All assertions passed");
        } finally {
            restoreStreams();
        }
    }
    
    public void testDatabasePersistence() throws IOException {
        try {
            // First create and save a database
            createTestDatabase();
            
            // Set up new streams for the test
            ByteArrayOutputStream newOutContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOutContent));
            
            // Create input for just selecting the contacts
            ByteArrayInputStream selectInput = new ByteArrayInputStream(
                "select name=*;\nexit;\n".getBytes());
            System.setIn(selectInput);
            
            // Run main with the test database
            oop.contacts.shell.Main.main(new String[]{"-db=" + TEST_DB_PATH});
            
            String output = newOutContent.toString();
            
            // Verify contact was loaded from the database
            assertTrue("Should load Droopy from database", 
                    output.contains("Droopy") && output.contains("212 555 5678"));
            
            System.out.println("testDatabasePersistence: All assertions passed");
        } finally {
            restoreStreams();
        }
    }
    
    private void createTestDatabase() throws IOException {
        // Create a test database file with one contact
        File dbFile = new File(TEST_DB_PATH + ".db");
        try (PrintStream ps = new PrintStream(new FileOutputStream(dbFile))) {
            ps.println("add phone=\"212 555 5678\" name=\"Droopy \" email=\"droopy@google.com\";");
        }
    }
    
    public static void main(String[] args) {
        AllTests tests = new AllTests();
        try {
            System.out.println("Running testContactsOperations...");
            tests.testContactsOperations();
            
            System.out.println("\nRunning testDatabasePersistence...");
            tests.testDatabasePersistence();
            
            System.out.println("\nAll tests for contact Application passed successfully!");
        } catch (AssertionError e) {
            System.err.println("Test failed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception during tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
}