package shop.storage.internal;

import shop.processing.Order;
import shop.processing.OrderProcessingException;
import shop.processing.Product;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Пакет internal не експортується іншим Java-модулям.
public final class ConcurrentOrderStore<T extends Product> {
    private final ConcurrentMap<String, Order<T>> orders = new ConcurrentHashMap<>();

    public void save(Order<T> order) {
        Order<T> previous = orders.putIfAbsent(order.id(), order);
        if (previous != null) {
            throw new OrderProcessingException("Замовлення з ID " + order.id() + " уже існує.");
        }
    }

    public List<Order<T>> findAll() {
        return List.copyOf(orders.values());
    }
}
