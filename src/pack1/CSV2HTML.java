// -----------------------------------------------------
// Assignment 3
// Written by: Kevin Tran - 40209451
// -----------------------------------------------------

// This program allows the hospital's staff to convert CSV files to HTML files. By inputting the name of existing CSV files, this program
// will read each line of the input file and will the convert each lines onto a corresponding output file. This output file will be formatted
// as an HTML file. The program also handles possible errors during the conversion. These errors can be monitored on the Exceptions.log file
// that prints out an error message everytime it encounters one. At the end of the conversion, it will display an HTML file to the screen,
// provided that the user enters the name of the file they wish to see.

package pack1;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class contains the main method as well as the core engine for processing the input files and creating
 * the output files.
 * @author Kevin Tran
 */
public class CSV2HTML {
    /**
     * This method is the core engine of the CSV2HTML tool. It takes in an array of CSV input files and convert each of them
     * to their corresponding html output file, while validating if all of their contents are there.
     * @param filesNames
     * @param readers
     * @param pws
     * @param eLog
     */
    private static void convertCSVtoHTML(String[] filesNames, Scanner[] readers, PrintWriter[] pws, PrintWriter eLog) {
        // Initialize HTML tags
        String htmlHeader = "<html> " +
                "\n<head>" +
                "\n<style>" +
                "\ntable {font-family: arial, sans-serif;border-collapse: collapse;}" +
                "\ntd, th {border: 1px solid #000000;text-align: left;padding: 8px;}" +
                "\ntr:nth-child(even) {background-color: #dddddd;}" +
                "\nspan{font-size: small}" +
                "\n</style>" +
                "\n</head>" +
                "\n<body>" +
                "\n<table>";
        String htmlEndTable = "</tbody>" +
                "\n</table>";
        String htmlFooter = "</body>" +
                "\n</html>";

        // One file at the time
        for (int i = 0; i < readers.length; i++) {
            boolean validAttributes = true;
            int numbAttributes = 1;
            // Read title
            String titleRow = readers[i].nextLine();
            String titleSpace = titleRow.substring(0, titleRow.indexOf(','));
            String title = titleSpace.replaceAll("[\\p{Cf}]", ""); //Avoid Zero width no break space (ZWNBS)
            // Read attributes
            String attributes = readers[i].nextLine();
            // Count number of attributes in CSV file
            for (int j = 0; j < attributes.length(); j++) {
                if (attributes.charAt(j) == ',') {
                    numbAttributes++;
                }
            }
            String[] splitAtt = attributes.split(",");
            // If missing attribute validAttributes will be false
            for (int j = 0; j < splitAtt.length; j++) {
                if (splitAtt[j] == "" || splitAtt.length < numbAttributes) {
                    validAttributes = false;
                }
            }
            if (validAttributes) { // Proceed to convert to HTML file
                pws[i].println(htmlHeader);
                pws[i].println("<caption>" + title + "</caption>" + "\n<tbody>"); // Title

                pws[i].println("<tr>"); // Start attributes
                for (int k = 0; k < numbAttributes; k++) {
                    pws[i].println("<th>" + splitAtt[k] + "</th>");
                }
                pws[i].println("</tr>"); // End attributes

                String data = "";
                int rowNumb = 3; // Data row starts at 3
                boolean missingData = false;

                String regex = "Note";
                boolean note = false;

                while (readers[i].hasNextLine() & !note) { // Print to HTML each data row until end of file or encounters "note"
                    data = readers[i].nextLine();
                    String[] splitData = data.split(",");
                    // If encounters "Note"
                    if (data.substring(0, 4).equals(regex)) {
                        note = true;
                    } else {
                        int tempIndex = splitData.length;
                        for (int j = 0; j < splitData.length; j++) { // Check for missingData in this line
                            try {
                                if(splitData.length < numbAttributes) { // If ata missing at end of row, pass last attribute
                                    isDataValid(splitData, j, filesNames[i],rowNumb, splitAtt[tempIndex], eLog, numbAttributes);
                                    tempIndex++;
                                }
                                isDataValid(splitData, j, filesNames[i],rowNumb, splitAtt[j], eLog, numbAttributes);
                            } catch (CSVDataMissing e) {
                                missingData = true;
                                break;
                            }
                            missingData = false;
                        }
                        if (!missingData) { // If not missing data will print data row
                            pws[i].println("<tr>"); // Start data row
                            for (int k = 0; k < splitData.length; k++) {
                                pws[i].println("<td>" + splitData[k] + "</td>");
                            }
                            pws[i].println("</tr>"); // End data row
                        }
                    }
                    rowNumb++;
                }
                pws[i].println(htmlEndTable);
                if (note) { // Print note row
                    pws[i].println("<span>" + data.substring(0, data.indexOf(',')) + "</span>");
                }

                pws[i].println(htmlFooter);

            } else { // If there are missing attributes, throw exception
                try {
                    checkMissingAttribute(filesNames[i], eLog);
                } catch (CSVAttributeMissing e) {
                    continue;
                }
            }
        }
    }

