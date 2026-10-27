public class Calculator {

    public double add(double a, double b) {
        validateInputs(a, b);
        return a + b;
    }

    public double subtract(double a, double b) {
        validateInputs(a, b);
        return a - b;
    }

    public double multiply(double a, double b) {
        validateInputs(a, b);
        return a * b;
    }

    public double divide(double a, double b) {
        validateInputs(a, b);

        if (b == 0.0) {
            throw new ArithmeticException("Ділення на нуль заборонено.");
        }

        return a / b;
    }

    private void validateInputs(double a, double b) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            throw new InvalidInputException(
                    "Числа повинні бути скінченними: NaN та Infinity заборонені."
            );
        }
    }
}