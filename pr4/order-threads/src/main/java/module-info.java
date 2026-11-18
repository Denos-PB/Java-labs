module order.threads {
    requires order.processing;
    requires order.storage;
    requires javafaker;
    // SnakeYAML (залежність JavaFaker) використовує java.sql.Date.
    requires java.sql;
    exports shop.threads;
}
