package ru.example.creditraschet;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private final NumberFormat rubles = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
    private EditText amountInput;
    private EditText rateInput;
    private EditText monthsInput;
    private TextView messageView;
    private TextView monthlyPaymentView;
    private TextView totalPaymentView;
    private TextView overpaymentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Калькулятор кредита");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Калькулятор кредита (аннуитетный платёж)");
        title.setTextSize(22);
        root.addView(title);

        amountInput = addInput(root, "Сумма кредита, ₽ (1 000–10 000 000)", InputType.TYPE_CLASS_NUMBER);
        rateInput = addInput(root, "Годовая ставка, % (0,1–30)", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        monthsInput = addInput(root, "Срок, месяцев (1–360)", InputType.TYPE_CLASS_NUMBER);

        Button calculateButton = new Button(this);
        calculateButton.setText("Рассчитать");
        calculateButton.setOnClickListener(view -> calculate());
        root.addView(calculateButton);

        messageView = addResult(root, "");
        monthlyPaymentView = addResult(root, "Ежемесячный платёж: —");
        totalPaymentView = addResult(root, "Общая сумма выплат: —");
        overpaymentView = addResult(root, "Переплата: —");

        setContentView(root);
    }

    private EditText addInput(LinearLayout root, String hint, int inputType) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setInputType(inputType);
        root.addView(editText);
        return editText;
    }

    private TextView addResult(LinearLayout root, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18);
        textView.setPadding(0, 16, 0, 0);
        root.addView(textView);
        return textView;
    }

    private void calculate() {
        try {
            long amount = Long.parseLong(amountInput.getText().toString().trim());
            double annualRate = Double.parseDouble(rateInput.getText().toString().trim().replace(',', '.'));
            int months = Integer.parseInt(monthsInput.getText().toString().trim());

            LoanCalculationResult result = LoanCalculator.calculate(amount, annualRate, months);
            messageView.setText("");
            monthlyPaymentView.setText("Ежемесячный платёж: " + rubles.format(result.monthlyPayment()));
            totalPaymentView.setText("Общая сумма выплат: " + rubles.format(result.totalPayment()));
            overpaymentView.setText("Переплата: " + rubles.format(result.overpayment()));
        } catch (NumberFormatException exception) {
            showError("Заполните все поля корректными числами");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void showError(String message) {
        messageView.setText("Ошибка: " + message);
        monthlyPaymentView.setText("Ежемесячный платёж: —");
        totalPaymentView.setText("Общая сумма выплат: —");
        overpaymentView.setText("Переплата: —");
    }
}
