import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        Stack<Integer> stack = new Stack<>();

        int size = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < size; i++) {
            stack.push(Integer.parseInt(scanner.nextLine()));
        }

        while (!stack.empty()) {
            System.out.println(stack.pop());
        }
    }
}