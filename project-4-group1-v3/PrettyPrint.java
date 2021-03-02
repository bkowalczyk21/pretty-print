import java.util.*;
import java.io.*;

public class PrettyPrint{

    private static ArrayList<ArrayList<String>> makePretty(ArrayList<String> words, int maxLen){

        int[] optSlack = new int[words.size() + 1];                 // ith index holds optimal slack for printing first i words
        int[] lineLengths = new int[words.size() + 1];              // ith position gives how to write the lines for optimal slack of first i words

        for (int i = 1; i < words.size() + 1; i++){         // Every pass finds the optimal slack for the first i words
            int minSlack = Integer.MAX_VALUE;
            int slack = 0;
            int numWords = 1;
            int j = i;
            int numChars = words.get(j - 1).length(); 

            if (maxLen < numChars){
                return null;      // word longer than max line lenght
            }

            while (j > 0 && numChars <= maxLen){
                int currSlack = (int) Math.pow(maxLen - numChars + (i - j), 2);
                slack = currSlack + optSlack[j - 1];

                if (slack < minSlack){          // Finds the option that adds the minimum slack
                    minSlack = slack;
                    numWords = i - j + 1;
                }
                j--;

                if (j > 0){
                    numChars += words.get(j-1).length() + 1;
                }
            }
            optSlack[i] = minSlack;
            lineLengths[i] = numWords;
        }
        return createLines(words, lineLengths);  // Method returns the optimal lines
    }


    public static ArrayList<ArrayList<String>> createLines(ArrayList<String> words, int[] lineLengths){
        ArrayList<ArrayList<String>> optLines = new ArrayList<ArrayList<String>>();       // will store final lines
        int numWords = 0;                       // keeps track of the number of words used
        int curWord = words.size() - 1;         // keeps track of the current word
        while(numWords < words.size()){         
            ArrayList<String> curLine = new ArrayList<String>();
            for(int i = 0; i < lineLengths[curWord+1]; i++){
                curLine.add(0, words.get(curWord-i));
            }
            // for(int i = lineLengths[curWord+1]-1; i >= 0; i--){
            //     curLine.add(words.get(curWord-i));
            // }
            numWords += lineLengths[curWord + 1];
            curWord = words.size() - numWords - 1;    

            optLines.add(0,curLine);  // Adds line at beginning of array and moves the rest down (adds last line first)
        }

        return optLines;
    }
    /*
    getFile method: 
    parses the input from given medium, either a file or the System.in
    will also take the maximum line length and ensure that no single word is longer than that length
    Inputs:
        fileName: the name of the file to read, can either be a 
    Outputs:
        ArrayList containing all the words to put into lines
    */ 
    private static ArrayList<String> getFile(String fileName, int maxLen){
        ArrayList<String> inputFile = new ArrayList<String>();
        Scanner file = null;
        // if we have no file we read from the System.in
        if (fileName == null || fileName == "-"){
            file = new Scanner(System.in);
        }
        //else we have to read from the given file
        else{
            try{

                file = new Scanner(new File(fileName));
            }
            catch (FileNotFoundException e){
                System.out.println("No Such File!");    
            }
        }
        // read through file, adding words to arraylist and 
        while (file.hasNext()){
            String temp = file.next();
            //check if the current word is longer than maximum line length
            if (temp.length() > maxLen){
                //print error message and return a null Arraylist
                System.out.println("Word is longer than Maximum Line Length");
                file.close();
                return null;
            }
            //add the word if its good
            inputFile.add(temp);
        }
        file.close();

        return inputFile;
    }


    /*
    Write to filefunction:
    writes the generated lines to a new file
    Inputs:
        arr: the ArrayList of Lines that we will write
        filename: filename we write to, will add the .txt extension
    Outputs:
        none
    */

    private static void writeToFile(ArrayList<ArrayList<String>> arr, String fileName){
        try {
            // opens new file
            FileWriter file = new FileWriter(fileName+".txt");
            //iterates through lines
            for (int i = 0; i < arr.size(); i++){
                // set up temporary variable to hold current line
                String toAdd = "";
                ArrayList<String> temp = arr.get(i);
                //add words to the current line string
                for (int j = 0; j < temp.size(); j++){
                    toAdd+= temp.get(j);
                    //if not the end add a space
                    if (j != temp.size()-1 ){
                        toAdd+= ' ';
                    }else{
                        // if at the end we add a new line character
                        toAdd+='\n';
                    }
                }
                //write the string to the line
                file.write(toAdd);
            }
            //close the file at the end
            file.close();
          } 
          catch (IOException e) {
            System.out.println("Could not Create File");
          }
    }

    /*
    Write to system function:
    writes the generated lines to the console
    Inputs:
        arr: the ArrayList of Lines that we will write
    Outputs:
        none
    */
    private static void writeToSystem(ArrayList<ArrayList<String>> arr){
        //iterate through lines
        for (int i = 0; i < arr.size(); i++){
            // set up temporary strign to add to
            String toAdd = "";
            ArrayList<String> temp = arr.get(i);
            //add words to the string
            for (int j = 0; j < temp.size(); j++){
                toAdd+= temp.get(j);
                if (j != temp.size()-1 ){
                    toAdd+= ' ';
                }
            }
            //print out the new words
            System.out.println(toAdd);
        }
    }



    /*
    Main method
    Parameters:
        Required:
            len: postive integer specifying maximum line length
        Optional:
            fileNameIn: name of file to parse input from
            fileNameOut: Name of file to output parsed input
    */ 
    public static void main(String[] args){
        int len = 0;
        // First parse the command line arguements to see if they are valid or not
        if (args.length == 0){
            System.out.println("Need at least Line Length Parameter!");
            return;
        }
        try { 
            len = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            System.out.println("The length must be a number!");
            return;
        }
        int i = 1;
        String fileNameIn = null;
        String fileNameOut = null;
        while (i < args.length){
            if (i == 1) fileNameIn = args[i];
            if (i == 2) fileNameOut = args[i];
            i++;
        }

        // get list of words in an arraylist
        ArrayList<String> words = getFile(fileNameIn, len);
        if (words == null) return; 
        
        //now we use algorithim to get appropriate line sizes
        ArrayList<ArrayList<String>> toPrint = makePretty(words, len);


        //now output either to System.out or to file if given
        //if we are writing to a file
        if (fileNameOut != null){
            writeToFile(toPrint,fileNameOut);
        }
        //else we will write to System.out
        else{
            writeToSystem(toPrint);
        }
    }
}