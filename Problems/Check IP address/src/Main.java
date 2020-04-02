import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        String zeroTo255 = "(1?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])";
        String regex = String.join("\\.", zeroTo255, zeroTo255, zeroTo255, zeroTo255);

        System.out.println(input.matches(regex) ? "YES" : "NO");

    }
}