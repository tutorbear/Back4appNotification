package com.example.back4appnotification.verificaiton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import com.example.back4appnotification.R;
import com.parse.ParseObject;

public class VerTView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_t_view);
    }

    public void verifyT(View view) {
        ParseObject obj= getIntent().getParcelableExtra("obj");
        obj.put("verified",true);
        obj.saveEventually();
        finish();
    }
}
