package oop.ex6.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for the initial parsing of the file the program tries to test.
 *
 */
public class FileToArrayParser {

    /**
     * This method gets the directory of the code file and translate the code into an array of strings
     * every cell in the array contains a line from the code.
     * @param fileDirectory the directory of the code file
     * @return an ArrayList of strings, each string is a line in the code
     * @throws FileNotFoundException
     */
    public static ArrayList<String> convertFileToArray(String fileDirectory) throws FileNotFoundException
    {
        ArrayList<String> linesArray = new ArrayList<String>();
        File myFile = new File(fileDirectory);
        Scanner scanner = new Scanner(myFile);
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            linesArray.add(line);
        }
        scanner.close();
        return linesArray;
    }
}
