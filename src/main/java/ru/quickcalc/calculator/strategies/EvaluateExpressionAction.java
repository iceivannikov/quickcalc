package ru.quickcalc.calculator.strategies;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import ru.quickcalc.calculator.model.ActionResult;

public class EvaluateExpressionAction implements CalculatorAction {
    @Override
    public ActionResult execute(String currentInput) {
        try {
            DoubleEvaluator evaluator = new DoubleEvaluator();
            double result = evaluator.evaluate(currentInput);
            String output = (result == (int) result) ? String.valueOf((int) result) : String.valueOf(result);
            return new ActionResult(output, true);
        } catch (
                IllegalArgumentException e) {
            return new ActionResult("Error", false);
        }
    }
}
