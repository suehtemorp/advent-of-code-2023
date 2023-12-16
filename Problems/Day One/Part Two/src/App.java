/// Scan charset string inputs for integers
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// Spelling-to-digit mapping
import java.util.Map;
import java.util.HashMap;

public class App 
{
    /// Map each spelling of a digit to its integer value
    private static Map<String, Integer> SpellingToDigit 
        = new HashMap<String, Integer>()
        {
            {
                /// put("zero", 0);
                put("one", 1);
                put("two", 2);
                put("three", 3);
                put("four", 4);
                put("five", 5);
                put("six", 6);
                put("seven", 7);
                put("eight", 8);
                put("nine", 9);
            }
        };

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

        Pattern spellingOrDigitPattern = Pattern.compile
            ("(?=(zero|one|two|three|four|five|six|seven|eight|nine|\\d))");
        
        for (Integer lineIndex = 1; digitScanner.hasNextLine(); 
            lineIndex += 1)
        {
            /// Buffer each digit match
            Matcher lineMatches = spellingOrDigitPattern.matcher
                (digitScanner.nextLine());

            /// Capture first digit
            Integer leftmostDigit = null;

            if (lineMatches.find())
            {
                String lineMatch = lineMatches.group(1);

                /// Perfom specialized parsing depending or whether or
                /// not the digit is spelled out or directly typed 
                if (lineMatch.matches("\\d"))
                {leftmostDigit = Integer.parseInt(lineMatch);}
                else
                {leftmostDigit = SpellingToDigit.get(lineMatch);}
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

            while (lineMatches.find())
            {
                String lineMatch = lineMatches.group(1);
                
                /// Perfom specialized parsing depending or whether or
                /// not the digit is spelled out or directly typed
                if (lineMatch.matches("\\d"))
                {rightmostDigit = Integer.parseInt(lineMatch);}
                else
                {rightmostDigit = SpellingToDigit.get(lineMatch);}
            }

            /// If no last digit is found past the first digit, then
            /// the last digit is by definition the first digit
            if (rightmostDigit == null)
            { rightmostDigit = leftmostDigit; }

            /// Me may now construct the current line calibration value
            /// and inject it right away into the total sum
            totalSum += leftmostDigit * 10 + rightmostDigit;
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
