package com.saberix.mymoney;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class signupactivity extends AppCompatActivity {

    private Button signup;

    private EditText etemail;
    private EditText etpass;
    private EditText etphone;
    private EditText etname;
    private EditText etrepass;

    private String entered_data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etemail = findViewById(R.id.emailinp);
        etpass = findViewById(R.id.passinp);
        etphone = findViewById(R.id.phoneinp);
        etname = findViewById(R.id.nameinp);
        etrepass = findViewById(R.id.repassinp);
        signup = findViewById(R.id.confirmsignup);
        entered_data = new String[5];

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("✍️Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                if (isInputValid()) {
                    if (register()) {
                        Toast.makeText(getApplicationContext(), "Account created, Proceed to login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), loginactivity.class);
                        startActivity(intent);
                        finish();
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

    private void setData() {
        entered_data[0] = etname.getText().toString();
        entered_data[1] = etemail.getText().toString();
        entered_data[2] = etphone.getText().toString();
        entered_data[3] = etpass.getText().toString();
        entered_data[4] = "0.00";
    }

    private boolean isInputValid() {

        boolean valid = true;
        if (entered_data[0].length() == 0) {
            etname.setError("This field is required");
            valid = false;
        }
        if (entered_data[1].length() == 0) {
            etemail.setError("This field is required");
            valid = false;
        }
        if (entered_data[2].length() == 0) {
            etphone.setError("This field is required");
            valid = false;
        }
        if (entered_data[3].length() == 0) {
            etpass.setError("This field is required");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(entered_data[1]).matches() && entered_data[1].length() != 0) {
            etemail.setError("Email not valid");
            valid = false;
        }
        if (!MyMoneyTools.isPhoneValid(entered_data[2]) && entered_data[2].length() != 0) {
            etphone.setError("Phone number not valid");
            valid = false;
        }
        if (entered_data[3].length() < 8 && entered_data[3].length() != 0) {
            etpass.setError("Password must of minimum 8 characters");
            valid = false;
        }
        if (!etrepass.getText().toString().equals(entered_data[3])) {
            etrepass.setError("Password doesn't match");
            valid = false;
        }
        if (valid) {
            if(!(valid = !MyMoneyTools.isPhoneRegistered(this, entered_data[2]))){
                Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
            }
        }
        return valid;
    }

    private boolean register() {
        return MyMoneyTools.register_phone(this, entered_data[2]) &
                MyMoneyTools.setDataInFile("" + entered_data[2] + ".bin", this, entered_data);
    }
}