package shop.processing;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public abstract class Product {
    private final String name;
    private final BigDecimal price;

    protected Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
