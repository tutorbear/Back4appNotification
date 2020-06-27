package com.example.back4appnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

public class InterviewDayList extends AppCompatActivity {
    ArrayList<ParseObject> list;
    InterviewDayAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_day_list);
        init();
    }

    private void init() {
        list = getIntent().getParcelableArrayListExtra("objects");
        recycle = findViewById(R.id.recyclerView_interviewDay);
        manager = new LinearLayoutManager(this);

        customAdapter = new InterviewDayAdapter(this, list);
        recycle.setAdapter(customAdapter);
        recycle.setLayoutManager(manager);

    }

    public void viewI(View view) {
        int pos = (int) view.getTag();
        startActivity(new Intent(this,InterviewDay.class).putExtra("object",list.get(pos)));
    }
}

class InterviewDayAdapter extends RecyclerView.Adapter<InterviewDayAdapter.MyViewHolder> {

    Context context;
    ArrayList<ParseObject> list;


    public InterviewDayAdapter(Context context, ArrayList<ParseObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_interview_day,parent,false);
        MyViewHolder  holder = new MyViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setTag(position);
        holder.textView.setText(list.get(position).getObjectId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_interDay);
            button = itemView.findViewById(R.id.btn_interDay);
        }
    }
}
