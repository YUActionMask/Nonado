package com.example.nonado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PointHistoryActivity extends AppCompatActivity {

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);

        listView = (ListView) findViewById(R.id.listview);
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