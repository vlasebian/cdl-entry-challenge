
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Code {
    private ArrayList<String> allCommands;
    private int codePointer;
    private int numberOfCores;

    private String command;
    private String firstArg;
    private String secondArg;

    public Code(String inputFileName) {
        File in = new File(inputFileName);
        allCommands = new ArrayList<String>();
        codePointer = 0;

        try {
            Scanner s = new Scanner(in);
            // Read the number of cores
            String firstLine = s.nextLine();
            numberOfCores = Integer.parseInt(firstLine);
            // Save all the commands line by line in a string array
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (!line.isEmpty()) {
                    allCommands.add(line);
                }
            }
            s.close();
        } catch (FileNotFoundException e) {
            // If the input file cannot be opened, the program will exit with error code -1
            System.err.println("[Error]: The input file cannot be opened!");
            System.exit(-1);
        }

        command = null;
        firstArg = null;
        secondArg = null;
    }

    public boolean nextInstruction() {
        if (allCommands.size() <= codePointer) {
            // The method returns false when there is no line left
            return false;
        }

        // Get the line corresponding to the counter
        StringTokenizer currentInstruction = new StringTokenizer(allCommands.get(codePointer));

        command = currentInstruction.nextToken();
        firstArg = currentInstruction.nextToken();
        if (currentInstruction.hasMoreTokens()) {
            secondArg = currentInstruction.nextToken();
        } else {
            secondArg = null;
        }

        return true;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public String getCommand() {
        return command;
    }

    public String getFirstArg() {
        return firstArg;
    }

    public String getSecondArg() {
        return secondArg;
    }

    public int getCodePointer() {
        return codePointer;
    }

    public void setCodePointer(int x) {
        codePointer = x;
    }
}
