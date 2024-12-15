package ru.quickcalc.calculator.strategies;

import ru.quickcalc.calculator.model.ActionResult;

public class ClearStrategy implements CalculatorAction {
    @Override
    public ActionResult execute(String currentInput) {
        return new ActionResult("0", false);
    }
}
