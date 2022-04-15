package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class ProfileSetting extends AppCompatActivity implements PasswordDialog.PasswordDialogListener {

    TextInputLayout nametxt;
    TextInputLayout emailtxt;
    TextInputLayout phonetxt;
    private String saved_file_data[];
    private String new_file_data[];

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
        saved_file_data = new String[5];
        new_file_data = new String[5];

        if (!MyMoneyTools.getDataFromFile("" + SaveSharedPreferences
                .getUserName(this) + ".bin", this, saved_file_data))
            finish();

        nametxt.getEditText().setText(saved_file_data[0]);
        emailtxt.getEditText().setText(saved_file_data[1]);
        phonetxt.getEditText().setText(saved_file_data[2]);

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
            File dir = getFilesDir();
            File file = new File(dir, "" + saved_file_data[2] + ".bin");
            if (file.delete()) {
                if (!MyMoneyTools.setDataInFile("" + new_file_data[2] + ".bin", this, new_file_data)) {
                    finish();
                    // attempt to recover the deleted file in case of an error occurs
                    MyMoneyTools.setDataInFile("" + saved_file_data[2] + ".bin", this, saved_file_data);
                }
                // if old and new phone numbers are not equal
                if (!new_file_data[2].equals(saved_file_data[2])) {
                    if (!MyMoneyTools.deregister_phone(this, saved_file_data[2])) {
                        finish();
                        // attempt to recover the deleted file in case of an error occurs
                        MyMoneyTools.setDataInFile("" + saved_file_data[2] + ".bin", this, saved_file_data);
                    }
                    if (!MyMoneyTools.register_phone(this, new_file_data[2]))
                        finish();
                }
                // this block logs out the user after data has been changed
                SaveSharedPreferences.clearUserName(this);
                finishAffinity();
                Toast.makeText(this, "Changes saved, Login again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, loginactivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean isInputValid() {

        new_file_data[0] = nametxt.getEditText().getText().toString();
        new_file_data[1] = emailtxt.getEditText().getText().toString();
        new_file_data[2] = phonetxt.getEditText().getText().toString();
        new_file_data[3] = saved_file_data[3];
        new_file_data[4] = saved_file_data[4];

        boolean valid = true;
        if (new_file_data[0].length() == 0) {
            nametxt.setError("This field is required");
            valid = false;
        }
        if (new_file_data[1].length() == 0) {
            emailtxt.setError("This field is required");
            valid = false;
        }
        if (new_file_data[2].length() == 0) {
            phonetxt.setError("This field is required");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(new_file_data[1]).matches() && new_file_data[1].length() != 0) {
            emailtxt.setError("Email not valid");
            valid = false;
        }
        if (!MyMoneyTools.isPhoneValid(new_file_data[2]) && new_file_data[2].length() != 0) {
            phonetxt.setError("Phone number not valid");
            valid = false;
        }
        if (valid) {
            if(!(valid = !MyMoneyTools.isPhoneRegistered(this, new_file_data[2]))){
                Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
            }
        }
        return valid;
    }

}