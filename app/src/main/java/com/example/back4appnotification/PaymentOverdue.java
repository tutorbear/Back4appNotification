package com.example.back4appnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PaymentOverdue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        init();
        set();
    }

    private void init() {

    }

    private void set() {

    }

    public void ban(View view) {

    }
}
