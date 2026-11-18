package shop.app;

import com.github.javafaker.Faker;
import shop.processing.Clothing;
import shop.processing.Electronics;
import shop.processing.Order;
import shop.processing.OrderProcessor;
import shop.storage.OrderRepository;
import shop.threads.OrderThreadManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker(Locale.ENGLISH);
        OrderRepository<Electronics> electronicsRepository = new OrderRepository<>();
        OrderRepository<Clothing> clothingRepository = new OrderRepository<>();

        // Посилання на методи збереження передаємо обробникам.
        OrderProcessor<Electronics> electronicsProcessor =
                new OrderProcessor<>(electronicsRepository::save);
        OrderProcessor<Clothing> clothingProcessor =
                new OrderProcessor<>(clothingRepository::save);

        List<Runnable> tasks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Electronics product = Electronics.builder()
                    .name("Electronics: " + faker.commerce().productName())
                    .price(randomPrice(faker))
                    .warrantyMonths(faker.number().numberBetween(6, 37))
                    .build();
            Order<Electronics> order = new Order<>(
                    "E-" + i, product, faker.number().numberBetween(1, 4));
            // Лямбда виконається у робочому потоці.
            tasks.add(() -> electronicsProcessor.process(order));
        }
        for (int i = 1; i <= 5; i++) {
            Clothing product = Clothing.builder()
                    .name("Clothing: " + faker.commerce().productName())
                    .price(randomPrice(faker))
                    .size(faker.options().option("S", "M", "L", "XL"))
                    .build();
            Order<Clothing> order = new Order<>(
                    "C-" + i, product, faker.number().numberBetween(1, 4));
            tasks.add(() -> clothingProcessor.process(order));
        }
        Electronics invalidProduct = Electronics.builder()
                .name("Некоректний товар")
                .price(new BigDecimal("-100.00"))
                .warrantyMonths(12)
                .build();
        Order<Electronics> invalidOrder = new Order<>("E-INVALID", invalidProduct, 1);
        tasks.add(() -> electronicsProcessor.process(invalidOrder));

        try (OrderThreadManager manager = new OrderThreadManager(3)) {
            int successful = manager.processAll(tasks);
            System.out.println("\nУспішно оброблено: " + successful);
            System.out.println("З помилкою: " + (tasks.size() - successful));
        }
        System.out.println("\nЗбережені замовлення електроніки:");
        electronicsRepository.findAll().forEach(Main::printOrder);
        System.out.println("\nЗбережені замовлення одягу:");
        clothingRepository.findAll().forEach(Main::printOrder);
    }

    private static BigDecimal randomPrice(Faker faker) {
        long cents = faker.number().numberBetween(10_000, 500_001);
        return BigDecimal.valueOf(cents, 2);
    }

    private static void printOrder(Order<?> order) {
        System.out.printf("%s | %s | кількість: %d%n",
                order.id(), order.product().getName(), order.quantity());
    }
}
