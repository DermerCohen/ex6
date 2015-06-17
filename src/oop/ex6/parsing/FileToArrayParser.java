package oop.ex6.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for the initial parsing of the file the program tries to test.
 * Created by amircohen on 6/14/15.
 */
public class FileToArrayParser {

    public static ArrayList<String> convertFileToArray(String fileDirectory) throws FileNotFoundException
    {//TODO:
    // handle this exception
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
