# ConverterCSVtoHTML
As a software designer, you have been tasked by the PR department to design and implement a program to transfer the contents
of the Excel tables to the hospital’s website in a best way possible. Based on your investigation, you have determined that the
best option is to convert the Excel tables into HTML tables. You are therefore required to design and implement a Java tool
called CSV2HTML to read and process CSV files and create the corresponding HTML tables. For information on HTML tables
see: https://www.w3schools.com/html/html_tables.asp

The input files consist of two or more text files in CSV format. The CSV files typically include attributes as described in Figure
1(a), but since they could be modified by the PR staff, your code must be able to accommodate any CSV file with any attributes.
In your design you may assume the following about the input CSV files:
a. There are at least three lines in a CSV file.
b. The first line represents the Title of the table.
c. The second line contains exactly four attributes, whose names may vary depending on the CSV file.
d. The third and possibly the following lines, if any, each represent the actual data records.
e. The last line may represent a note line if it begins with the text “Note:” in its first data field.
The title in a CSV file is stored in HTML <caption> tags, the attributes are stored in <th> tags, the data records in <tr>
tags, the data fields in <td> tags, and finally the note, if any, is stored in <span> tags.
  
Given a number of CSV files, your program must generate the corresponding HTML files. The HTML files must have the same
name as their corresponding CSV files but with the extension “.html”. Your program must also log particular runtime processing exceptions (more details below), if any, to a text file named
“Exceptions.log”. This log file must be opened for appending whether it exists or not, and regardless of whether your
program encounters processing errors.
  
a. In case of a missing attribute, the file should not be converted to HTML. Instead, a message must be displayed to the
user and the name of the file must be written to the log file (“Exceptions.log”). See Figure 3 for an example of a
file with missing attribute.
b. In case of missing data, the data record should not be included in the HTML table; however, a message should be both
displayed to the user and written to the log file (“Exceptions.log”). See Figure 4 for an example of missing data
and the specific error message associated with the missing data.
  
Requirement 1: Input Files
In this assignment you must demonstrate your program with a minimum of two CSV files. For demonstration purposes,
you will find two sample files covidStatistics.CSV and doctorList.CSV included in the assignment zip file.
Your code must work on the two sample files as well as on any number of CSV file with arbitrary number data records.
  
Requirement 2: Programmer-Defined Exceptions
Write two exception classes called CSVAttributeMissing and CSVDataMissing. These classes should have
appropriate constructors to allow:
a. A default error message that reads “Error: Input row cannot be parsed due to missing information”.
b. A programmer supplied message. This is actually the constructor that you will be using throughout this assignment.
The behavior of the two exceptions is presented in Requirement 4 below.
  
Requirement 3: Opening the input and output streams
In the method main() of the application, use a Scanner object to open the input files (covidStatistics.CSV and
doctorList.CSV) for reading. If either of the files does not exist or is not readable, then your program must display
the following error message and then terminate:
Could not open input file xxxxx for reading.
Please check that the file exists and is readable. This program will terminate after closing any opened files.
  
However, you must close all the opened files before exiting the program. For example, if covidStatistics.CSV does
not exist, then the following shows the behavior of the program:
Could not open file covidStatistics.CSV for reading
Please check that the file exists and is readable. This program will terminate after closing any opened files.

  If the input files have successfully been opened, then your program will attempt to open/create all of the output files
(covidStatistics.html, doctorList.html and Exceptions.log). You will use PrintWriter to open these output
files. If “any” of these output files cannot be created, then you must:
a. Display a message to the user indicating which file could not be opened/created;
b. Clean the directory by deleting all of the other output files that have already been successfully created.
c. Close all the opened input files, and then exit the program.

  Requirement 4: Creating the HTML file
Write a method called ConvertCSVtoHTML This method will represent the core engine for processing the input files
and creating the output files. You can pass any needed parameters to this method, and the method may return any needed
information. This method however must declare two exceptions CSVAttributeMissing and CSVDataMissing.
In other words, any exceptions that may occur within this method must be handled by the calling method . Specifically:
a. The method should work on the files that are already open;
b. The method must process each of these files to find out whether it is valid or not. (Here valid means no missing
attributes, remember the number of attributes must be four)
c. If a file is valid, then the method must create the corresponding HTML files.
d. If a file is invalid, then the method must stop processing this file, and must throw a CSVAttributeMissing
exception to display an exception message and save the exception information in the log file. For example in Fig.
3, the following message is displayed to the user and saved in the log file :
 ERROR: In file covidStatistics.CSV. Missing attribute.File is not converted to HTML.
e. If a file is valid but one of the data rows has missing data then, the method throws a CSVDataMissing exception.
This data row will not be converted to HTML and a message is both presented to the user and written to the log file.
For the example in Figure 4, the following message is displayed to the user and saved in the log file:
WARNNING: In file covidStatistics.CSV line 5 is not converted to HTML: missing data: ICU.
 The method will then continue with the processing of the remaining data rows, if any. 
  
  Requirement 5: Displaying the HTMl files
Finally, your program must display one of the output file on the screen. So your program will prompt the user to
enter the name of one of the output files created above.
- If the user enters an invalid name, a FileNotFoundException should be thrown, but the user must be allowed
a second and final chance to enter another file name. If this second attempt also fails, then the program must
terminate.
- Otherwise, if the entered file name is valid, then your program must open this file for reading using the
BufferedReader class, outputting its contents on the screen. Do not use the Scanner class to read the file for this
task. 
  
  a. For processing of input files, you may want to use the StringTokenizer class or the split method. But you CANNOT use
lists, Hash tables, hash maps, collections, or any other built-in data types.
b. You should minimize opening and closing the files as much as possible; a better mark will be given for that;
c. Don’t use any external libraries or existing software to produce what is required; that will directly result in a 0 mark!
d. Your program must work for any input files. The CSV files provided with this assignment are only one possible versions,
and must not be considered as the general case when writing your code.
