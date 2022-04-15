package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddMoney extends AppCompatActivity {

    private String phone;
    private String amount;
    private EditText entered_amount;
    private EditText person;
    private EditText card_num;
    private EditText expiry;
    private EditText cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("📝 Enter Card Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        phone = SaveSharedPreferences.getUserName(this);
        amount = MyMoneyTools.getDataFromFileAt(5, "" + phone + ".bin", this);
        Button proceed = findViewById(R.id.proceed_to_add);
        entered_amount = findViewById(R.id.amount_inp);
        person = findViewById(R.id.person_name);
        card_num = findViewById(R.id.crd_num);
        expiry = findViewById(R.id.expiry);
        cvv = findViewById(R.id.cvv);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    double total = Double.parseDouble(amount) +
                            Double.parseDouble(entered_amount.getText().toString());
                    if (total > 100000.00) {
                        Toast.makeText(getApplicationContext(), "Total balance exceed max limit", Toast.LENGTH_SHORT).show();
                    } else {
                        MyMoneyTools.setDataInFileAt(5, "" + phone + ".bin", Double.toString(total), getApplicationContext());
                        new AlertDialog.Builder(AddMoney.this)
                                .setTitle("Money Added Successfully")
                                .setIcon(R.drawable.ic_baseline_done_24)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isInputValid() {
        boolean valid = true;
        if (person.getText().toString().length() == 0) {
            person.setError("This field is required");
            valid = false;
        }
        if (card_num.getText().toString().length() == 0) {
            card_num.setError("This field is required");
            valid = false;
        }
        if (expiry.getText().toString().length() == 0) {
            expiry.setError("This field is required");
            valid = false;
        }
        if (cvv.getText().toString().length() == 0) {
            cvv.setError("This field is required");
            valid = false;
        }
        if (entered_amount.getText().toString().length() == 0) {
            entered_amount.setError("This field is required");
            valid = false;
        }
        if (!expiry.getText().toString().matches("\\d{2}/\\d{2}/\\d{4}")
                && expiry.getText().toString().length() != 0) {
            expiry.setError("Wrong format");
            valid = false;
        }
        if (cvv.getText().toString().length() > 3 && cvv.getText().toString().length() != 0) {
            cvv.setError("Wrong input");
            valid = false;
        }
        return valid;
    }
}