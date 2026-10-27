import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {

    private static final double MONEY_PER_STAR = 1000.0;
    private static final int MAX_STARS = 80;

    private TransactionReportGenerator() {
    }

    public static void printTransactions(List<Transaction> transactions) {
        System.out.println("Прочитані транзакції:");
        transactions.forEach(System.out::println);
    }

    public static void printBalanceReport(double totalBalance) {
        System.out.printf(
                Locale.ROOT,
                "%nЗагальний баланс: %.2f грн%n",
                totalBalance
        );
    }

    public static void printTransactionsCountByMonth(
            String monthYear,
            int count) {

        System.out.printf(
                "Кількість транзакцій за %s: %d%n",
                monthYear,
                count
        );
    }

    public static void printTopExpensesReport(
            List<Transaction> topExpenses) {

        System.out.println("\n10 найбільших витрат:");

        if (topExpenses.isEmpty()) {
            System.out.println("Витрат немає.");
            return;
        }

        topExpenses.forEach(transaction ->
                System.out.printf(
                        Locale.ROOT,
                        "%s | %s | %.2f грн%n",
                        transaction.getDate(),
                        transaction.getDescription(),
                        -transaction.getAmount()
                ));
    }

    public static void printPeriodExpensesReport(
            LocalDate from,
            LocalDate to,
            Optional<Transaction> largest,
            Optional<Transaction> smallest) {

        System.out.printf("%nВитрати за період %s — %s:%n", from, to);

        printExpense("Найбільша витрата", largest);
        printExpense("Найменша витрата", smallest);
    }

    private static void printExpense(
            String label,
            Optional<Transaction> expense) {

        if (expense.isEmpty()) {
            System.out.println(label + ": немає");
            return;
        }

        Transaction transaction = expense.get();

        System.out.printf(
                Locale.ROOT,
                "%s: %s | %s | %.2f грн%n",
                label,
                transaction.getDate(),
                transaction.getDescription(),
                -transaction.getAmount()
        );
    }

    public static void printExpensesSummary(
            String title,
            Map<?, Double> expenses) {

        System.out.println("\n" + title);
        System.out.println(
                "* = 1000 грн; неповні 1000 не відображаються зіркою."
        );

        if (expenses.isEmpty()) {
            System.out.println("Витрат немає.");
            return;
        }

        expenses.forEach((label, amount) -> {
            double fullStars = Math.floor(amount / MONEY_PER_STAR);
            int visibleStars = (int) Math.min(fullStars, MAX_STARS);

            String bar = "*".repeat(visibleStars);

            if (fullStars > MAX_STARS) {
                bar += "...";
            }

            System.out.printf(
                    Locale.ROOT,
                    "%-20s | %10.2f грн | %s%n",
                    label,
                    amount,
                    bar
            );
        });
    }
}