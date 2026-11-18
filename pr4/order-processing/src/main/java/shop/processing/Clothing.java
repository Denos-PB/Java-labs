package shop.processing;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public final class Clothing extends Product {
    private final String size;

    @Builder
    public Clothing(String name, BigDecimal price, String size) {
        super(name, price);
        this.size = size;
    }
}
