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

public class MyinfoActivity extends AppCompatActivity {

    private String TAG = MyinfoActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        //오류수정
        

        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter();

        //추가 정보 담기
        adapter.addItem(new Posting("A"));
        adapter.addItem(new Posting("B"));
        adapter.addItem(new Posting("C"));
        adapter.addItem(new Posting("D"));
        adapter.addItem(new Posting("E"));
        adapter.addItem(new Posting("F"));

        listView.setAdapter(adapter);
    }

    public class ListViewAdapter extends BaseAdapter{
        ArrayList <Posting> items = new ArrayList<Posting>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Posting item){
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
            final Posting posting = items.get(position);

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_posting, viewGroup, false);
            } else{
                View view = new View(context);
                view = (View) convertView;
            }

            TextView postingTv = (TextView) convertView.findViewById(R.id.postingTv);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            postingTv.setText(posting.getPostName());
            Log.d(TAG, "getView() - ["+position+"] "+posting.getPostName());


            return convertView;
        }
    }
}