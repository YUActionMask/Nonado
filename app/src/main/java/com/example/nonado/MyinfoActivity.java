package com.example.nonado;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import java.io.File;
import java.util.ArrayList;

public class MyinfoActivity extends AppCompatActivity {
    Button msgBtn;
    Button imageBtn;

    private String TAG = MyinfoActivity.class.getSimpleName();
    private ListView listView = null;
    private ListViewAdapter adapter = null;

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
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                new AlertDialog.Builder(MyinfoActivity.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();

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
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //임시파일 경로
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode){
            case PICK_FROM_ALBUM:
            {
                mlmageCaptureUri = data.getData();
                Log.d("SmartWheel", mlmageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA:
            {
                ㅑ
            }
            case CROP_FROM_IMAGE:{

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

