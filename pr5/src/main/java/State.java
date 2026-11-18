public enum State {
    S("S"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    F("F");

    private final String label;

    State(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
