import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Calculator {
    public static void main(String[] args) throws Exception {
        while (true) {

            BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
            String inputExpression;

            try {
                System.out.println("Please type your expression. Support +,-,*,/,^,%,(,) :");
                System.out.println("Or type 'exit' to stop the program");
                inputExpression = d.readLine();
                if (inputExpression.equals("exit")) {
                    break;
                } else {
                    inputExpression = convertingReversePolishNotation(inputExpression);
                    System.out.println(calculatingExpression(inputExpression));
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Convert string into Reverse Polish notation
     *
     * @param userExpression input string
     * @return output string with RPN
     */
    private static String convertingReversePolishNotation(String userExpression) throws Exception {
        StringBuilder sbStack = new StringBuilder(""), sbOut = new StringBuilder("");
        char charInput, charTemporary;

        for (int i = 0; i < userExpression.length(); i++) {
            charInput = userExpression.charAt(i);
            if (isOperator(charInput)) {
                while (sbStack.length() > 0) {
                    charTemporary = sbStack.substring(sbStack.length() - 1).charAt(0);
                    if (isOperator(charTemporary) && (operatorPriority(charInput) <= operatorPriority(charTemporary))) {
                        sbOut.append(" ").append(charTemporary).append(" ");
                        sbStack.setLength(sbStack.length() - 1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbStack.append(charInput);
            } else if ('(' == charInput) {
                sbStack.append(charInput);
            } else if (')' == charInput) {
                charTemporary = sbStack.substring(sbStack.length() - 1).charAt(0);
                while ('(' != charTemporary) {
                    if (sbStack.length() < 1) {
                        throw new Exception("Error parsing parentheses. Check if the expression is correct");
                    }
                    sbOut.append(" ").append(charTemporary);
                    sbStack.setLength(sbStack.length() - 1);
                    charTemporary = sbStack.substring(sbStack.length() - 1).charAt(0);
                }
                sbStack.setLength(sbStack.length() - 1);
            } else {
                // If the character is not an operator, add it to the output sequence
                sbOut.append(charInput);
            }
        }

        // If there are operators left on the stack, add them to the input line
        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length() - 1));
            sbStack.setLength(sbStack.length() - 1);
        }

        return sbOut.toString();
    }

    /**
     * The function checks if the current character is an operator
     */
    private static boolean isOperator(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
            case '%':
                return true;
        }
        return false;
    }

    /**
     * Returns the priority of the operation
     *
     * @param op char
     * @return byte
     */
    private static byte operatorPriority(char op) {
        switch (op) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1; //  + and -
    }

    /**
     * Considers an expression written in reverse Polish notation
     *
     * @param sIn
     * @return double result
     */
    private static double calculatingExpression(String sIn) throws Exception {
        double dA = 0, dB = 0;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<Double>();
        StringTokenizer st = new StringTokenizer(sIn);

        while (st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOperator(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Invalid amount of data on the stack for the operation " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        case '%':
                            dA %= dB;
                            break;
                        case '^':
                            dA = Math.pow(dA, dB);
                            break;
                        default:
                            throw new Exception("Invalid operation " + sTmp);
                    }
                    stack.push(dA);
                } else {
                    dA = Double.parseDouble(sTmp);
                    stack.push(dA);
                }
            } catch (Exception e) {
                throw new Exception("Invalid symbol in expression ");
            }
        }

        if (stack.size() > 1) {
            throw new Exception("The number of operators does not match the number of operands ");
        }

        return stack.pop();
    }
}
