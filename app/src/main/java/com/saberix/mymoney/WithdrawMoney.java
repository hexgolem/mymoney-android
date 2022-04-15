package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WithdrawMoney extends AppCompatActivity {

    private String phone;
    private double amount;
    private String pass;
    private EditText entered_amount;
    private EditText person;
    private EditText acc_num;
    private EditText confirm_acc_num;
    private EditText IFSC;
    private EditText entered_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("📝 Enter Bank Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        phone = SaveSharedPreferences.getUserName(this);
        amount = Double.parseDouble(MyMoneyTools.getDataFromFileAt
                (5, "" + phone + ".bin", this));
        pass = MyMoneyTools.getDataFromFileAt(4, "" + phone + ".bin", this);

        Button proceed = findViewById(R.id.proceed_to_withdraw);
        entered_amount = findViewById(R.id.amount_withdraw);
        person = findViewById(R.id.recipient_name);
        acc_num = findViewById(R.id.acc_num);
        confirm_acc_num = findViewById(R.id.confirm_acc_num);
        IFSC = findViewById(R.id.ifsc);
        entered_pass = findViewById(R.id.withdraw_pass);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()){
                   amount=amount-Double.parseDouble(entered_amount.getText().toString());
                   MyMoneyTools.setDataInFileAt(5,""+phone+".bin",Double.toString(amount),getApplicationContext());
                    new AlertDialog.Builder(WithdrawMoney.this)
                            .setTitle("Money Withdrawal Successful")
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

    private boolean isValidInput() {
        boolean valid = true;
        if (person.getText().toString().length() == 0) {
            person.setError("This field is required");
            valid = false;
        }
        if (acc_num.getText().toString().length() == 0) {
            acc_num.setError("This field is required");
            valid = false;
        }
        if (confirm_acc_num.getText().toString().length() == 0) {
            confirm_acc_num.setError("This field is required");
            valid = false;
        }
        if (IFSC.getText().toString().length() == 0) {
            IFSC.setError("This field is required");
            valid = false;
        }
        if (entered_amount.getText().toString().length() == 0) {
            entered_amount.setError("This field is required");
            valid = false;
        }
        if (entered_pass.getText().toString().length() == 0) {
            entered_pass.setError("This field is required");
            valid = false;
        }
        if (acc_num.getText().toString().length() < 9
                && acc_num.getText().toString().length() != 0) {
            acc_num.setError("Account number not valid");
            valid = false;
        }
        if (!confirm_acc_num.getText().toString().equals(acc_num.getText().toString())
                && confirm_acc_num.getText().toString().length() != 0) {
            confirm_acc_num.setError("Account number does not match");
            valid = false;
        }
        if (valid && amount - Double.parseDouble(entered_amount.getText().toString()) < 0) {
            entered_amount.setError("Insufficient wallet balance");
            valid = false;
        }
        if (valid) {
            String checkpass = MyMoneyTools.getDataFromFileAt(4, "" + phone + ".bin", this);
            if (!entered_pass.getText().toString().equals(checkpass)) {
                Toast.makeText(this, "The password is incorrect", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }
}