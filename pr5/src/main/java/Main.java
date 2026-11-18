import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String input;
        if (args.length > 0) {
            input = String.join(" ", args);
        } else {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введіть рядок для пошуку TEST (латинські великі літери):");
                if (!scanner.hasNextLine()) {
                    System.out.println("Вхідні дані відсутні.");
                    return;
                }
                input = scanner.nextLine();
            }
        }
        State original = new OriginalTestAutomaton().recognize(input);
        State corrected = new TestAutomaton().recognize(input);
        System.out.println("Стан за початковою схемою: " + original);
        System.out.println("Кінцевий стан виправленого автомата: " + corrected);
        System.out.println(corrected == State.F ? "TEST знайдено." : "TEST не знайдено.");
    }
}
