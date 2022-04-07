package com.saberix.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button signup;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(isUserLoggedIn()){
            Intent intent=new Intent(this,HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup=findViewById(R.id.signupbtn);
        login=findViewById(R.id.loginbtn);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),signupactivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),loginactivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isUserLoggedIn(){
        return (SaveSharedPreferences.getUserName(getApplicationContext()).length()!=0);
    }
}