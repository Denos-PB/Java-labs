import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = List.of(
                new Transaction("01-01-2024", 100, "Дохід"),
                new Transaction("02-01-2024", -50, "Продукти"),
                new Transaction("03-01-2024", 150, "Дохід")
        );

        double result =
                TransactionAnalyzer.calculateTotalBalance(transactions);

        assertEquals(200.0, result, 0.000001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = List.of(
                new Transaction("01-02-2023", 50, "Дохід"),
                new Transaction("15-02-2023", -20, "Продукти"),
                new Transaction("05-03-2023", 100, "Дохід"),
                new Transaction("01-02-2024", -30, "Транспорт")
        );

        assertEquals(
                2,
                TransactionAnalyzer.countTransactionsByMonth(
                        transactions, "02-2023"
                )
        );

        assertEquals(
                1,
                TransactionAnalyzer.countTransactionsByMonth(
                        transactions, "03-2023"
                )
        );

        assertEquals(
                0,
                TransactionAnalyzer.countTransactionsByMonth(
                        transactions, "04-2023"
                )
        );
    }

    @Test
    void testReadTransactionsFromCSV(@TempDir Path directory)
            throws IOException {

        Path file = directory.resolve("transactions.csv");

        Files.writeString(file, """
                date,amount,description
                01-01-2024,1000,Дохід
                02-01-2024,-250,"Продукти, напої"
                """, StandardCharsets.UTF_8);

        List<Transaction> transactions =
                TransactionCSVReader.readTransactions(file.toString());

        assertEquals(
                List.of(
                        new Transaction("01-01-2024", 1000, "Дохід"),
                        new Transaction("02-01-2024", -250, "Продукти, напої")
                ),
                transactions
        );
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            transactions.add(
                    new Transaction("01-01-2024", -i * 100, "Витрата " + i)
            );
        }

        transactions.add(
                new Transaction("01-01-2024", 5000, "Дохід")
        );

        List<Transaction> top =
                TransactionAnalyzer.findTopExpenses(transactions);

        List<Double> amounts = top.stream()
                .map(Transaction::getAmount)
                .toList();

        assertEquals(
                List.of(
                        -1200.0, -1100.0, -1000.0, -900.0, -800.0,
                        -700.0, -600.0, -500.0, -400.0, -300.0
                ),
                amounts
        );
    }
}