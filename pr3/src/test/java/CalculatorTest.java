import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void testAdd() {
        assertEquals(8.0, calculator.add(5, 3), 0.000001);
    }

    @Test
    void testSubtract() {
        assertEquals(2.0, calculator.subtract(5, 3), 0.000001);
    }

    @Test
    void testMultiply() {
        assertEquals(15.0, calculator.multiply(5, 3), 0.000001);
    }

    @Test
    void testDivide() {
        assertEquals(2.5, calculator.divide(5, 2), 0.000001);
    }

    @Test
    void testNegativeNumbersAreAllowed() {
        assertEquals(-8.0, calculator.add(-5, -3), 0.000001);
    }

    @Test
    void testDivideByZero() {
        assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10, 0)
        );
    }

    @Test
    void testDivideByNegativeZero() {
        assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10, -0.0)
        );
    }

    @Test
    void testTextInsteadOfNumber() {
        assertThrows(
                NumberFormatException.class,
                () -> Main.parseNumber("hello")
        );
    }

    @Test
    void testEmptyInput() {
        assertThrows(
                NumberFormatException.class,
                () -> Main.parseNumber("")
        );
    }

    @Test
    void testParseNumberWithComma() {
        assertEquals(3.5, Main.parseNumber("3,5"), 0.000001);
    }

    @Test
    void testAddRejectsNaN() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.add(Double.NaN, 5)
        );
    }

    @Test
    void testSubtractRejectsInfinity() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.subtract(5, Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void testMultiplyRejectsInfinity() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.multiply(Double.NEGATIVE_INFINITY, 5)
        );
    }

    @Test
    void testDivideRejectsNaN() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.divide(5, Double.NaN)
        );
    }
}