import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Order {
    @Getter(AccessLevel.NONE)
    private final List<Product> products;

    private final double totalPrice;

    @Setter
    private String status;

    public Order(Cart cart) {
        if (cart.getProducts().isEmpty()) {
            throw new IllegalArgumentException(
                    "Не можна створити замовлення з порожнього кошика"
            );
        }

        this.products = cart.getProducts();
        this.totalPrice = cart.getTotalPrice();
        this.status = "Нове";
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}