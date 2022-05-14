package com.example.nonado;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MyinfoActivity extends AppCompatActivity {
    Button msgBtn;
    Button imageBtn;

    private String TAG = MyinfoActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;

    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    RecyclerView imageView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter MultiAdapter;  // 리사이클러뷰에 적용시킬 어댑터

    //사진 업로드용 uri
    private Uri mlmageCaptureUri;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        msgBtn = (Button) findViewById(R.id.msgBtn);
        imageBtn = (Button) findViewById(R.id.imageBtn);

        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        //사진 등록 버튼
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);

            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                MultiAdapter = new MultiImageAdapter(uriList, getApplicationContext());
                imageView.setAdapter(MultiAdapter);
                imageView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            }
        }
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

