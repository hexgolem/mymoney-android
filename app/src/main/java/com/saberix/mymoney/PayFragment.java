package com.saberix.mymoney;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class PayFragment extends Fragment {

    private TextInputLayout entered_phone;
    private TextInputLayout entered_amount;
    private TextInputLayout entered_pass;
    private String phone;
    private double available_amount;

    public PayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Button pay_money = getActivity().findViewById(R.id.pay_btn);
        phone = SaveSharedPreferences.getUserName(getContext());
        available_amount = Double.parseDouble(MyMoneyTools.getDataFromFileAt
                (5, "" + phone + ".bin", getContext()));
        entered_phone = getActivity().findViewById(R.id.textInputLayoutpayphone);
        entered_amount = getActivity().findViewById(R.id.textInputLayoutamount);
        entered_pass = getActivity().findViewById(R.id.textInputLayoutpass);

        entered_phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                entered_phone.setError(null);
            }
        });

        entered_amount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                entered_amount.setError(null);
            }
        });

        entered_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                entered_pass.setError(null);
            }
        });

        pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    String receiver_phone = entered_phone.getEditText().getText().toString();
                    double receiver_balance = Double.parseDouble(MyMoneyTools.getDataFromFileAt
                            (5, "" + receiver_phone + ".bin", getContext()));
                    available_amount = available_amount - Double.parseDouble
                            (entered_amount.getEditText().getText().toString());
                    receiver_balance = receiver_balance + Double.parseDouble
                            (entered_amount.getEditText().getText().toString());
                    MyMoneyTools.setDataInFileAt(5, "" + phone + ".bin", Double.toString(available_amount), getContext());
                    MyMoneyTools.setDataInFileAt(5, "" + receiver_phone + ".bin", Double.toString(receiver_balance), getContext());
                    new AlertDialog.Builder(getContext())
                            .setTitle("Transaction Successful")
                            .setIcon(R.drawable.ic_baseline_done_24)
                            .setPositiveButton("Ok",null)
                            .show();
                }
            }
        });
    }

    private boolean isValidInput() {
        boolean valid = true;
        if (entered_phone.getEditText().getText().toString().length() == 0) {
            entered_phone.setError("This field is required");
            valid = false;
        }
        if (entered_amount.getEditText().getText().toString().length() == 0) {
            entered_amount.setError("This field is required");
            valid = false;
        }
        if (entered_pass.getEditText().getText().toString().length() == 0) {
            entered_pass.setError("This field is required");
            valid = false;
        }
        if (!MyMoneyTools.isPhoneValid(entered_phone.getEditText().getText().toString())
                && entered_phone.getEditText().getText().toString().length() != 0) {
            entered_phone.setError("Phone number not valid");
            valid = false;
        }
        if (valid && !MyMoneyTools.isPhoneRegistered(getContext(),
                entered_phone.getEditText().getText().toString())) {
            Toast.makeText(getContext(), "Phone number not registered", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid && available_amount - Double.parseDouble(entered_amount.getEditText().getText().toString()) < 0) {
            entered_amount.setError("Insufficient wallet balance");
            valid = false;
        }
        if (valid && entered_phone.getEditText().getText().toString().equals(phone)) {
            Toast.makeText(getContext(), "Can't pay to yourself", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid) {
            String checkpass = MyMoneyTools.getDataFromFileAt(4, "" + phone + ".bin", getContext());
            if (!entered_pass.getEditText().getText().toString().equals(checkpass)) {
                Toast.makeText(getContext(), "The password is incorrect", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }
}