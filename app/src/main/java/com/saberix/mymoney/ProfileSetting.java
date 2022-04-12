package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileSetting extends AppCompatActivity implements PasswordDialog.PasswordDialogListener {

    TextInputLayout nametxt;
    TextInputLayout emailtxt;
    TextInputLayout phonetxt;
    private String phone;
    private String name;
    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nametxt = findViewById(R.id.textInputLayoutname);
        emailtxt = findViewById(R.id.textInputLayoutemail);
        phonetxt = findViewById(R.id.textInputLayoutphone);

        phone = SaveSharedPreferences.getUserName(this);
        name = MyMoneyTools.getDataFromFileAt(1, "" + phone + ".bin", this);
        email = MyMoneyTools.getDataFromFileAt(2, "" + phone + ".bin", this);
        pass = MyMoneyTools.getDataFromFileAt(4, "" + phone + ".bin", this);

        nametxt.getEditText().setText(name);
        emailtxt.getEditText().setText(email);
        phonetxt.getEditText().setText(phone);

        nametxt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                nametxt.setError(null);
            }
        });

        emailtxt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailtxt.setError(null);
            }
        });

        phonetxt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phonetxt.setError(null);
            }
        });

        Button btn = findViewById(R.id.setting_cnfrm_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid())
                    openDialog();
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

    public void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(), "password dialog");
    }

    @Override
    public void checkPass(boolean flag) {
        if (flag) {
            if (!nametxt.getEditText().getText().toString().equals(name)) {
                MyMoneyTools.setDataInFileAt(1, "" + phone + ".bin", nametxt
                        .getEditText().getText().toString(), getApplicationContext());
            }
        }
    }

    private boolean isInputValid() {

        String entered_name = nametxt.getEditText().getText().toString();
        String entered_email = emailtxt.getEditText().getText().toString();
        String entered_phone = phonetxt.getEditText().getText().toString();

        boolean valid = true;
        if (entered_name.length() == 0) {
            nametxt.setError("This field is required");
            valid = false;
        }
        if (entered_email.length() == 0) {
            emailtxt.setError("This field is required");
            valid = false;
        }
        if (entered_phone.length() == 0) {
            phonetxt.setError("This field is required");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(entered_email).matches() && entered_email.length() != 0) {
            emailtxt.setError("Email not valid");
            valid = false;
        }
        if (!MyMoneyTools.isPhoneValid(entered_phone) && entered_phone.length() != 0) {
            phonetxt.setError("Phone number not valid");
            valid = false;
        }
        if (valid) {
            boolean EOF = false;

            try (FileInputStream fin = openFileInput("pnos.bin");
                 DataInputStream din = new DataInputStream(fin);) {
                while (!EOF) {
                    try {
                        if (entered_phone.equals(din.readUTF()) && !entered_phone.equals(phone)) {
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

}