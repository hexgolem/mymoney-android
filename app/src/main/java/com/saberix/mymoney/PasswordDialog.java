package com.saberix.mymoney;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PasswordDialog extends AppCompatDialogFragment {

    private EditText confirmpass;
    private PasswordDialogListener listener;
    private String phone;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        phone=SaveSharedPreferences.getUserName(getActivity().getApplicationContext());

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.password_dialog,null);

        builder.setView(view)
                .setTitle("Confirm Password")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password=confirmpass.getText().toString();
                        String savedpass=MyMoneyTools.getDataFromFileAt(4,""+phone+".bin",getActivity().getApplicationContext());
                        if(savedpass.equals(password)){
                            listener.checkPass(true);
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(),"Wrong Pass",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        confirmpass=view.findViewById(R.id.edit_password);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener=(PasswordDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement");
        }
    }

    public interface PasswordDialogListener{
        void checkPass(boolean flag);
    }
}