    /**
     * This method handles the error of missing attributes by throwing it back to the convertCSVtoHTML, so it can catch it.
     * @param file
     * @param eLog
     * @throws CSVAttributeMissing
     */
    private static void checkMissingAttribute(String file, PrintWriter eLog) throws CSVAttributeMissing {
        throw new CSVAttributeMissing("ERROR: In file " + file + ". Missing attribute. " +
                "File is not converted to HTML.", eLog);
    }

    /**
     * This method handles the error of missing data by throwing it back to the convertCSVtoHTML, so it can catch it.
     * @param data
     * @param j
     * @param fileName
     * @param rowNumb
     * @param attribute
     * @param eLog
     * @param numbAttributes
     * @throws CSVDataMissing
     */
    private static void isDataValid(String[] data, int j, String fileName, int rowNumb, String attribute, PrintWriter eLog, int numbAttributes) throws CSVDataMissing {
        if(data[j] == "" || data.length < numbAttributes) {
            throw new CSVDataMissing("WARNING: In file " + fileName + " line " + rowNumb + " is not" +
                    " converted to HTML: missing data: " + attribute + ".", eLog);
        }
    }

    /**
     * This is the main method that starts the program by prompting the user to enter the names of the input files. It then tries to open the input, output,
     * exception files. If these files cannot be opened, or they encounter an error, it will be handled within this main method. Once these files have been opened,
     * the main method calls the convertCSVtoHTML method to convert the files. At the end it will call the method displayOutputFile to display a file to the screen.
     * @param args
     */
    public static void main(String[] args) {
        // Welcome message
        System.out.println("Welcome to the CSV2HTML tool created by Kevin Tran!");
        Scanner kb = new Scanner(System.in);

        // Prompt user for input files names
        System.out.println("Enter CSV files names separated by a comma and no space (xxxx.csv,xxxx.csv) : ");
        String csvFilesNames = kb.nextLine();

        // Put file names into an array
        String[] filesNames;
        if (csvFilesNames.indexOf(',') == -1) {
            filesNames = new String[]{csvFilesNames};
        } else {
            filesNames = csvFilesNames.split(",");
        }

        // Create Exceptions.log file
        File exceptLog = new File("Exceptions.log");
        PrintWriter eLog = null;
        try {
            eLog = new PrintWriter(new FileOutputStream(exceptLog, true));
        } catch (FileNotFoundException e) {
            System.out.println("Error: cannot open Exceptions.log");
            e.getMessage();
        }

        // Create input and output files arrays to pass in convertCSVtoHTML method
        Scanner[] inputFiles = new Scanner[filesNames.length];
        PrintWriter[] outputFiles = new PrintWriter[filesNames.length];

        // Open and verify input/output files
        for (int i = 0; i < filesNames.length; i++) {
            File csvFile = new File(filesNames[i]);
            Scanner csvReader = null;
            // Give same name to output file
            String name = filesNames[i].substring(0, filesNames[i].indexOf('.'));
            File htmlFile = new File(name + ".html");
            PrintWriter htmlPw = null;

            try {
                csvReader = new Scanner(new FileInputStream(csvFile));
                htmlPw = new PrintWriter(new FileOutputStream(htmlFile));
            } catch (FileNotFoundException e) {
                System.out.println("Error: cannot find " + name + ".csv or " + name + ".html files.");
                e.getMessage();
            }

            // Verify if input/output file exists and can be read/written
            if (!verifyInput(csvFile, csvReader, eLog)) {
                return;
            }
            if (!verifyOutput(htmlFile, eLog)) {
                cleanUp(htmlPw, csvReader, eLog);
                return;
            } else { // Add files to input/output arrays
                inputFiles[i] = csvReader;
                outputFiles[i] = htmlPw;
            }
        }
        // Call conversion method
        convertCSVtoHTML(filesNames, inputFiles, outputFiles, eLog);
        // Close all opened file and clear buffer
        for (int i = 0; i < filesNames.length; i++) {
            outputFiles[i].flush();
            outputFiles[i].close();
            inputFiles[i].close();
            eLog.flush();
            eLog.close();
        }
        System.out.println("Successfully converted!");
        // Display an HTML file to screen
        displayOutputFile();
        // Closing message
        System.out.println("Thank you for using the CSV2HTML tool!");
    }

