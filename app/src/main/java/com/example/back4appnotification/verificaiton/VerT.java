package com.example.back4appnotification.verificaiton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.back4appnotification.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;

public class VerT extends AppCompatActivity {
    ParseObject obj;
    TextView name,username,gender,currentEdu,curriculum,background,schoolName,uniName,uniProgram,address,phone;
    ImageView proPic,nId,certificate,idCard;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_t);
        init();
        set();
    }


    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //TextViews
        name=findViewById(R.id.nameTP);
        username=findViewById(R.id.usernameTP);
        gender=findViewById(R.id.genderTP);
        currentEdu=findViewById(R.id.cEduTP);
        curriculum=findViewById(R.id.curriculumTP);
        background=findViewById(R.id.backgroundTP);
        schoolName=findViewById(R.id.schoolNameTP);
        uniName=findViewById(R.id.uniNameTP);
        uniProgram=findViewById(R.id.uniProgramTP);
        address=findViewById(R.id.addressTP);
        phone=findViewById(R.id.phoneTP);
        //Image Vies
        proPic = findViewById(R.id.proPicTP);
        nId = findViewById(R.id.nIdTP);
        certificate=findViewById(R.id.certificateTP);
        idCard=findViewById(R.id.idCardTP);
        //progress bar
        progress = findViewById(R.id.proPicBarTP);
    }

    private void set() {
        // Text Views
        name.setText("full Name: "+obj.getString("fullName"));
        username.setText("username: "+obj.getString("username"));
        gender.setText("gender: "+obj.getString("gender"));
        currentEdu.setText("currentEducation: "+obj.getString("currentEducation"));
        curriculum.setText("curriculum: "+obj.getString("curriculum"));
        background.setText("background: "+obj.getString("background"));
        schoolName.setText("schoolName: "+obj.getString("schoolName"));
        address.setText("address: "+obj.getString("address"));
        //Images
        ParseFile proPicFile = obj.getParseFile("proPic");
        proPicFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null){
                    Glide.with(VerT.this)
                            .load(data)
                            .into(proPic);
                }
            }
        });

        ParseFile nIdFile = obj.getParseFile("document");
        nIdFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null){
                    Glide.with(VerT.this)
                            .load(data)
                            .into(nId);

                }
            }
        }, new ProgressCallback() {
            public void done(Integer percentDone) {
                progress.setProgress(percentDone);
                if (percentDone==100){
                    progress.setVisibility(View.GONE);
                }
            }
        });

        ParseFile certificateFile = obj.getParseFile("certificate");
        certificateFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null){
                    Glide.with(VerT.this)
                            .load(data)
                            .into(certificate);
                }
            }
        });

        if (obj.getString("university")!=null){
            uniName.setText("university: "+obj.getString("university"));
            uniProgram.setText("uniProgram: "+obj.getString("uniProgram"));

            ParseFile idCardFile = obj.getParseFile("document");
            idCardFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e==null){
                        Glide.with(VerT.this)
                                .load(data)
                                .into(idCard);
                    }
                }
            });
        }else {
            uniName.setVisibility(View.GONE);
            uniProgram.setVisibility(View.GONE);
            idCard.setVisibility(View.GONE);
        }
    }

    public void verifyT(View view) {
        obj.put("verified",true);
        obj.saveEventually();
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getString("phone")));
        startActivity(intent);
    }

    public void rejectT(View view) {
        obj.put("verFailed",true);
        obj.saveEventually();
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }
}
