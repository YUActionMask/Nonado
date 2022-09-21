package com.example.nonado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private final List data;

    public CommentAdapter(List data){
        this.data =data;
    }

    @Override
    public int getCount(){
        return data.size();
    }
    @Override
    public Object getItem(int i){
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_comment,viewGroup,false);
        TextView id = view.findViewById(R.id.cmt_userid_tv);
        TextView comment = view.findViewById(R.id.cmt_content_tv);
        TextView date = view.findViewById(R.id.cmt_date_tv);

        Comment com = (Comment) data.get(i);
        id.setText(com.getId());
        comment.setText(com.getComment());
        date.setText(com.getDate());
        return view;
    }
}