    /**
     * This method returns true if the input file exists and if it can be read, it returns false and prints an error message otherwise.
     * @param csvFile
     * @param csvReader
     * @param eLog
     * @return boolean
     */
    private static boolean verifyInput(File csvFile, Scanner csvReader, PrintWriter eLog) {
        if (!csvFile.exists() || !csvFile.canRead()) {
            eLog.println("\nCould not open input file " + csvFile.getName() + " for reading." +
                    "\nPlease check that the file exists and is readable. \nThis program will terminate after closing amy opened files.");
            eLog.flush();
            eLog.close();
            csvReader.close();
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns true if the output file exists and if it can be written, it returns false and prints an error message otherwise.
     * @param htmlFile
     * @param eLog
     * @return boolean
     */
    private static boolean verifyOutput(File htmlFile, PrintWriter eLog) {
        if (!htmlFile.exists() || !htmlFile.canWrite()) {
            eLog.println("\nCould not create output file " + htmlFile.getName() + " for reading." +
                    "\nPlease check that the file exists and is readable. \nThis program will terminate after closing amy opened files.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method cleans up the program in case of an output file error. It closes all the open input and output files, as well as deletes any
     * created output files.
     * @param htmlPw
     * @param csvReader
     * @param eLog
     */
    private static void cleanUp(PrintWriter htmlPw, Scanner csvReader, PrintWriter eLog) {
        eLog.flush();
        eLog.close();
        htmlPw.flush();
        htmlPw.close();
        csvReader.close();
        String directory = System.getProperty("user.dir");
        String regex = ".html";
        File htmlDir = new File(directory);

        for (File f : htmlDir.listFiles()) {
            Pattern symbolPat = Pattern.compile(regex);
            Matcher matcher = symbolPat.matcher(f.getName());
            if (matcher.find()) {
                f.delete();
            }
        }
    }

    /**
     * This method allows the user to choose an output file they wish to see on the screen. Once the file has been chosen, using the buffered reader
     * to read the file, it will output onto the screen, the html file.
     */
    private static void displayOutputFile() {
        Scanner kb = new Scanner(System.in);
        String fileName = "";
        BufferedReader htmlFile = null;
        for (int i = 1; i <= 2; i++) { // Only allows 2 attempts for user prompt
            try {
                System.out.println("Please enter the name of the file you wish to see (ex: xxxx.html): ");
                fileName = kb.nextLine();
                // Open buffered reader
                htmlFile = new BufferedReader(new FileReader(fileName));
                String line = "";
                while (line != null) { // Print content of html file
                    line = htmlFile.readLine();
                    System.out.println(line);
                }
                return;

            } catch (FileNotFoundException exception) { // Invalid input
                if (i==2) {
                    return;
                }
                System.out.println("Please try again.");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}



