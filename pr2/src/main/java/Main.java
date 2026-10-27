import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 0 && args.length != 4) {
            System.err.println(
                    "Використання: <CSV-файл або URL> <MM-yyyy>"
                            + " <yyyy-MM-dd> <yyyy-MM-dd>"
            );
            System.exit(1);
        }

        String source = args.length == 0
                ? "src/main/resources/transactions.csv"
                : args[0];

        String monthYear = args.length == 0 ? "01-2024" : args[1];
        String fromValue = args.length == 0 ? "2024-01-01" : args[2];
        String toValue = args.length == 0 ? "2024-01-31" : args[3];

        try {
            LocalDate from = LocalDate.parse(fromValue);
            LocalDate to = LocalDate.parse(toValue);

            List<Transaction> transactions =
                    TransactionCSVReader.readTransactions(source);

            double balance =
                    TransactionAnalyzer.calculateTotalBalance(transactions);

            int count = TransactionAnalyzer.countTransactionsByMonth(
                    transactions,
                    monthYear
            );

            var largest = TransactionAnalyzer.findLargestExpense(
                    transactions, from, to
            );

            var smallest = TransactionAnalyzer.findSmallestExpense(
                    transactions, from, to
            );

            TransactionReportGenerator.printTransactions(transactions);
            TransactionReportGenerator.printBalanceReport(balance);

            TransactionReportGenerator.printTransactionsCountByMonth(
                    monthYear, count
            );

            TransactionReportGenerator.printTopExpensesReport(
                    TransactionAnalyzer.findTopExpenses(transactions)
            );

            TransactionReportGenerator.printPeriodExpensesReport(
                    from, to, largest, smallest
            );

            TransactionReportGenerator.printExpensesSummary(
                    "Сумарні витрати за категоріями:",
                    TransactionAnalyzer.calculateExpensesByCategory(transactions)
            );

            TransactionReportGenerator.printExpensesSummary(
                    "Сумарні витрати за місяцями:",
                    TransactionAnalyzer.calculateExpensesByMonth(transactions)
            );

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Помилка: " + e.getMessage());
            System.exit(1);
        }
    }
}