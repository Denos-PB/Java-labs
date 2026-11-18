package shop.processing;

public record Order<T extends Product>(String id, T product, int quantity) {
}
