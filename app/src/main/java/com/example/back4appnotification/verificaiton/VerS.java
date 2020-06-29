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

public class VerS extends AppCompatActivity {
    TextView gName,relation;
    ImageView gNId,idCardS;
    ProgressBar progress;
    ParseObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_s_view);
        init();
        set();
    }

    private void set() {
        // Text Views
        gName.setText("Guardians Name: "+obj.getString("guardianName"));
        relation.setText("Relation: "+obj.getString("relation"));

        //Images
        ParseFile gNIdFile = obj.getParseFile("nId");
        gNIdFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null){
                    Glide.with(VerS.this)
                            .load(data)
                            .into(gNId);
                }
            }
        });

        ParseFile idCard = obj.getParseFile("studentId");
        idCard.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null){
                    Glide.with(VerS.this)
                            .load(data)
                            .into(idCardS);

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
    }

    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //TextViews
        gName=findViewById(R.id.gName);
        relation=findViewById(R.id.relation);
        //Image Vies
        gNId = findViewById(R.id.nIdSPR);
        idCardS = findViewById(R.id.idCardSPR);

        //progress bar
        progress = findViewById(R.id.progressBarSPR);
    }

    public void verifyS(View view) {
        obj.put("verified",true);
        obj.saveEventually();
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getString("phone")));
        startActivity(intent);
    }

    public void rejectS(View view) {
        obj.put("verFailed",true);
        obj.saveEventually();
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }
}
