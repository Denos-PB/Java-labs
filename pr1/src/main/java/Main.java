import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Category electronics = new Category(1, "Електроніка");
        Category accessories = new Category(2, "Аксесуари");

        Product laptop = new Product(
                1,
                "Ноутбук",
                19999.99,
                "Ноутбук для роботи та навчання",
                electronics
        );

        Product headphones = new Product(
                2,
                "Навушники",
                2499.00,
                "Бездротові навушники",
                accessories
        );

        List<Product> catalog = new ArrayList<>();
        catalog.add(laptop);
        catalog.add(headphones);

        Cart cart = new Cart();
        List<Order> orders = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== ІНТЕРНЕТ-МАГАЗИН =====");
                System.out.println("1 — Переглянути каталог");
                System.out.println("2 — Додати товар до кошика");
                System.out.println("3 — Переглянути кошик");
                System.out.println("4 — Оформити замовлення");
                System.out.println("5 — Видалити товар з кошика");
                System.out.println("6 — Переглянути історію замовлень");
                System.out.println("7 — Пошук товарів");
                System.out.println("0 — Вийти");

                Integer choice = readNumber(scanner, "Ваш вибір: ");

                if (choice == null) {
                    return;
                }

                switch (choice) {
                    case 1 -> {
                        printProducts(catalog);
                    }

                    case 2 -> {
                        Integer id = readNumber(scanner, "ID товару: ");

                        if (id == null) {
                            return;
                        }

                        Product product = findProduct(catalog, id);

                        if (product == null) {
                            System.out.println("Товар не знайдено.");
                        } else {
                            cart.addProduct(product);
                            System.out.println("Додано: " + product.getName());
                        }
                    }

                    case 3 -> {
                        if (cart.getProducts().isEmpty()) {
                            System.out.println("Кошик порожній.");
                        } else {
                            printProducts(cart.getProducts());

                            System.out.printf(
                                    "Разом: %.2f грн%n",
                                    cart.getTotalPrice()
                            );
                        }
                    }

                    case 4 -> {
                        if (cart.getProducts().isEmpty()) {
                            System.out.println(
                                    "Спочатку додайте товари до кошика."
                            );
                        } else {
                            Order order = new Order(cart);
                            orders.add(order);
                            cart.clear();

                            System.out.println("Замовлення оформлено!");
                            System.out.println("Статус: " + order.getStatus());

                            System.out.printf(
                                    "Сума: %.2f грн%n",
                                    order.getTotalPrice()
                            );
                        }
                    }

                    case 5 -> {
                        Integer id = readNumber(
                                scanner,
                                "ID товару для видалення: "
                        );

                        if (id == null) {
                            return;
                        }

                        Product product = findProduct(
                                cart.getProducts(),
                                id
                        );

                        if (product == null) {
                            System.out.println("Цього товару немає в кошику.");
                        } else {
                            cart.removeProduct(product);
                            System.out.println("Видалено: " + product.getName());
                        }
                    }

                    case 6 -> {
                        if (orders.isEmpty()) {
                            System.out.println("Історія замовлень порожня.");
                        } else {
                            for (int i = 0; i < orders.size(); i++) {
                                Order order = orders.get(i);

                                System.out.println("\nЗамовлення №" + (i + 1));
                                printProducts(order.getProducts());

                                System.out.printf(
                                        "Сума: %.2f грн%n",
                                        order.getTotalPrice()
                                );

                                System.out.println(
                                        "Статус: " + order.getStatus()
                                );
                            }
                        }
                    }

                    case 7 -> {
                        System.out.print("Назва товару або категорії: ");

                        if (!scanner.hasNextLine()) {
                            return;
                        }

                        String query = scanner.nextLine()
                                .trim()
                                .toLowerCase(Locale.ROOT);

                        if (query.isEmpty()) {
                            System.out.println("Пошуковий запит порожній.");
                        } else {
                            List<Product> results = new ArrayList<>();

                            for (Product product : catalog) {
                                String name = product.getName()
                                        .toLowerCase(Locale.ROOT);

                                String categoryName = product.getCategory()
                                        .getName()
                                        .toLowerCase(Locale.ROOT);

                                if (name.contains(query)
                                        || categoryName.contains(query)) {
                                    results.add(product);
                                }
                            }

                            printProducts(results);
                        }
                    }

                    case 0 -> {
                        System.out.println("До побачення!");
                        return;
                    }

                    default -> {
                        System.out.println("Невідома опція.");
                    }
                }
            }
        }
    }

    private static Integer readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);

            if (!scanner.hasNextLine()) {
                return null;
            }

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Введіть ціле число.");
            }
        }
    }

    private static Product findProduct(List<Product> products, int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товарів не знайдено.");
            return;
        }

        for (Product product : products) {
            System.out.printf(
                    "ID: %d | %s | %.2f грн | Категорія: %s%n",
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getCategory().getName()
            );

            System.out.println("Опис: " + product.getDescription());
        }
    }
}