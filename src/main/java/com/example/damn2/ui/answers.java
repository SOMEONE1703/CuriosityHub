package com.example.damn2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.damn2.MainActivity;
import com.example.damn2.MainActivity2;
import com.example.damn2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// create a static method that takes an answer_layout parameter
public class answers extends AppCompatActivity {
    static int identity;
    String Username = log_in.Username;
    String question;
    ArrayList<answer_layout> answers=new ArrayList<>();
    LinearLayout who;
    int blue= R.color.bigger_box;
    int white=R.color.greyer;
    static Boolean ready=false;

    String he_who_asked="dummy";
    int width;
    Thread runner;
    static String text_to_be_added;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        setup();
        runner=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (ready){

                        break;
                    }
                }
            }
        });

    }
    public Boolean already_in(answer_layout which){
        for (answer_layout trial:answers){
            if (which.equals(trial)){
                return true;
            }
        }
        return false;
    }

    public void collect(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String question = item.getString("ANSWER");
                String brand = item.getString("VOTE");
                String user = item.getString("USERNAME");
                String id = item.getString("ANS_ID");
                int identity = Integer.parseInt(id);
                int count = Integer.parseInt(brand);
                answer_layout what = new answer_layout(this,question,count,identity,user,Username,width);
                answers.add(what);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void append(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String question = item.getString("ANSWER");
                String brand = item.getString("VOTE");
                String user = item.getString("USERNAME");
                String id = item.getString("ANS_ID");
                int identity = Integer.parseInt(id);
                int count = Integer.parseInt(brand);
                answer_layout what = new answer_layout(this,question,count,identity,user,Username,width);
                if (!already_in(what)){
                    answers.add(what);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void refresh(){
        who.removeAllViews();
        answers.clear();
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/answers_get.php").newBuilder();
        urlBuilder.addQueryParameter("identity", String.valueOf(identity));

        String url = urlBuilder.build().toString();
        //System.out.println(url);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String MyRes = response.body().string();
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            append(MyRes);
                            who.removeAllViews();
                            //System.out.println("collect is working properly");
                            int i=0;
                            for (answer_layout trial:answers){
                                //System.out.println("customs added properly");
                                ViewGroup parent = (ViewGroup) trial.getParent();

                                if (parent != null) {
                                    parent.removeView(trial);
                                }

                                trial.setColor(blue);
                                LinearLayout divider = new LinearLayout(answers.this);
                                LinearLayout.LayoutParams div= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,15);
                                divider.setLayoutParams(div);
                                divider.setBackgroundColor(getResources().getColor(R.color.greyer));
                                who.addView(divider);
                                who.addView(trial);

                            }
                        }
                    });
                }
            }
        });

    }
    public void setup(){
        identity=getIntent().getIntExtra("identity",0);
        question=getIntent().getStringExtra("question");
        he_who_asked=getIntent().getStringExtra("username");
        ScrollView one = (ScrollView) findViewById(R.id.something);
        TextView quest=findViewById(R.id.question);
        quest.setText(question);
        TextView asker=findViewById(R.id.asker);
        String word="Asked by: "+he_who_asked;
        asker.setText(word);
        who=new LinearLayout(this);
        who.setOrientation(LinearLayout.VERTICAL);
        one.addView(who);
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/answers_get.php").newBuilder();
        urlBuilder.addQueryParameter("identity", String.valueOf(identity));
//        urlBuilder.addQueryParameter("username", username);
//        urlBuilder.addQueryParameter("username", username);

        FloatingActionButton she= findViewById(R.id.floatingActionButton);
        she.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(answers.this,add_answer.class);
                startActivity(intent);
            }
        });
        FloatingActionButton he=findViewById(R.id.refresh);
        he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();

            }
        });

        String url = urlBuilder.build().toString();
        //System.out.println(url);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String MyRes = response.body().string();
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            collect(MyRes);
                            //System.out.println("collect is working properly");
                            for (answer_layout trial:answers){
                                //System.out.println("customs added properly");
                                trial.setColor(R.color.blue);
                                LinearLayout divider = new LinearLayout(answers.this);
                                LinearLayout.LayoutParams div= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,15);
                                divider.setLayoutParams(div);
                                divider.setBackgroundColor(getResources().getColor(R.color.greyer));
                                who.addView(divider);
                                who.addView(trial);
                            }
                        }
                    });
                }
            }
        });
    }


}