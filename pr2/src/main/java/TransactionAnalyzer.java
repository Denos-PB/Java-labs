import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private TransactionAnalyzer() {
    }

    public static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException secondException) {
                throw new IllegalArgumentException(
                        "Дата повинна бути dd-MM-yyyy або yyyy-MM-dd: " + value,
                        secondException
                );
            }
        }
    }

    public static double calculateTotalBalance(
            List<Transaction> transactions) {

        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static int countTransactionsByMonth(
            List<Transaction> transactions,
            String monthYear) {

        YearMonth target = YearMonth.parse(monthYear, MONTH_FORMAT);

        return (int) transactions.stream()
                .filter(transaction ->
                        YearMonth.from(parseDate(transaction.getDate()))
                                .equals(target))
                .count();
    }

    public static List<Transaction> findTopExpenses(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(transaction -> transaction.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    private static Stream<Transaction> expensesInPeriod(
            List<Transaction> transactions,
            LocalDate from,
            LocalDate to) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Початкова дата не може бути пізніше кінцевої"
            );
        }

        return transactions.stream()
                .filter(transaction -> transaction.getAmount() < 0)
                .filter(transaction -> {
                    LocalDate date = parseDate(transaction.getDate());

                    return !date.isBefore(from) && !date.isAfter(to);
                });
    }

    public static Optional<Transaction> findLargestExpense(
            List<Transaction> transactions,
            LocalDate from,
            LocalDate to) {

        // Найбільша витрата має найбільш від'ємну суму.
        return expensesInPeriod(transactions, from, to)
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findSmallestExpense(
            List<Transaction> transactions,
            LocalDate from,
            LocalDate to) {

        // Найменша витрата має від'ємну суму, найближчу до нуля.
        return expensesInPeriod(transactions, from, to)
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> calculateExpensesByCategory(
            List<Transaction> transactions) {

        Map<String, Double> result = new TreeMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                result.merge(
                        transaction.getDescription(),
                        -transaction.getAmount(),
                        Double::sum
                );
            }
        }

        return result;
    }

    public static Map<YearMonth, Double> calculateExpensesByMonth(
            List<Transaction> transactions) {

        Map<YearMonth, Double> result = new TreeMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                YearMonth month =
                        YearMonth.from(parseDate(transaction.getDate()));

                result.merge(month, -transaction.getAmount(), Double::sum);
            }
        }

        return result;
    }
}