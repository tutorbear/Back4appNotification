package com.example.back4appnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class LockedJob extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t1U,t2N,t2U,t3N,t3U,t1Time,t2Time,t3Time,lastDate;
    LinearLayout l1,l2,l3;
    Button t1No,t2No,t3No;
    HashMap<String,String> map;
    ArrayList<String> time = new ArrayList<>(Arrays.asList("","",""));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_job);
        init();
        set();

    }

    private void init() {
        obj= getIntent().getParcelableExtra("obj");

        //Text views
        id = findViewById(R.id.jobIdJP);
        name = findViewById(R.id.nameJP);
        salary = findViewById(R.id.salaryJP);
        location = findViewById(R.id.locationJP);
        stdNumber = findViewById(R.id.studentNumberJP);
        sClass = findViewById(R.id.classJP);
        sub = findViewById(R.id.subjectJP);
        curr = findViewById(R.id.curriculumJP);
        address = findViewById(R.id.addressJP);
        //Teacher
        t1N = findViewById(R.id.t1N);
        t1U = findViewById(R.id.t1U);
        t2N = findViewById(R.id.t2N);
        t2U = findViewById(R.id.t2U);
        t3N = findViewById(R.id.t3N);
        t3U = findViewById(R.id.t3U);
        t1Time= findViewById(R.id.t1Time);
        t2Time = findViewById(R.id.t2Time);
        t3Time = findViewById(R.id.t3Time);
        lastDate = findViewById(R.id.lastDate);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Buttons
        t1No = findViewById(R.id.t1No);
        t2No = findViewById(R.id.t2No);
        t3No = findViewById(R.id.t3No);
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


    public void timeSetter(final View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(LockedJob.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
                if (hourOfDay >= 12) {
                    amPm = "PM";
                    hourOfDay = hourOfDay-12;
                } else {
                    amPm = "AM";
                }
                if (view.getId()==R.id.t1SetTime){
                    t1Time.setVisibility(View.VISIBLE);
                    t1Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    time.set(0,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    //putting the time array
                    obj.remove("interviewTime");
                    obj.addAll("interviewTime",time);
                }else if(view.getId()==R.id.t2SetTime){
                    t2Time.setVisibility(View.VISIBLE);
                    t2Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    time.set(1,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    obj.remove("interviewTime");
                    obj.addAll("interviewTime",time);
                }else{
                    t3Time.setVisibility(View.VISIBLE);
                    t3Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    time.set(2,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    obj.remove("interviewTime");
                    obj.addAll("interviewTime",time);
                }
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }




    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
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

    public void notGoing(View view) {
        //Todo: set time to " "
        String id;
        if (view.getId()==R.id.t1No){
            l1.setVisibility(View.GONE);
            id = map.get("l1");
        }else if(view.getId()==R.id.t2No){
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
        //Adding to removed
        obj.add("removed",temp);
        //removing from Requested
        ArrayList<ParseObject> removeObject = new ArrayList<>();
        removeObject.add(temp);
        obj.removeAll("requested",removeObject);
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(LockedJob.this, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void submit(View view) {
        //Todo: remember to check for "" values in time array
        //Removing teachers from applied to released
        List<ParseObject> temp = obj.getList("applied");
        if(temp!=null && !temp.isEmpty()){
            //Adding to released array
            obj.addAll("released",temp);
            //Clearing out applied list
            obj.remove("applied");
        }

        obj.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(LockedJob.this, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void datePicker(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        lastDate.setText(currentDateString);
        obj.put("interviewDate",c.getTime());
    }
}
