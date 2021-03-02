import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**Test Class for PrettyPrint.java testing ability to process command 
 * line arguments, algorithm correctness and efficeincy **/

public class TestPrettyPrint {
    
    /** Corectness test method. Tests algorithm on random inputs of
     * up to 100000 characters with different maximum line lengths up to 1000
     * both on a logarithmic scale. Test should also catch the error where 
     * there are words longer than the line length.
     **/
    public static void prettyPrintViaSystemInput() {
        final InputStream original = System.in;
        int i = 1;
        while (i < 100001) {
            int j = 1;
            while (j < 1001) {
                InputStream input = new ByteArrayInputStream(randomStringOfLength(i).getBytes());
                System.setIn(input);
                System.out.println("Printing wrapped text with " + i + " characters and a max line length of " + j + " charcters.");
                System.out.println();
                PrettyPrint.main(new String[] {Integer.toString(j)});
                j *= 10;
            }
            i *= 10;
        }
        System.setIn(original);
    }

    /** Effeciency test method. Tests algorithm on random inputs of
     * up to 100000 characters with different maximum line lengths up to 1000
     * and prints the time the algorithm takes.
     * Actual output is printed to output files testing ability to handle
     * command line arguments **/
    public static void prettyPrintViaSystemInputWithOutputFileName() {
        final InputStream original = System.in;
        int i = 1;
        while (i < 100000) {
            int j = 1;
            while (j < 1000) {
                final InputStream input = new ByteArrayInputStream(randomStringOfLength(i).getBytes());
                System.setIn(input);
                final String fileName = randomStringOfLength(10);
                long start = System.nanoTime();
                PrettyPrint.main(new String[] {Integer.toString(i), "-", fileName});
                long end = System.nanoTime();
                System.out.println("Text with " + i + " characters and max line length " + j + " printed in: " + (end - start));
                System.out.println();
                j *= 10;
            }
            i *= 10;
        }
        System.setIn(original);
    }

    /** Large input file test. Tests input file reading ability with several
     * different line lengths.
     */
    public static void prettyPrintViaInputFileWithOutputFileName() {
        int i = 1;
        while (i < 1000) {
            PrettyPrint.main(new String[] {Integer.toString(i), "Siddhartha.txt", "Siddhartha" + i});
            System.out.println("Siddhartha wrapped to a max line length of " + i + " saved to Siddhartha" + i + ".txt.");
            i *= 2;
        }
        
    }

    public static void prettyPrintTrivialCases() {
        System.out.println("Printing lines with length 0");
        System.out.println();
        PrettyPrint.main(new String[] {Integer.toString(0), "test.txt", "linelength0"});
        System.out.println("Printing lines from an unavailable file:");
        PrettyPrint.main(new String[] {Integer.toString(0), "unavailable.pdf", "unavailable"});
    }

    /** Helper method to create a random string **/
    public static final String AlPHABET_AND_SPACE = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String randomStringOfLength(int length) {
        StringBuilder sBuilder = new StringBuilder();
        while (length != 0) {
            int index = (int)(Math.random()%AlPHABET_AND_SPACE.length());
            sBuilder.append(AlPHABET_AND_SPACE.charAt(index));
            length--;
        }
        return sBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println("Testing for correctness:");
        System.out.println();
        prettyPrintViaSystemInput();
        System.out.println("Testing for effeciency (and file output):");
        System.out.println();
        prettyPrintViaSystemInputWithOutputFileName();
        System.out.println("Testing large file input:");
        System.out.println();
        prettyPrintViaInputFileWithOutputFileName();
        prettyPrintTrivialCases();
    }
}