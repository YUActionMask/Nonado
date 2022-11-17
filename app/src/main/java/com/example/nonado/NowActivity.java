package com.example.nonado;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NowActivity extends AppCompatActivity {
    private TextView nowPerson1, nowPerson2;
    private ListView listView;
    private int now1 =0, now2=0;
    private String postId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Post = database.getReference("Post-User");
    private DatabaseReference Postin = database.getReference("Point");
    private ListViewAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now);
        nowPerson1 = findViewById(R.id.nowPerson1);
        nowPerson2 = findViewById(R.id.nowPerson2);
        listView = findViewById(R.id.listV);
        postId = getIntent().getStringExtra("postId");

        Post.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageData : snapshot.getChildren()) {
                    now1 ++;
                }
                nowPerson1.setText(now1 + "명");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ListViewAdapter();

        Postin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageData : snapshot.getChildren()) {
                    String msgP = messageData.getValue().toString();
                    String msg[] = msgP.split(",");
                    if(postId.equals(msg[3].substring(7))){
                        adapter.addItem(new PointHistory("보냄", msg[2].substring(8), Integer.parseInt(msg[0].substring(9))));
                        now2 ++;
                    }

                }
                nowPerson2.setText(now2 + "명");
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public class ListViewAdapter extends BaseAdapter {
        ArrayList<PointHistory> items = new ArrayList<PointHistory>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PointHistory item){
            items.add(item);
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final PointHistory pointHistory = items.get(position);

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_point_history, viewGroup, false);
            } else{
                View view = new View(context);
                view = (View) convertView;
            }

            TextView typeTv = (TextView) convertView.findViewById(R.id.typeTv);
            TextView pointNameTv = (TextView) convertView.findViewById(R.id.pointNameTv);
            TextView pointAmountTv = (TextView) convertView.findViewById(R.id.pointAmountTv);

            typeTv.setText(pointHistory.getType());
            pointNameTv.setText(pointHistory.getPointName());
            pointAmountTv.setText(Integer.toString(pointHistory.getAmount()));


            return convertView;
        }
    }
}
