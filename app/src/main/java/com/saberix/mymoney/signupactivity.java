package com.saberix.mymoney;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class signupactivity extends AppCompatActivity {

    Button signup;

    EditText etemail;
    EditText etpass;
    EditText etphone;
    EditText etname;
    EditText etrepass;

    String name, email, phone, pass, repass;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("✍️Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(252, 159, 81)));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                if (isInputValid()) {
                    boolean success = register();
                    if (success)
                    {finish();
                        Toast.makeText(getApplicationContext(), "Account created, Proceed to login", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                        startActivity(intent);
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
        email = etemail.getText().toString();
        phone = etphone.getText().toString();
        name = etname.getText().toString();
        pass = etpass.getText().toString();
        repass = etrepass.getText().toString();
    }

    private boolean isInputValid() {

        boolean valid = true;
        if (name.length() == 0) {
            etname.setError("This field is required");
            valid = false;
        }
        if (email.length() == 0) {
            etemail.setError("This field is required");
            valid = false;
        }
        if (phone.length() == 0) {
            etphone.setError("This field is required");
            valid = false;
        }
        if (pass.length() == 0) {
            etpass.setError("This field is required");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length() != 0) {
            etemail.setError("Email not valid");
            valid = false;
        }
        if (!IsPhoneValid.check(phone) && phone.length() != 0) {
            etphone.setError("Phone number not valid");
            valid = false;
        }
        if (pass.length() < 8 && pass.length() != 0) {
            etpass.setError("Password must of minimum 8 characters");
            valid = false;
        }
        if (!repass.equals(pass)) {
            etrepass.setError("Password doesn't match");
            valid = false;
        }
        if (valid) {
            boolean EOF = false;

            try (FileInputStream fin = openFileInput("pnos.bin");
                 DataInputStream din = new DataInputStream(fin);) {
                while (!EOF) {
                    try {
                        if (phone.equals(din.readUTF())) {
                            Toast.makeText(getApplicationContext(), "Phone number already registered", Toast.LENGTH_SHORT).show();
                            valid = false;
                            EOF = true;
                        }
                    } catch (EOFException e) {
                        EOF = true;
                    }
                }
            } catch (FileNotFoundException e) {
                ;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }

    private boolean register() {
        try (FileOutputStream fout = openFileOutput("" + phone + ".bin", Context.MODE_APPEND);
             DataOutputStream dout = new DataOutputStream(fout);
             FileOutputStream fout1 = openFileOutput("pnos.bin", Context.MODE_APPEND);
             DataOutputStream dout1 = new DataOutputStream(fout1);) {

            dout1.writeUTF(phone);
            dout.writeUTF(name);
            dout.writeUTF(email);
            dout.writeUTF(phone);
            dout.writeUTF(pass);
            dout.writeUTF(Double.toString(0.00));
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}