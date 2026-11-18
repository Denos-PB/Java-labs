package shop.processing;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

public class OrderProcessor<T extends Product> {
    private final Consumer<Order<T>> saveOrder;

    public OrderProcessor(Consumer<Order<T>> saveOrder) {
        this.saveOrder = Objects.requireNonNull(saveOrder);
    }

    public void process(Order<T> order) {
        validate(order);
        BigDecimal total = order.product().getPrice()
                .multiply(BigDecimal.valueOf(order.quantity()));
        saveOrder.accept(order);
        System.out.printf(
                "[%s] Оброблено %s | %s | кількість: %d | сума: %s грн%n",
                Thread.currentThread().getName(), order.id(),
                order.product().getName(), order.quantity(), total.toPlainString());
    }

    private void validate(Order<T> order) {
        if (order == null) {
            throw new OrderProcessingException("Замовлення відсутнє.");
        }
        if (order.id() == null || order.id().isBlank()) {
            throw new OrderProcessingException("Відсутній ID замовлення.");
        }
        Product product = order.product();
        if (product == null) {
            throw new OrderProcessingException(order.id() + ": товар відсутній.");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new OrderProcessingException(order.id() + ": назва товару порожня.");
        }
        if (product.getPrice() == null || product.getPrice().signum() <= 0) {
            throw new OrderProcessingException(order.id() + ": ціна повинна бути більшою за нуль.");
        }
        if (order.quantity() <= 0) {
            throw new OrderProcessingException(order.id() + ": кількість повинна бути більшою за нуль.");
        }
        if (product instanceof Electronics electronics && electronics.getWarrantyMonths() < 0) {
            throw new OrderProcessingException(order.id() + ": гарантія не може бути від'ємною.");
        }
        if (product instanceof Clothing clothing
                && (clothing.getSize() == null || clothing.getSize().isBlank())) {
            throw new OrderProcessingException(order.id() + ": розмір одягу не вказаний.");
        }
    }
}
