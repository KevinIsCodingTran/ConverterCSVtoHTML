// -----------------------------------------------------
// Assignment 3
// Written by: Kevin Tran - 40209451
// -----------------------------------------------------
package pack1;

import java.io.PrintWriter;
/**
 * This is a customized exception class (which extends Exception) in case of a missing data while converting
 * from CSV to HTML.
 * @author Kevin Tran
 */
public class CSVDataMissing extends Exception {
    /**
     * Default constructor with a default message
     */
    public CSVDataMissing() {
        System.out.println("Error: Input row cannot be parsed due to missing information");
    }

    /**
     * Parameterized constructor with two parameters to print an error message to the user and
     * the exception log.
     * @param message
     * @param eLog
     */
    public CSVDataMissing(String message, PrintWriter eLog) {
        System.out.println("\n" + message);
        eLog.println("\n" + message);
    }
}
