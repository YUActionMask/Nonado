package com.example.nonado;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private List<Chat> chatList;
    private String name;

    public ChatAdapter(List<Chat> chatData, String name){
        chatList = chatData;
        this.name = name;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTv;
        private TextView msgTv;
        private View rootView;
        public LinearLayout msgLinear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

             nameTv = itemView.findViewById(R.id.nameTv);
             msgTv = itemView.findViewById(R.id.msgTv);
            msgLinear = itemView.findViewById(R.id.msgLinear);
             rootView = itemView;

             itemView.setEnabled(true);
             itemView.setClickable(true);

        }
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(linearLayout);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.nameTv.setText(chat.getName());
        holder.msgTv.setText(chat.getMsg());

        if(chat.getName().equals(this.name)){
            holder.nameTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            holder.msgLinear.setGravity(Gravity.RIGHT);
        } else {
            holder.nameTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            holder.msgLinear.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0: chatList.size();
    }

    //추가 메소드
    public void addChat(Chat chat){
        chatList.add(chat);
        notifyItemInserted(chatList.size() - 1);
    }

}
