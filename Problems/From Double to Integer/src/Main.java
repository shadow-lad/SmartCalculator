import java.util.*;

public class Main {

    public static int convert(Double val) {
        // write your code here
        return val.intValue();
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Double doubleVal = scanner.nextDouble() / scanner.nextDouble();
        System.out.println(convert(doubleVal));
    }
}