package ui;

import core.Calc;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;

public class AppController {

    private Calc calc;

    public AppController() {
        calc = new Calc(0.0, 0.0, 0.0);
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
        updateOperandsView();
    }

    @FXML
    private ListView<Double> operandsView;

    @FXML
    private Label operandView;

    @FXML
    void initialize() {
        updateOperandsView();
    }

    private void updateOperandsView() {
        List<Double> operands = operandsView.getItems();
        operands.clear();
        int elementCount = Math.min(calc.getOperandCount(), 3);
        for (int i = 0; i < elementCount; i++) {
            operands.add(calc.peekOperand(elementCount - i - 1));
        }
    }

    private String getOperandString() {
        return operandView.getText();
    }

    private boolean hasOperand() {
        return ! getOperandString().isBlank();
    }

    private double getOperand() {
        return Double.valueOf(operandView.getText());
    }
    
    private void setOperand(String operandString) {
        operandView.setText(operandString);
    }

    @FXML
    void handleEnter() {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
        } else {
            calc.dup();
        }
        setOperand("");
        updateOperandsView();
    }

    private void appendToOperand(String s) {
        setOperand(getOperandString() + s);
    }

    @FXML
    void handleDigit(ActionEvent ae) {
        if (ae.getSource() instanceof Labeled l) {
            appendToOperand(l.getText());
        }
    }

    @FXML
    void handlePoint() {
        var operandString = getOperandString();
        if (operandString.contains(".")) {
            int index = operandString.lastIndexOf(".");
            String leftOfPoint = operandString.substring(0, index + 1);
            setOperand(leftOfPoint);
        } else {
            appendToOperand(".");
        }
    }

    @FXML
    void handleClear() {
        setOperand("");
    }

    @FXML
    void handleSwap() {
        calc.swap();
        updateOperandsView();
    }

    private void performOperation(UnaryOperator<Double> op) {
        calc.performOperation(op);
    }

    private void performOperation(boolean swap, BinaryOperator<Double> op) {
        if (hasOperand()) {
            calc.pushOperand(getOperand());
            handleClear();
            calc.performOperation(op);
            updateOperandsView();
        }
        if (swap) {
            // perform operation, but swap first if needed
            calc.swap();
            calc.performOperation(op);
            updateOperandsView();
        }
    }

    @FXML
    void handleOpAdd() {
        // addition
        BinaryOperator<Double> add = (x, y) -> x + y;
        performOperation(false, add);
    }

    @FXML
    void handleOpSub() {
        BinaryOperator<Double> sub = (x, y) -> x - y;
        performOperation(false, sub);
    }

    @FXML
    void handleOpMult() {
        BinaryOperator<Double> mult = (x, y) -> x * y;
        performOperation(false, mult);
    }

    @FXML
    void handleOpDiv() {
        BinaryOperator<Double> div = (x, y) -> x / y;
        performOperation(false, div);
    }

    @FXML
    void handleOpSqrt() {
        UnaryOperator<Double> sqrt = Math::sqrt;
        performOperation(sqrt);
        updateOperandsView();
    }

    @FXML
    void handleOpPi() {
        calc.pushOperand(Math.PI);
        updateOperandsView();
    }
}
