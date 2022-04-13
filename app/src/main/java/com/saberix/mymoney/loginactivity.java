package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class loginactivity extends AppCompatActivity {

    Button login;
    EditText etphone;
    EditText etpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        etphone = findViewById(R.id.loginPhone);
        etpass = findViewById(R.id.loginPass);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("🎉Welcome Back");
        actionBar.setDisplayHomeAsUpEnabled(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    SaveSharedPreferences.setUserName(getApplicationContext(), etphone.getText().toString());
                    finishAffinity();
                    Intent intent = new Intent(loginactivity.this, HomeScreen.class);
                    startActivity(intent);
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
        String phone = etphone.getText().toString();
        String pass = etpass.getText().toString();

        boolean valid = true;
        if (!MyMoneyTools.isPhoneValid(phone)) {
            etphone.setError("Phone number not valid");
            valid = false;
        }
        if (pass.length() < 8) {
            etpass.setError("Password must be of minimum 8 characters");
            valid = false;
        }

        if (valid) {
            boolean EOF = false;

            try (FileInputStream fin = openFileInput("pnos.bin");
                 DataInputStream din = new DataInputStream(fin);) {
                while (!EOF) {
                    try {
                        if (phone.equals(din.readUTF())) {
                            EOF = true;
                        }
                    } catch (EOFException e) {
                        Toast.makeText(getApplicationContext(), "Phone number not registered", Toast.LENGTH_SHORT).show();
                        valid = false;
                        EOF = true;
                    }
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Phone number not registered", Toast.LENGTH_SHORT).show();
                valid = false;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        if (valid) {
            String checkpass = MyMoneyTools.getDataFromFileAt(4, "" + phone + ".bin", this);
            if (checkpass != null) {
                if (!pass.equals(checkpass)) {
                    Toast.makeText(getApplicationContext(), "The password is incorrect", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            } else {
                valid = false;
            }
        }
        return valid;
    }

}
