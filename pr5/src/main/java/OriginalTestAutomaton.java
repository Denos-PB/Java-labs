import java.util.Objects;

public final class OriginalTestAutomaton {

    public State recognize(String input) {
        Objects.requireNonNull(input, "Вхідний рядок не може бути null");
        State state = State.S;
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);
            state = switch (state) {
                case S -> symbol == 'T' ? State.ONE : State.S;
                case ONE -> symbol == 'E' ? State.TWO : State.S;
                case TWO -> symbol == 'S' ? State.THREE : State.S;
                case THREE -> symbol == 'T' ? State.F : State.S;
                case F -> State.F;
            };
        }
        return state;
    }
}
