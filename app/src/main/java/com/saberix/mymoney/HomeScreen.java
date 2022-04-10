package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;

public class HomeScreen extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private NavigationBarView navigationBarView;
    WalletFragment walletFragment=new WalletFragment();
    PayFragment payFragment=new PayFragment();
    HistoryFragment historyFragment=new HistoryFragment();

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        txt=findViewById(R.id.welcome_message);
        String nameOfUser=MyMoneyTools.getDataFromFileAt(1,
                ""+SaveSharedPreferences.getUserName(this)+".bin",
                this);
        txt.setText("Welcome 😀\n"+nameOfUser);

        navigationBarView=findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.getMenu().getItem(0).setCheckable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.walletoption){
            navigationBarView.getMenu().getItem(0).setCheckable(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,walletFragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.payoption){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,payFragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.historyoption){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,historyFragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutoption){
            SaveSharedPreferences.clearUserName(getApplicationContext());
            finish();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(HomeScreen.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {finish();}
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}