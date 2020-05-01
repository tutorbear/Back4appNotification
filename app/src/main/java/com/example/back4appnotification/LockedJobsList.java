package com.example.back4appnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LockedJobsList extends AppCompatActivity {

    LockedJobsAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_jobs_list);
        init();
        query();
    }

    private void init() {
        recycle = findViewById(R.id.locked_recycle);
        manager = new LinearLayoutManager(this);
    }

    private void query() {
        ParseCloud.callFunctionInBackground("teacherRequested", new HashMap<String, String>(), new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if (objects.size()==0){
                        Toast.makeText(LockedJobsList.this, "Nothing found", Toast.LENGTH_SHORT).show();
                    }else{
                        obj = objects;
                        customAdapter = new LockedJobsAdapter(LockedJobsList.this, objects);
                        recycle.setAdapter(customAdapter);
                        recycle.setLayoutManager(manager);
                    }
                }else{
                    Toast.makeText(LockedJobsList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void viewL(View view) {
        int pos = (int) view.getTag();
        startActivity(new Intent(this,LockedJob.class).putExtra("obj",obj.get(pos)));
    }
}

class LockedJobsAdapter extends RecyclerView.Adapter<LockedJobsAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> title;

    public LockedJobsAdapter(Context context, List<ParseObject> title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public LockedJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_locked_jobs,parent,false);
        LockedJobsAdapter.MyViewHolder holder = new LockedJobsAdapter.MyViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LockedJobsAdapter.MyViewHolder holder, int position) {
        holder.button.setTag(position);
        holder.textView.setText(title.get(position).getObjectId());
    }

    @Override
    public int getItemCount() {
        return title.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.locked_job_id);
            button =itemView.findViewById(R.id.locked_view);
        }
    }
}
