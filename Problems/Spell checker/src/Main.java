import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner sc = new Scanner(System.in);

        int d = Integer.parseInt(sc.nextLine());

        Set<String> words = new HashSet<>();

        for (int i = 0; i < d; i++) {
            words.add(sc.nextLine().toLowerCase());
        }

        Set<String> notInDictionary = new TreeSet<>();

        int l = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < l; i++) {
            String[] line = sc.nextLine().split(" ");
            for (String word : line) {
                if (!words.contains(word.toLowerCase())) {
                    notInDictionary.add(word);
                }
            }
        }

        for (String word : notInDictionary) {
            System.out.println(word);
        }

    }
}