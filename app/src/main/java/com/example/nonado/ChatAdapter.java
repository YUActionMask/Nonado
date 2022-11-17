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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private List<Chat> chatList;
    private String name;
    private String postId;

    private String msg;
    private String sender;
    private String token;
    private FirebaseUser user;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public ChatAdapter(List<Chat> chatData, String name, String postId){
        chatList = chatData;
        this.name = name;
        this.postId = postId;
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

            if (chat.getName().equals(this.name)) {
                holder.nameTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                holder.msgTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                holder.msgLinear.setGravity(Gravity.RIGHT);
            } else {
                holder.nameTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                holder.msgTv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

                holder.msgLinear.setGravity(Gravity.LEFT);

                mDatabase.child("User").child(this.name).child("token").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            token = task.getResult().toString();
                        }
                    }
                });
                msg = chat.getMsg();
                sender = this.name;
                //현재 채팅보낸 사람과 현재 로그인한 사람의 이름이 다른경우 메시지 fcm 알림 보내기
                //sendGson(token, sender,msg);
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
