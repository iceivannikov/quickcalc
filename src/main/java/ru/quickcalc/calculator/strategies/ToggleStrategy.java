package ru.quickcalc.calculator.strategies;

import ru.quickcalc.calculator.model.ActionResult;

public class ToggleStrategy implements CalculatorAction {

    @Override
    public ActionResult execute(String currentInput) {
        if (!currentInput.equals("0")) {
            String toggled = currentInput.startsWith("-") ? currentInput.substring(1) : "-" + currentInput;
            return new ActionResult(toggled, false);
        }
        return new ActionResult(currentInput, false);
    }
}
