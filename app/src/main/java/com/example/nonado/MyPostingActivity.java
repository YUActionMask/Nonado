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

public class MyPostingActivity extends AppCompatActivity {

    private ListView listView = null;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posting);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter();

        adapter.addItem(new Posting("A", "김"));
        adapter.addItem(new Posting("B", "이"));
        adapter.addItem(new Posting("C", "박"));
        adapter.addItem(new Posting("D", "최"));
        adapter.addItem(new Posting("E", "윤"));
        adapter.addItem(new Posting("F", "강"));

        listView.setAdapter(adapter);


    }

    public class ListViewAdapter extends BaseAdapter {
        ArrayList<Posting> items = new ArrayList<Posting>();

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
                convertView = inflater.inflate(R.layout.row_my_posting, viewGroup, false);
            } else{
                View view = new View(context);
                view = (View) convertView;
            }

            TextView postNameTv = (TextView) convertView.findViewById(R.id.postNameTv);
            TextView writerTv = (TextView) convertView.findViewById(R.id.writerTv);

            postNameTv.setText(posting.getPostName());
            writerTv.setText(posting.getWriter());


            return convertView;
        }
    }
}