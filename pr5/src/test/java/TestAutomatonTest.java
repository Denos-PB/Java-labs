import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestAutomatonTest {
    private final TestAutomaton automaton = new TestAutomaton();

    static Stream<Arguments> inputs() {
        return Stream.of(
                Arguments.of("", State.S),
                Arguments.of("abc", State.S),
                Arguments.of("   ", State.S),
                Arguments.of("T", State.ONE),
                Arguments.of("TE", State.TWO),
                Arguments.of("TES", State.THREE),
                Arguments.of("TEST", State.F),
                Arguments.of("abcTESTabc", State.F),
                Arguments.of("abcTES", State.THREE),
                Arguments.of("TESTabc", State.F),
                Arguments.of("abcTEST", State.F),
                Arguments.of("TESTTEST", State.F),
                Arguments.of("TESTES", State.F),
                Arguments.of("test", State.S),
                Arguments.of("Test", State.S),
                Arguments.of("T E S T", State.ONE),
                Arguments.of("TE!ST", State.ONE),
                Arguments.of("TEX", State.S),
                Arguments.of("TESX", State.S),
                Arguments.of("abcT", State.ONE),
                Arguments.of("abcTE", State.TWO),
                Arguments.of("TT", State.ONE),
                Arguments.of("TET", State.ONE),
                Arguments.of("TTES", State.THREE),
                Arguments.of("TETES", State.THREE),
                Arguments.of("\nTEST\t", State.F),
                Arguments.of("Привіт TEST!", State.F),
                // Перша літера тут кирилична Т, а не латинська T.
                Arguments.of("\u0422EST", State.ONE),
                Arguments.of("😀TEST😀", State.F)
        );
    }

    @ParameterizedTest(name = "[{index}] input={0}, expected={1}")
    @MethodSource("inputs")
    void recognizesFinalState(String input, State expected) {
        assertEquals(expected, automaton.recognize(input));
    }

    @ParameterizedTest
    @CsvSource({
            "S,T,ONE", "S,E,S", "S,S,S", "S,X,S",
            "ONE,T,ONE", "ONE,E,TWO", "ONE,S,S", "ONE,X,S",
            "TWO,T,ONE", "TWO,E,S", "TWO,S,THREE", "TWO,X,S",
            "THREE,T,F", "THREE,E,S", "THREE,S,S", "THREE,X,S",
            "F,T,F", "F,E,F", "F,S,F", "F,X,F"
    })
    void checksEveryTransition(State before, char symbol, State expected) {
        assertEquals(expected, automaton.nextState(before, symbol));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "TTEST", "TETEST", "TTTTEST", "TTTTTTEST",
            "TETETETEST", "abcTTESTabc", "abcTETESTabc"
    })
    void fixesCounterexamplesFromOriginalDiagram(String input) {
        assertTrue(input.contains("TEST"));
        assertNotEquals(State.F, new OriginalTestAutomaton().recognize(input));
        assertEquals(State.F, automaton.recognize(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TEST", "abcTESTabc", "TESTTEST", "TESTTES"})
    void acceptingStateCannotBeLost(String input) {
        assertEquals(State.F, automaton.recognize(input));
    }

    @Test
    void rejectsNullInput() {
        assertThrows(NullPointerException.class, () -> automaton.recognize(null));
    }

    @Test
    void rejectsNullState() {
        assertThrows(NullPointerException.class, () -> automaton.nextState(null, 'T'));
    }

    @Test
    void startsFromInitialStateOnEveryCall() {
        assertEquals(State.F, automaton.recognize("TEST"));
        assertEquals(State.S, automaton.recognize(""));
        assertEquals(State.THREE, automaton.recognize("TES"));
    }
    @ParameterizedTest(name = "All strings of length {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void matchesContainsForEveryShortString(int length) {
        char[] alphabet = {'T', 'E', 'S', 'X'};
        int combinations = 1 << (2 * length);
        for (int code = 0; code < combinations; code++) {
            int value = code;
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < length; i++) {
                input.append(alphabet[value % alphabet.length]);
                value /= alphabet.length;
            }
            String text = input.toString();
            assertEquals(text.contains("TEST"), automaton.recognize(text) == State.F,
                    () -> "Wrong recognition for: " + text);
        }
    }
}
