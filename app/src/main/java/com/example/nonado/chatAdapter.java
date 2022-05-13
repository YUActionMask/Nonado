package com.example.nonado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private List<Chat> chatList;
    private String name;


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTv;
        private TextView msgTv;
        private View rootView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

             nameTv = itemView.findViewById(R.id.nameTv);
             msgTv = itemView.findViewById(R.id.msgTv);
             rootView = itemView;

             itemView.setEnabled(true);
             itemView.setClickable(true);
        }
    }
    public ChatAdapter(List<Chat> chatData, String name){
        chatList = chatData;
        this.name = name;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(linearLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.nameTv.setText(chat.getName());
        holder.msgTv.setText(chat.getMsg());
    }

    @Override
    public int getItemCount() {
        if(chatList == null){
            return 0;
        }
        else{
            return chatList.size();
        }
    }

    //추가 메소드
    public void addChat(Chat chat){
        chatList.add(chat);
        notifyItemInserted(chatList.size() - 1);
    }

}
