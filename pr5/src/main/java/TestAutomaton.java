import java.util.Objects;

public final class TestAutomaton {

    public State recognize(String input) {
        Objects.requireNonNull(input, "Вхідний рядок не може бути null");
        State state = State.S;
        for (int i = 0; i < input.length(); i++) {
            state = nextState(state, input.charAt(i));
        }
        return state;
    }

    public State nextState(State state, char symbol) {
        Objects.requireNonNull(state, "Стан не може бути null");
        return switch (state) {
            case S -> symbol == 'T' ? State.ONE : State.S;
            case ONE -> {
                if (symbol == 'E') yield State.TWO;
                yield symbol == 'T' ? State.ONE : State.S;
            }
            case TWO -> {
                if (symbol == 'S') yield State.THREE;
                yield symbol == 'T' ? State.ONE : State.S;
            }
            case THREE -> symbol == 'T' ? State.F : State.S;
            case F -> State.F;
        };
    }
}
