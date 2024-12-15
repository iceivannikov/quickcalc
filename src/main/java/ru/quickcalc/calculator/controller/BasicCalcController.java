package ru.quickcalc.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.quickcalc.calculator.model.ActionResult;
import ru.quickcalc.calculator.strategies.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class BasicCalcController {

    private String currentInput = "0";
    private boolean isResultDisplayed = false;
    private final Map<String, CalculatorAction> calculatorActions = new HashMap<>();

    public BasicCalcController() {
        calculatorActions.put("clear", new ClearStrategy());
        calculatorActions.put("toggle", new ToggleStrategy());
        calculatorActions.put("=", new EvaluateExpressionAction());
        calculatorActions.put("%", new PercentageStrategy());
    }

    @GetMapping("/")
    public String showCalculatorPage(Model model) {
        model.addAttribute("result", currentInput);
        return "calculator";
    }

    @PostMapping("/calculate")
    public String handleInput(@RequestParam(required = false) String number,
                              @RequestParam(required = false) String action,
                              Model model) {

        if (number != null) {
            handleNumberInput(number);
        } else if (action != null) {
            handleActionInput(action);
        }

        model.addAttribute("result", currentInput);
        return "calculator";
    }

    private void handleNumberInput(String number) {
        if (isResultDisplayed) {
            currentInput = number;
            isResultDisplayed = false;
        } else {
            currentInput = currentInput.equals("0") ? number : currentInput + number;
        }
    }

    private void handleActionInput(String action) {
        CalculatorAction actionHandler = calculatorActions.get(action);
        if (actionHandler != null) {
            ActionResult result = actionHandler.execute(currentInput);
            currentInput = result.input();
            isResultDisplayed = result.resultDisplayed();
        } else {
            currentInput += action;
        }
    }
}
