package com.example.nonado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PointHistoryActivity extends AppCompatActivity {

    private ListView listView = null;
    private ListViewAdapter adapter = null;
    private Button chargingBtn;
    private TextView retentionPoint;

    //DB
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);

        listView = (ListView) findViewById(R.id.listview);
        chargingBtn = (Button) findViewById(R.id.chargingBtn);
        retentionPoint = (TextView) findViewById(R.id.retentionPoint);

        //DB
        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getEmail().split("@")[0];
        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(user_id);

        chargingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(PointHistoryActivity.this);

                AlertDialog.Builder dlg = new AlertDialog.Builder(PointHistoryActivity.this);
                dlg.setTitle("충전할 포인트를 입력하세요");
                dlg.setView(editText);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String price = (editText.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                       //intent.putExtra("price", price);
                        intent.putExtra("price", price);
                        startActivity(intent);
                    }
                });
                dlg.show();

            }
        });

        adapter = new ListViewAdapter();

        //임시정보 저장
        adapter.addItem(new Point("입금", "A", 1000));
        adapter.addItem(new Point("입금", "B", 2300));
        adapter.addItem(new Point("출금", "C", 4533));
        adapter.addItem(new Point("입금", "D", 1234));
        adapter.addItem(new Point("출금", "E", 12000));
        adapter.addItem(new Point("출금", "F", 12455));
        adapter.addItem(new Point("입금", "G", 12466));

        listView.setAdapter(adapter);

        //현재 보유한 포인트
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userPoint = dataSnapshot.child("point").getValue().toString();
                retentionPoint.setText(userPoint);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("milky", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<Point> items = new ArrayList<Point>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Point item){
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
            final Point point = items.get(position);

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

            typeTv.setText(point.getType());
            pointNameTv.setText(point.getPointName());
            pointAmountTv.setText(point.getAmount().toString());


            return convertView;
        }
    }
}