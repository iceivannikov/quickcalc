package ru.quickcalc.calculator.strategies;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import ru.quickcalc.calculator.model.ActionResult;

public class PercentageStrategy implements CalculatorAction {

    private static final int NO_OPERATOR = -1;

    @Override
    public ActionResult execute(String currentInput) {
        try {
            DoubleEvaluator evaluator = new DoubleEvaluator();
            int lastOperatorIndex = getLastOperatorIndex(currentInput);
            if (lastOperatorIndex != -1) {
                String basePart = getBasePart(currentInput, lastOperatorIndex);
                char operator = currentInput.charAt(lastOperatorIndex);
                String percentagePart = currentInput.substring(lastOperatorIndex + 1);
                if (isDivisionByZero(operator, percentagePart)) {
                    throw new ArithmeticException("Division by zero");
                }
                double base = evaluator.evaluate(basePart);
                String output = calculatePercentageResult(base, operator, percentagePart);
                return new ActionResult(output, false);
            } else if (currentInput.endsWith("%")) {
                return handleSimplePercentage(currentInput);
            } else {
                return new ActionResult("Error", false);
            }
        } catch (Exception e) {
            return new ActionResult("Error", false);
        }
    }

    private int getLastOperatorIndex(String input) {
        return getLastOperatorIndex(input, new char[]{'+', '-', '*', '/'});
    }

    private int getLastOperatorIndex(String input, char[] operators) {
        int lastIndex = NO_OPERATOR;
        for (char operator : operators) {
            lastIndex = Math.max(lastIndex, input.lastIndexOf(operator));
        }
        return lastIndex;
    }

    private String getBasePart(String input, int lastOperatorIndex) {
        if (lastOperatorIndex < 0 || lastOperatorIndex >= input.length()) {
            throw new IllegalArgumentException("Invalid lastOperatorIndex: " + lastOperatorIndex);
        }
        return input.substring(0, lastOperatorIndex);
    }

    private boolean isDivisionByZero(char operator, String percentagePart) {
        if (operator == '/') {
            try {
                double percentage = parseNumber(percentagePart) / 100;
                return percentage == 0;
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    private String calculatePercentageResult(double base, char operator, String percentagePart) {
        try {
            double percentage = parseNumber(percentagePart) / 100;
            double result = switch (operator) {
                case '+' -> base + (base * percentage);
                case '-' -> base - (base * percentage);
                case '*' -> base * percentage;
                case '/' -> base / percentage;
                default -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
            return (result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result);
        } catch (NumberFormatException e) {
            return "Error";
        }
    }

    private ActionResult handleSimplePercentage(String currentInput) {
        String numberPart = currentInput.substring(0, currentInput.length() - 1);
        try {
            double value = parseNumber(numberPart);
            return new ActionResult(String.valueOf(value / 100), false);
        } catch (NumberFormatException e) {
            return new ActionResult("Error", false);
        }
    }

    private double parseNumber(String numberPart) {
        return Double.parseDouble(numberPart);
    }
}
