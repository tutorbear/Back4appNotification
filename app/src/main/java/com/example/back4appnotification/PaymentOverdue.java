package com.example.back4appnotification;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PaymentOverdue extends AppCompatActivity {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t2N,t3N,t2Time,t3Time;
    LinearLayout l2,l3;
    Button ban,t2hire,t3hire;
    HashMap<String,String> map;
    ArrayList<String> time = new ArrayList<>(Arrays.asList("","",""));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        init();
        set();
    }

    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //Text views
        id = findViewById(R.id.jobIdPO);
        name = findViewById(R.id.namePO);
        salary = findViewById(R.id.salaryPO);
        location = findViewById(R.id.locationPO);
        stdNumber = findViewById(R.id.studentNumberPO);
        sClass = findViewById(R.id.classPO);
        sub = findViewById(R.id.subjectPO);
        curr = findViewById(R.id.curriculumPO);
        address = findViewById(R.id.addressPO);

        //Teacher
        t1N = findViewById(R.id.t1N);

        t2N = findViewById(R.id.t2N);
        t3N = findViewById(R.id.t3N);
        t2Time = findViewById(R.id.t2Time);
        t3Time = findViewById(R.id.t3Time);

        //linear layouts
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Buttons
        ban = findViewById(R.id.ban);
        t2hire = findViewById(R.id.t2Hire);
        t3hire = findViewById(R.id.t3Hire);

        //Map
        map = new HashMap<>();
    }

    @SuppressLint("SetTextI18n")
    private void set() {
        id.setText("ID: "+ obj.getObjectId()); //createdBy.sProfile.guardianName
        name.setText(""+obj.getParseObject("createdBy").getParseObject("sProfile").getString("guardianName"));
        salary.setText("Salary: "+ obj.get("salary").toString());
        location.setText("Location: "+ obj.getString("location"));
        stdNumber.setText("Number: "+ obj.get("numberOfStudents").toString());
        sClass.setText("Class: "+ obj.getString("class1")+","+ obj.getString("class2"));
        sub.setText("Subject1: "+ obj.getString("subject1")+"\nSubject2: "+ obj.getString("subject2"));
        address.setText("Address: "+ obj.getString("address"));


        t1N.setText(obj.getParseObject("hired").get("username")+"");
        if(obj.getParseObject("hired").getBoolean("banLock")){
            ban.setEnabled(false);
        }

        // Setting visibility to false
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        t2Time.setVisibility(View.GONE);
        t3Time.setVisibility(View.GONE);
        //-------------------------------------

        List<ParseObject> requested = obj.getList("requested");
        if(requested!=null && !requested.isEmpty()){
            for (int i = 0; i < requested.size(); i++) {
                if(i==0){
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(0).getString("username"));
                    map.put("l2",requested.get(i).getObjectId());
                }else if(i==1){
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(1).getString("username"));
                    map.put("l3",requested.get(i).getObjectId());
                }
            }
        }

    }

    public void ban(View view) {
        HashMap<String, String> params = new HashMap();
        params.put("profileId",obj.getParseObject("hired").getObjectId());
        params.put("jobId",obj.getObjectId());
        ParseCloud.callFunctionInBackground("banTeacher", params, new FunctionCallback<Float>() {
            @Override
            public void done(Float aFloat, ParseException e) {
                if (e == null) {
                    Toast.makeText(PaymentOverdue.this, "Teacher was banned", Toast.LENGTH_SHORT).show();
                    ban.setEnabled(false);
                }else{
                    Toast.makeText(PaymentOverdue.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
    }

    public void hire(View view) {
        //getting requested array index
        int i  = 0;
        if (view.getId()!=R.id.t2Hire){
            i =1;
        }

        ParseObject temp = (ParseObject) obj.getList("requested").get(i);


        // hiring
        obj.put("hired",temp);

        //Payment date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        Date paymentDate = cal.getTime();

        obj.put("paymentDate",paymentDate);
        obj.saveEventually();

        //removing from Requested
        ArrayList<ParseObject> removeObject = new ArrayList<>();
        removeObject.add(temp);
        obj.removeAll("requested",removeObject);
        obj.saveEventually();
    //Todo : finish
    }
}
