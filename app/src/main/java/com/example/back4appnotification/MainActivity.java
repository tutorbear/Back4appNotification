package com.example.back4appnotification;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ParseUser.getCurrentUser()!=null){
            startActivity(new Intent(this,HomePage.class));
        }
    }


    public void login(View view) {
        ParseUser.logInInBackground("taz", "1", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                    Toast.makeText(MainActivity.this, "DOne", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

