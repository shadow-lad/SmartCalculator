package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    final static protected Map<String, BigInteger> variables = new HashMap<>();

    final static protected AtomicBoolean running = new AtomicBoolean(true);

    final static private Pattern VARIABLE_PATTERN = Pattern.compile("(\\w*\\s*=*\\s*[a-zA-Z]*\\d*)*");
    final static private Pattern EQUATION_PATTERN = Pattern.compile("([+\\-]*\\s*\\(*\\s*\\w*\\s*[+\\-*^/]*\\s*\\w*\\s*\\)*\\s*[+\\-]*\\s*)*");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // put your code here

        while (running.get()) {

            String input = scanner.nextLine().trim();

            Matcher variableMatcher = VARIABLE_PATTERN.matcher(input);
            Matcher equationMatcher = EQUATION_PATTERN.matcher(input);

            if (input.length() == 0)
                continue;

            if(input.startsWith("/")) {
                Command command = new Command(input);
                command.doCommand();
            } else if (isNum(input)) {
                System.out.println(input);
            } else if (variableMatcher.matches()) {
                Variable variable = new Variable(input);
                variable.assignValue();
            } else if (equationMatcher.matches()) {
                Expression expression = new Expression(input);
                expression.solveExpression();
            } else {
                System.out.println("Invalid expression");
            }

        }

    }

    private static boolean isNum(String str) {
        try {
            new BigInteger(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}

abstract class Input {

    private String input;

    public Input(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

}

class Command extends Input {

    private static final String HELP_MESSAGE = "Even number of '-' is '+' and odd number of '-' is '-'.\n" +
            "Any number of '+' is the same as '+'\n" +
            "You can assign variables so: n=5";

    enum Commands {
        EXIT,
        HELP;

        protected static Commands getCommand(String command) {
            switch (command){
                case "/exit": return EXIT;
                case "/help": return HELP;
                default: return null;
            }
        }

    }

    public Command(String input) {
        super(input);
    }

    public void doCommand() {
        Commands command = Commands.getCommand(this.getInput());
        if (command == Commands.EXIT) {
            System.out.println("Bye!");
            Main.running.set(false);
        } else if (command == Commands.HELP) {
            System.out.println(HELP_MESSAGE);
        } else {
            System.out.println("Unknown command");
        }
    }

}

class Expression extends Input {

    private final static Pattern INVALID_OPERATION_PATTERN = Pattern.compile("(/(\\s*/)+|\\*(\\s*\\*)+|\\^(\\s*\\^)+2)");

    public Expression(String input) {
        super(input);
    }

    public void solveExpression() {


        if(!isValidExpression(getInput())){
            System.out.println("Invalid expression");
            return;
        }

        String equation = "( "+simplifyExpression(getInput())+ " )";

        String[] expression = equation.split("\\s+");

        String[] postFix = infixToPostFix(expression);

        System.out.println(solvePostFix(postFix));

    }

    public BigInteger getValue(String key) {

        if(!key.matches("[a-zA-Z]+")) {
            System.out.println("Invalid expression");
            return null;
        } else if (!Main.variables.containsKey(key)) {
            System.out.println("Invalid expression");
            return null;
        } else {
            return Main.variables.get(key);
        }

    }

    private BigInteger solvePostFix(String[] expression) throws IllegalArgumentException {

        Stack<BigInteger> stack = new Stack<>();
        for (String token : expression) {
            if (token.matches("([a-zA-Z]+|[0-9]+)")) {
                stack.push(token.matches("[a-zA-Z]+") ? getValue(token) : new BigInteger(token));
            } else {
                BigInteger num2 = stack.pop();
                BigInteger num1 = stack.pop();
                BigInteger result = doOperation(num1, num2, token.charAt(0));
                if ( result == null ) {
                    throw new IllegalArgumentException();
                }
                stack.push(result);
            }
        }

        return stack.pop();
    }

    private BigInteger doOperation (BigInteger x, BigInteger y, char operator) {
        switch (operator) {
            case '+': return add(x, y);
            case '-': return subtract(x, y);
            case '*': return multiply(x, y);
            case '/': return divide(x, y);
            case '^': return power(x, y);
            default: return null;
        }
    }

    private BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    private BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    private BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    private BigInteger divide(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    private BigInteger power(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.ZERO))
            return BigInteger.ONE;
        return x.multiply(power(x, y.subtract(BigInteger.ONE)));
    }

    private String simplifyExpression(String input) {

        input = input.replaceAll("\\s*((\\+\\s*)+|(-\\s*-\\s*)+)\\s*", " + ")
                .replaceAll("\\s*(-\\s*(-\\s*-\\s*)*|\\+\\s*-|-\\s*\\+)\\s*", " - ")
                .replaceAll("\\s*\\*\\s*", " * ")
                .replaceAll("\\s*/\\s*", " / ")
                .replaceAll("\\s*\\^\\s*", " ^ ")
                .replaceAll("\\s*\\(\\s*", " ( ")
                .replaceAll("\\s*\\)\\s*", " ) ");

        return input.trim();

    }

    private boolean isValidExpression(String expression){

        Matcher invalidOperationMatcher = INVALID_OPERATION_PATTERN.matcher(expression);
        String operators = "+-*/^";

        return ((expression.contains("+") || expression.contains("-") ||
                expression.contains("*") || expression.contains("/") ||
                expression.contains("^")) && !invalidOperationMatcher.find() &&
                countChars(expression, '(') == countChars(expression, ')') &&
                !(operators.contains(""+expression.charAt(0)) || operators.contains(""+expression.charAt(expression.length()-1))));

    }

    private String[] infixToPostFix(String[] expression) {
        Stack<String> stack = new Stack<>();
        String[] postFix = new String[expression.length - (countBrackets(expression)*2)];
        int i = 0;
        for (String token : expression) {
            if (token.matches("[a-zA-Z0-9]+")) {
                postFix[i++] = token;
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while(!stack.peek().equals("(")) {
                    postFix[i++] = stack.pop();
                }
                stack.pop();
            } else {
                if (getPrecedence(stack.peek().charAt(0)) >= getPrecedence(token.charAt(0))) {
                    postFix[i++] = stack.pop();
                }
                stack.push(token);
            }
        }
        return postFix;
    }

    private int countChars(String string, char character) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character) {
                count++;
            }
        }
        return count;
    }

    private int countBrackets(String[] array) {
        StringBuilder string = new StringBuilder();
        for (String s : array ) {
            string.append(s);
        }
        return countChars(string.toString(), '(');
    }

    private int getPrecedence(char c) {
        switch(c) {
            case '-':
            case '+': return 0;
            case '*':
            case '/': return 1;
            case '^': return 2;
            default: return -1;
        }
    }

}

class Variable extends Input {

    public Variable(String input) {
        super(input);
    }

    public void assignValue() {
        String[] input = this.getInput().split("\\s*=\\s*");
        try {
            if(!input[0].matches("[a-zA-Z]+")) {
                System.out.println("Invalid identifier");
                return;
            }

            if (input.length == 1) {
                displayValue(input[0]);
                return;
            }

            if(input.length > 2) {
                System.out.println("Invalid assignment");
                return;
            }

            Main.variables.put(input[0], new BigInteger(input[1]));
        } catch (NumberFormatException ex) {
            if (Main.variables.containsKey(input[1])) {
                Main.variables.put(input[0], Main.variables.get(input[1]));
            } else if (input[1].matches("[a-zA-Z]+")){
                System.out.println("Unknown variable");
            } else {
                System.out.println("Invalid assignment");
            }
        }
    }

    private void displayValue(String key) {
        if (Main.variables.containsKey(key)) {
            System.out.println(Main.variables.get(key));
        } else {
            System.out.println("Unknown variable");
        }
    }

}
