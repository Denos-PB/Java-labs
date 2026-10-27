import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        try (Scanner scanner = new Scanner(System.in)) {
            try {
                System.out.print("Введіть перше число: ");
                double a = parseNumber(scanner.nextLine());

                System.out.print("Введіть друге число: ");
                double b = parseNumber(scanner.nextLine());

                System.out.print("Введіть операцію (+, -, *, /): ");
                String operation = scanner.nextLine().trim();

                double result = switch (operation) {
                    case "+" -> calculator.add(a, b);
                    case "-" -> calculator.subtract(a, b);
                    case "*" -> calculator.multiply(a, b);
                    case "/" -> calculator.divide(a, b);
                    default -> throw new InvalidInputException(
                            "Невідома операція. Доступні: +, -, *, /."
                    );
                };

                System.out.println("Результат: " + result);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Помилка: введіть число, наприклад 12 або 3.5."
                );
            } catch (ArithmeticException e) {
                System.out.println("Арифметична помилка: " + e.getMessage());
            } catch (InvalidInputException e) {
                System.out.println("Некоректні дані: " + e.getMessage());
            } finally {
                System.out.println("Обробку запиту завершено.");
            }
        }
    }

    public static double parseNumber(String input) {
        return Double.parseDouble(input.trim().replace(',', '.'));
    }
}