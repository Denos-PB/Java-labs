import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    private TransactionCSVReader() {
    }

    public static List<Transaction> readTransactions(String source)
            throws IOException {

        try (Reader reader = DataSourceReader.open(source)) {
            return parseTransactions(reader);
        }
    }

    public static List<Transaction> parseTransactions(Reader reader)
            throws IOException {

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setTrim(true)
                .setIgnoreEmptyLines(true)
                .build();

        CSVParser parser = format.parse(reader);
        List<Transaction> transactions = new ArrayList<>();

        for (CSVRecord record : parser) {
            if (record.size() != 3) {
                throw new IllegalArgumentException(
                        "Запис " + record.getRecordNumber()
                                + ": очікується 3 поля"
                );
            }

            String date = record.get(0).replace("\uFEFF", "").trim();

            if (record.getRecordNumber() == 1
                    && date.equalsIgnoreCase("date")
                    && record.get(1).equalsIgnoreCase("amount")
                    && record.get(2).equalsIgnoreCase("description")) {
                continue;
            }

            try {
                TransactionAnalyzer.parseDate(date);

                double amount = Double.parseDouble(record.get(1));

                if (!Double.isFinite(amount)) {
                    throw new IllegalArgumentException("Некоректна сума");
                }

                String description = record.get(2);

                if (description.isBlank()) {
                    throw new IllegalArgumentException("Порожній опис");
                }

                transactions.add(
                        new Transaction(date, amount, description)
                );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Некоректний запис " + record.getRecordNumber()
                                + ": " + record,
                        e
                );
            }
        }

        return transactions;
    }
}