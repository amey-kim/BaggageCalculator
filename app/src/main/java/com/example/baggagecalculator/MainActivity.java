
package com.example.baggagecalculator;

import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etRate;
    Spinner spTicketClass;
    Button btnCalculate, btnAbout, btnShare;
    TextView tvExcessWeight, tvExcessCharge, tvTotalPayable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Connect Java to XML
        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);
        spTicketClass = findViewById(R.id.spTicketClass);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnAbout = findViewById(R.id.btnAbout);
        btnShare = findViewById(R.id.btnShare);

        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvExcessCharge = findViewById(R.id.tvExcessCharge);
        tvTotalPayable = findViewById(R.id.tvTotalPayable);

        // Calculate button
        btnCalculate.setOnClickListener(v -> {

            String weightInput = etWeight.getText().toString().trim();
            String rateInput = etRate.getText().toString().trim();

            if (weightInput.isEmpty() || rateInput.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double baggageWeight = Double.parseDouble(weightInput);
                double rate = Double.parseDouble(rateInput);

                if (!Double.isFinite(baggageWeight) || baggageWeight < 0 ||
                        !Double.isFinite(rate) || rate < 0) {
                    Toast.makeText(this,
                            "Please enter valid non-negative values!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String ticketClass =
                        spTicketClass.getSelectedItem().toString();

                double allowance = ticketClass.equals("Economy") ? 20 : 40;

                double excessWeight =
                        Math.max(0, baggageWeight - allowance);

                double excessCharge = excessWeight * rate;
                double totalPayable = excessCharge * 1.08;

                tvExcessWeight.setText(String.format(Locale.US,
                        "Excess Weight: %.2f kg", excessWeight));

                tvExcessCharge.setText(String.format(Locale.US,
                        "Excess Charge: RM %.2f", excessCharge));

                tvTotalPayable.setText(String.format(Locale.US,
                        "Total Payable: RM %.2f", totalPayable));

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numeric values!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // About button
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Share button
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            String shareText =
                    "Check out my Excess Baggage Calculator!\n\n" +
                            "Calculate your excess baggage charges here:\n" +
                            "https://github.com/amey-kim/BaggageCalculator";

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(
                    shareIntent, "Share using"));
        });

        // Window insets
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(systemBars.left, systemBars.top,
                            systemBars.right, systemBars.bottom);

                    return insets;
                });
    }
}
