package com.example.nonado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

public class KakaoPayActivity extends AppCompatActivity {

    private String application_id = "634567e6d01c7e002293c145";


    Context context;

    Spinner spinner_pg;
    Spinner spinner_method;
    View v ;
//    EditText edit_price;
//    EditText edit_non_tax;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay);

        Log.d("bootpay", "milky");


        BootpayAnalytics.init(this, "634567e6d01c7e002293c145");

        v = (View) findViewById(R.id.payView);
        PaymentTest(v);
    }

    public void PaymentTest(View v) {
        BootUser user = new BootUser().setId("id"); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)


        List items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(2000d);
        items.add(item1);

        Log.d("bootpay", item1.getName());

        Payload payload = new Payload();
        payload.setApplicationId("5b8f6a4d396fa665fdc2b5e8")
                .setOrderName("포인트 충전")
                //.setPg("나이스페이")
                //.setMethod("네이버페이")
                .setOrderId("1234")
                .setPrice(2000d)
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
                        Log.d("done", data);
                    }
                }).requestPayment();
    }
}