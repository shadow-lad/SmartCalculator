import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        String keys = scanner.nextLine();

        String values = scanner.nextLine();

        Map<Character, Character> encodeKey = new HashMap<>();
        Map<Character, Character> decodeKey = new HashMap<>();

        for (int i = 0; i < keys.length(); i++) {
            encodeKey.put(keys.charAt(i), values.charAt(i));
            decodeKey.put(values.charAt(i), keys.charAt(i));
        }

        String encode = scanner.nextLine();

        String decode = scanner.nextLine();

        StringBuilder encoded = new StringBuilder();
        StringBuilder decoded = new StringBuilder();

        for (int i = 0; i < encode.length(); i++) {
            encoded.append(encodeKey.get(encode.charAt(i)));
        }

        System.out.println(encoded);

        for (int i = 0; i < decode.length(); i++) {
            decoded.append(decodeKey.get(decode.charAt(i)));
        }

        System.out.println(decoded);

    }
}