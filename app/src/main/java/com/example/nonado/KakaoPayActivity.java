package com.example.nonado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

import java.util.HashMap;
import java.util.Map;

public class KakaoPayActivity extends AppCompatActivity {

    // Request 작업을 할 Queue
    static RequestQueue requestQueue;

    // 결제 정보를 받을 변수
    static String productName; // 상품 이름
    static String productPrice; // 상품 가격

    // 웹 뷰
    WebView webView;

    // json 파싱
    Gson gson;

    // 커스텀 웹 뷰 클라이언트
    MyWebViewClient myWebViewClient;

    // 결제 고유 번호
    String tidPin;

    // 결제 요청 토큰
    String pgToken;

    // 기본 생성자
    // - Activity는 기본 생성자가 없으면 Manifest에서 사용하지 못함.
    // - 만약 생성자를 오버라이딩 했다면 기본 생성자를 작성해 둘것!
    public KakaoPayActivity() {
        KakaoPayActivity.productName = "name";
        KakaoPayActivity.productPrice = "price";
    }

    // 상품 이름과 가격을 초기화할 생성자
    public KakaoPayActivity(String productName, String productPrice) {
        KakaoPayActivity.productName = productName;
        KakaoPayActivity.productPrice = productPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay);

        // 초기화
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        myWebViewClient = new MyWebViewClient();
        webView = findViewById(R.id.webView);
        gson = new Gson();

        // 웹 뷰 설정
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(myWebViewClient);

        // 실행 시 바로 결제 Http 통신 실행
        requestQueue.add(myWebViewClient.readyRequest);
    }

    public class MyWebViewClient extends WebViewClient {

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("milky", "error : " + error);
            }
        };

        Response.Listener<String> readyResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("milky", response);

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(response);

                String url = element.getAsJsonObject().get("next_redirect_mobile_url").getAsString();
                String tid = element.getAsJsonObject().get("tid").getAsString();

                Log.d("milky", "url : " +  url);
                Log.d("milky", "tid : " +  tid);

                webView.loadUrl(url);
                tidPin = tid;

            }
        };

        StringRequest readyRequest = new StringRequest(Request.Method.POST,  "https://kapi.kakao.com/v1/payment/ready", readyResponse, errorListener){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("milky", "name :  " + productName);
                Log.d("milky", "price:  " + productPrice);

                Map<String, String> params = new HashMap<>();
                params.put("cid", "TC0ONETIME"); // 가맹점 코드
                params.put("partner_order_id", "1001"); // 가맹점 주문 번호
                params.put("partner_user_id", "gorany"); // 가맹점 회원 아이디
                params.put("item_name", productName); // 상품 이름
                params.put("quantity", "1"); // 상품 수량
                params.put("total_amount", productPrice); // 상품 총액
                params.put("tax_free_amount", "0"); // 상품 비과세
                params.put("approval_url", "https://www.naver.com/success"); // 결제 성공시 돌려 받을 url 주소
                params.put("cancel_url", "https://www.naver.com/cancel"); // 결제 취소시 돌려 받을 url 주소
                params.put("fail_url", "https://www.naver.com/fali"); // 결제 실패시 돌려 받을 url 주소
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "KakaoAK " + "dfdf6d58a8a2977f93162ae5375c579a");
                return headers;
            }
        };

        Response.Listener<String> approvalResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("milky", response);
            }
        };


        StringRequest approvalRequest = new StringRequest(Request.Method.POST, "https://kapi.kakao.com/v1/payment/approve", approvalResponse, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cid", "TC0ONETIME");
                params.put("tid", tidPin);
                params.put("partner_order_id", "1001");
                params.put("partner_user_id", "gorany");
                params.put("pg_token", pgToken);
                params.put("total_amount", productPrice);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "KakaoAK " + "dfdf6d58a8a2977f93162ae5375c579a");
                return headers;
            }
        };


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("milky", "url" + url);

            if(url != null && url.contains("pg_token=")){
                String pg_Token = url.substring(url.indexOf("pg_token=") + 9);
                pgToken = pg_Token;

                requestQueue.add(approvalRequest);
            } else if (url != null && url.startsWith("intent://")) {
                try{
                    Intent intent  = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if(existPackage != null){
                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            view.loadUrl(url);
            return false;
        }
    }
}