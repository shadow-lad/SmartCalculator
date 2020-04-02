import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        Deque<Integer> queue = new ArrayDeque<>();

        int size = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < size; i++) {
            int number = Integer.parseInt(scanner.nextLine());
            if (number % 2 == 0) {
                queue.addFirst(number);
            } else {
                queue.addLast(number);
            }
        }

        for (Integer number : queue) {
            System.out.println(number);
        }

    }
}