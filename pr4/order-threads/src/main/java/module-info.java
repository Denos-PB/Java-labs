module order.threads {
    requires order.processing;
    requires order.storage;
    requires javafaker;
    requires java.sql;
    exports shop.threads;
}
