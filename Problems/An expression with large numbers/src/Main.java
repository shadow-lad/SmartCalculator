import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");

        BigInteger number1 = new BigInteger(input[0]);
        BigInteger number2 = new BigInteger(input[1]);
        BigInteger number3 = new BigInteger(input[2]);
        BigInteger number4 = new BigInteger(input[3]);

        System.out.println(number1.negate().multiply(number2).add(number3).subtract(number4));

    }
}