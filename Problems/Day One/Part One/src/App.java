/// Scan charset string inputs for integers
import java.util.Scanner;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception 
    {
        /// Indicate user to input the document
        System.out.println("Enter the calibration document below.");
        System.out.println("EOF will be taken as the end of input.");
        
        /// Keep track of the total calibration sum
        Integer totalSum = 0;

        /// Update the sum for each line until it runs out
        /// To do so, scan each line until no more digits exist
        Scanner digitScanner = new Scanner(System.in);
        Pattern digitPattern = Pattern.compile("\\d");
        
        for (Integer lineIndex = 1; digitScanner.hasNextLine(); 
            lineIndex += 1)
        {
            /// Buffer each digit match
            String lineMatch;

            /// Capture first digit
            Integer leftmostDigit = null;

            lineMatch = digitScanner.findInLine(digitPattern);
            if (lineMatch != null)
            {
                leftmostDigit 
                    = Integer.parseInt(lineMatch);
            }

            /// If there is no first digit, the line is ill-informed
            /// and we may not proceed
            else
            {
                throw new Exception
                (
                    String.format
                    (
                        "Document is ill-informed! At line %d", 
                        lineIndex
                    )
                );
            }


            /// Capture last digit past the first digit
            Integer rightmostDigit = null;

            for (lineMatch = digitScanner.findInLine(digitPattern); 
                lineMatch != null; 
                lineMatch = digitScanner.findInLine(digitPattern))
            {
                rightmostDigit
                    = Integer.parseInt(lineMatch);
            }

            /// If no last digit is found past the first digit, then
            /// the last digit is by definition the first digit
            if (rightmostDigit == null)
            { rightmostDigit = leftmostDigit; }

            /// Me may now construct the current line calibration value
            /// and inject it right away into the total sum
            totalSum += leftmostDigit * 10 + rightmostDigit;

            /// After processing the current line, we may carry onto the
            /// next one
            digitScanner.nextLine();
        }

        /// After all lines have been scanned, we may now close the scanner
        /// and report the net sum of all the lines' calibration values 
        digitScanner.close();

        System.out.println
        (
            String.format
            (
                "The net calibration value is %d", 
                totalSum
            )
        );
    }
}
