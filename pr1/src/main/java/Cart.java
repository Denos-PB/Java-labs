import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Cart {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public double getTotalPrice() {
        double total = 0;

        for (Product product : products) {
            total += product.getPrice();
        }

        return total;
    }

    public void clear() {
        products.clear();
    }
}