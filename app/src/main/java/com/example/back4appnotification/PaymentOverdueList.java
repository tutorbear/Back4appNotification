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
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PaymentOverdueList extends AppCompatActivity {
    PaymentOverdueAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue_list);
        init();
        query();

    }

    private void init() {
        recycle = findViewById(R.id.recyclerView_payment_overdue);
        manager = new LinearLayoutManager(this);
    }

    private void query() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.whereLessThan("paymentDate", today);
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.isEmpty()){
                        Toast.makeText(PaymentOverdueList.this, "No Posts", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PaymentOverdueList.this, ""+objects.size(), Toast.LENGTH_SHORT).show();
                        obj = objects;
                        customAdapter = new PaymentOverdueAdapter(PaymentOverdueList.this, objects);
                        recycle.setAdapter(customAdapter);
                        recycle.setLayoutManager(manager);
                    }
                }else{
                    Toast.makeText(PaymentOverdueList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void viewP(View view) {
        int pos = (int) view.getTag();
        Toast.makeText(this, ""+pos, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,PaymentOverdue.class).putExtra("obj",obj.get(pos)));
    }
}

class PaymentOverdueAdapter extends RecyclerView.Adapter<PaymentOverdueAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> title;

    public PaymentOverdueAdapter(Context context, List<ParseObject> title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_payment_overdue,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setTag(position);
        holder.textView.setText("hello");//title.get(position).getDate("paymentDate").toString()
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
            textView = itemView.findViewById(R.id.txt_payment_overdue);
            button =itemView.findViewById(R.id.btn_payment_overdue);
        }
    }
}

