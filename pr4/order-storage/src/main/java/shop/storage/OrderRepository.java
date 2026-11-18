package shop.storage;

import shop.processing.Order;
import shop.processing.Product;
import shop.storage.internal.ConcurrentOrderStore;
import java.util.List;
import java.util.Objects;

public final class OrderRepository<T extends Product> {
    private final ConcurrentOrderStore<T> store = new ConcurrentOrderStore<>();

    public void save(Order<T> order) {
        Objects.requireNonNull(order, "Замовлення не може бути null");
        Objects.requireNonNull(order.id(), "ID не може бути null");
        store.save(order);
    }

    public List<Order<T>> findAll() {
        return store.findAll();
    }
}
