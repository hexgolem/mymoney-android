package com.saberix.mymoney;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isUserLoggedIn()){
                    Intent intent=new Intent(SplashScreenActivity.this,HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 600);
    }

    private boolean isUserLoggedIn(){
        return (SaveSharedPreferences.getUserName(getApplicationContext()).length()!=0);
    }
}
