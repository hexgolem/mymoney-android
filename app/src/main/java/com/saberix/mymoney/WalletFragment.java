package com.saberix.mymoney;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WalletFragment extends Fragment {


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Button add_money=getActivity().findViewById(R.id.button_addmoney);
        Button withdraw=getActivity().findViewById(R.id.button_withdraw);
        String phone=SaveSharedPreferences.getUserName(getContext());
        String available_amount=MyMoneyTools.getDataFromFileAt(5,""+phone+".bin",getContext());
        TextView txt=getActivity().findViewById(R.id.txt);

        txt.setText("₹ "+available_amount);

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AddMoney.class);
                startActivity(intent);
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),WithdrawMoney.class);
                startActivity(intent);
            }
        });
    }
}