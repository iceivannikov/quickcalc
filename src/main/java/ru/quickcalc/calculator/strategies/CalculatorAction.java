package ru.quickcalc.calculator.strategies;

import ru.quickcalc.calculator.model.ActionResult;

public interface CalculatorAction {
    ActionResult execute(String currentInput);
}
