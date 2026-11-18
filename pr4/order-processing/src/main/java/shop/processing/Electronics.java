package shop.processing;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public final class Electronics extends Product {
    private final int warrantyMonths;

    @Builder
    public Electronics(String name, BigDecimal price, int warrantyMonths) {
        super(name, price);
        this.warrantyMonths = warrantyMonths;
    }
}
