package ru.quickcalc.controller;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class BasicCalcController {

    private String currentInput = "0";
    private boolean isResultDisplayed = false;

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
            if (isResultDisplayed) {
                currentInput = number;
                isResultDisplayed = false;
            } else {
                if (currentInput.equals("0")) {
                    currentInput = number;
                } else {
                    currentInput += number;
                }
            }
        } else if (action != null) {
            switch (action) {
                case "clear" -> {
                    currentInput = "0";
                    isResultDisplayed = false;
                }
                case "=" -> {
                    try {
                        DoubleEvaluator evaluator = new DoubleEvaluator();
                        double result = evaluator.evaluate(currentInput);
                        if (result == (int) result) {
                            currentInput = String.valueOf((int) result);
                        } else {
                            currentInput = String.valueOf(result);
                        }
                        isResultDisplayed = true;
                    } catch (IllegalArgumentException e) {
                        currentInput = "Error";
                    }
                }
                case "toggle" -> {
                    if (!currentInput.equals("0")) {
                        currentInput = currentInput.startsWith("-")
                                ? currentInput.substring(1)
                                : "-" + currentInput;
                    }
                    isResultDisplayed = false;
                }
                case "%" -> {
                    try {
                        DoubleEvaluator evaluator = new DoubleEvaluator();
                        int lastOperatorIndex = Math.max(
                                Math.max(currentInput.lastIndexOf('+'), currentInput.lastIndexOf('-')),
                                Math.max(currentInput.lastIndexOf('*'), currentInput.lastIndexOf('/'))
                        );
                        if (lastOperatorIndex != -1) {
                            String basePart = currentInput.substring(0, lastOperatorIndex);
                            char operator = currentInput.charAt(lastOperatorIndex);
                            String percentagePart = currentInput.substring(lastOperatorIndex + 1);
                            double base = evaluator.evaluate(basePart);
                            double percentage = Double.parseDouble(percentagePart) / 100;
                            double result = switch (operator) {
                                case '+' -> base + (base * percentage);
                                case '-' -> base - (base * percentage);
                                case '*' -> base * percentage;
                                case '/' -> {
                                    if (percentage == 0) {
                                        throw new ArithmeticException("Деление на 0");
                                    }
                                    yield base / percentage;
                                }
                                default -> throw new IllegalArgumentException("Неверный оператор: " + operator);
                            };
                            currentInput = (result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result);
                        } else if (!currentInput.isEmpty() && currentInput.endsWith("%")) {
                            String numberPart = currentInput.substring(0, currentInput.length() - 1);
                            try {
                                double value = Double.parseDouble(numberPart);
                                currentInput = String.valueOf(value / 100);
                            } catch (NumberFormatException e) {
                                currentInput = "Error";
                            }
                        } else {
                            currentInput = "Error";
                        }
                        isResultDisplayed = false;
                    } catch (Exception e) {
                        currentInput = "Error";
                        isResultDisplayed = false;
                    }
                }
                default -> {
                    if (isResultDisplayed) {
                        isResultDisplayed = false;
                    }
                    currentInput += action;
                }
            }
        }
        model.addAttribute("result", currentInput);
        return "calculator";
    }
}
