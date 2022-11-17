package com.example.nonado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.*;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;

public class PayActivity extends AppCompatActivity {

    private  double price;
    private String user_id;
    private String userPoint = "0";
    View v ;

    //DB
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference myPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        String strPrice = intent.getStringExtra("price");
        price = Double.parseDouble(strPrice);

        //DB
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = user.getEmail().split("@")[0];
        Log.d("bootpay", user_id);
        mDatabase = FirebaseDatabase.getInstance().getReference("User").child(user_id);
        myPoint= FirebaseDatabase.getInstance().getReference("Point");

        BootpayAnalytics.init(this, "634567e6d01c7e002293c145");

        // 현재 포인트
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPoint = dataSnapshot.child("point").getValue().toString();
                Log.d("bootpay", "userPoint : " + userPoint);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("bootpay", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        v = (View) findViewById(R.id.payView);
        PaymentTest(v);
    }

    public void PaymentTest(View v) {

        BootUser user = new BootUser().setId(user_id); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)


        List items = new ArrayList<>();
        BootItem item1 = new BootItem().setName(user_id + "_point").setId(user_id + "_POINT").setQty(1).setPrice(price);
        items.add(item1);


        Payload payload = new Payload();
        payload.setApplicationId("5b8f6a4d396fa665fdc2b5e8")
                .setOrderName("포인트 충전")
                //.setPg("나이스페이")
                //.setMethod("네이버페이")
                .setOrderId("1234")
                .setPrice(price)
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Map map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));






        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close: " );


                        /**********포인트 충전 완료되면 처리되어야 하는 부분*************/
                        Map<String, Object> update = new HashMap<>();
                        int setPoint = (int) price + Integer.parseInt(userPoint);
                        String setPointStr = Integer.toString(setPoint);
                        update.put("point", setPointStr);
                        mDatabase.updateChildren(update);


                        Point point = new Point();
                        point.setBalance((int) (price));
                        point.setCertification("0");
                        point.setReceiver(user_id);
                        point.setSender("");
                        point.setTitle("");

                        myPoint.push().setValue(point);

                        /**********포인트 충전 완료되면 처리되어야 하는 부분*************/



                        Intent intent = new Intent(getApplicationContext(), PointHistoryActivity.class);
                        //intent.putExtra("price", price);
                        startActivity(intent);
                    }


                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " +data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
//                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                        return true; //재고가 있어서 결제를 진행하려 할때 true (방법 2)
//                        return false; //결제를 진행하지 않을때 false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("bootpay", data);
                    }
                }).requestPayment();
    }
}