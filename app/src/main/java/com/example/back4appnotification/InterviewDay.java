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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterviewDay extends AppCompatActivity {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t2N,t3N,t1Time,t2Time,t3Time;
    HashMap<String,String> map;
    LinearLayout l1,l2,l3;
    Button h1,h2,h3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_day);
        init();
        set();
    }



    private void init() {
        obj= getIntent().getParcelableExtra("object");

        //Text views
        id = findViewById(R.id.jobIdInter);
        name = findViewById(R.id.nameInter);
        salary = findViewById(R.id.salaryInter);
        location = findViewById(R.id.locationInter);
        stdNumber = findViewById(R.id.studentNumberInter);
        sClass = findViewById(R.id.classInter);
        sub = findViewById(R.id.subjectInter);
        curr = findViewById(R.id.curriculumInter);
        address = findViewById(R.id.addressInter);
        //Teacher
        t1N = findViewById(R.id.t1NInter);
        t2N = findViewById(R.id.t2NInter);
        t3N = findViewById(R.id.t3NInter);
        t1Time= findViewById(R.id.t1TimeInter);
        t2Time = findViewById(R.id.t2TimeInter);
        t3Time = findViewById(R.id.t3TimeInter);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Map
        map = new HashMap<>();

        //Button
        h1 = findViewById(R.id.t1Hire);
        h2 = findViewById(R.id.t2Hire);
        h3 = findViewById(R.id.t3Hire);
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

        // Setting visibility to false
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        t1Time.setVisibility(View.GONE);
        t2Time.setVisibility(View.GONE);
        t3Time.setVisibility(View.GONE);
        //-------------------------------------

        List<ParseObject> requested = obj.getList("requested");
        if(requested!=null && !requested.isEmpty()){
            for (int i = 0; i < requested.size(); i++) {
                if(i==0){
                    l1.setVisibility(View.VISIBLE);
                    t1N.setText(requested.get(0).getString("username"));
                    map.put("l1",requested.get(i).getObjectId());
                }else if(i==1){
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(1).getString("username"));
                    map.put("l2",requested.get(i).getObjectId());
                }else{
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(2).getString("username"));
                    map.put("l3",requested.get(i).getObjectId());
                }
            }

            List<String> time = obj.getList("interviewTime");
            if(time!=null && !time.isEmpty()){
                for (int i = 0; i < time.size(); i++) {
                    if(i==0){
                        t1Time.setText(time.get(i));
                        time.set(i,time.get(i));
                    }else if(i==1){
                        t2Time.setText(time.get(i));
                        time.set(i,time.get(i));
                    }else{
                        t3Time.setText(time.get(i));
                        time.set(i,time.get(i));
                    }
                }
            }
        }
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        List<ParseObject> requested = obj.getList("requested");

        String id;
        if (view.getId()==R.id.t1Call){
            id = map.get("l1");
        }else if(view.getId()==R.id.t2Call){
            id = map.get("l2");
        }else{
            id = map.get("l3");
        }

        ParseObject temp = null;
        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                temp = requested.get(i);
            }
        }

        intent.setData(Uri.parse("tel:"+temp.getString("phone")));
        startActivity(intent);
    }

    public void fine(View view) {
        String id;
        if (view.getId()==R.id.t1Fine){
            l1.setVisibility(View.GONE);
            id = map.get("l1");
        }else if(view.getId()==R.id.t2Fine){
            l2.setVisibility(View.GONE);
            id = map.get("l2");
        }else{
            l3.setVisibility(View.GONE);
            id = map.get("l3");
        }
        ParseObject temp = null;
        List<ParseObject> requested = obj.getList("requested");
        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                temp = requested.get(i);
            }
        }

        //Adding to Fined
        obj.add("fined",temp);
        //removing from Requested
        ArrayList<ParseObject> removeObject = new ArrayList<>();
        removeObject.add(temp);
        obj.removeAll("requested",removeObject);
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(InterviewDay.this, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InterviewDay.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hire(View view) {

        if (view.getId()==R.id.t1Hire){
            h2.setEnabled(false);
            h3.setEnabled(false);
        }else if(view.getId()==R.id.t2Hire){
            h1.setEnabled(false);
            h3.setEnabled(false);
        }else{
            h1.setEnabled(false);
            h2.setEnabled(false);
        }

    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
    }

    public void submitI(View view) {
        String id=null;
        if(h1.isEnabled() && !h2.isEnabled() && !h3.isEnabled()){
            id = map.get("l1");

        }else if(!h1.isEnabled() && h2.isEnabled() && !h3.isEnabled()){
            id = map.get("l2");

        }else if(!h1.isEnabled() && !h2.isEnabled() && h3.isEnabled()){
            id = map.get("l3");
        }

        if(id==null){
            Toast.makeText(this, "Hire someone First", Toast.LENGTH_SHORT).show();
        }else{
            ParseObject temp = null;
            List<ParseObject> requested = obj.getList("requested");
            for (int i = 0; i < requested.size(); i++) {
                if(id.equals(requested.get(i).getObjectId())){
                    temp = requested.get(i);
                }
            }
            obj.put("hired",temp);

            //removing from Requested
            ArrayList<ParseObject> removeObject = new ArrayList<>();
            removeObject.add(temp);
            obj.removeAll("requested",removeObject);
            obj.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(InterviewDay.this, "Done", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(InterviewDay.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
