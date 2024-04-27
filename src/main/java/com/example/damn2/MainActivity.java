package com.example.damn2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.damn2.ui.DatabaseHelper;
import com.example.damn2.ui.log_in;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// what's left is taking some load off the main thread, sharing the workload, SQLite workload
public class MainActivity extends AppCompatActivity {

    public static String Username;

    public static DatabaseHelper myDB;

    String logs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB=new DatabaseHelper(this,"app");
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        LinearLayout the = findViewById(R.id.cont);
        //the.setOrientation(LinearLayout.HORIZONTAL);
        enable_question();
        enable_answer();
        if (logged()){
            Button she = new Button(this);
            she.setWidth(356);
            she.setHeight(46);
            she.setTextColor(getResources().getColor(R.color.white));
            she.setBackgroundResource(R.drawable.custom_button);
            she.setText("USE ANOTHER ACCOUNT");
            she.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDB.doUpdate("delete from LOG");
                    Intent intent = new Intent(MainActivity.this, log_in.class);
                    startActivity(intent);
                }
            });
            LinearLayout l=new LinearLayout(this);
            LinearLayout.LayoutParams w=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20);
            l.setLayoutParams(w);
            Button he=new Button(this);
            he.setWidth(356);
            he.setHeight(46);
            he.setTextColor(getResources().getColor(R.color.white));
            he.setBackgroundResource(R.drawable.custom_button);
            Username=logs;
            String lo="Continue as "+logs;
            he.setText(lo);
            he.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(intent);
                }
            });

            the.addView(she);
            the.addView(l);
            the.addView(he);
        }
        else{
            Button him=new Button(this);
            him.setWidth(356);
            him.setHeight(46);
            him.setTextColor(getResources().getColor(R.color.white));
            him.setBackgroundResource(R.drawable.custom_button);
            him.setText("Continue to login");
            him.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,log_in.class);
                    startActivity(intent);
                }
            });
            the.addView(him);
        }

    }

//    public void onBackPressed(){
//        moveTaskToBack(true);
//        //android.os.Process.killProcess(android.os.Process.myPid());
//        //System.exit(1);
//    }


    public Boolean logged(){
        Cursor c = myDB.doQuery("select * from LOG where STATUS=1");
//        System.out.println("checking");
        while (c.moveToNext()){
            String what=c.getString(c.getColumnIndex("USERNAME"));
            if (!what.equals("")){
                logs=what;
                return true;
            }
        }
        c.close();
        return false;
    }


    public void enable_question(){

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/question_vote_get.php").newBuilder();
        String url = urlBuilder.build().toString();

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
                    Thread runner = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            processJSON(MyRes);
                        }
                    });
                    runner.start();
                }
            }
        });
        //System.out.println("Done with enable call");

    }

    public void enable_answer(){

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/answer_vote_get.php").newBuilder();
        String url = urlBuilder.build().toString();

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
                    Thread runner=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            processJSON_answer(MyRes);
                        }
                    });
                    runner.start();
                }
            }
        });
        //System.out.println("Done with enable call");

    }
    public void processJSON(String json){
        try {
            //System.out.println(json);
            myDB.doUpdate("delete from QUESTION_VOTES");
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                //System.out.println("iteration"+i);
                JSONObject item=all.getJSONObject(i);
                String u = item.getString("USERNAME");
                String id = item.getString("POST_ID");
                String[] vals = {u,id};
                myDB.doUpdate("Insert into QUESTION_VOTES(USERNAME,POST_ID) values(?,?)",vals);

            }
            //System.out.println("done processing likes");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processJSON_answer(String json){
        try {
            //System.out.println(json);
            myDB.doUpdate("delete from ANSWER_VOTES");
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                //System.out.println("iteration"+i);
                JSONObject item=all.getJSONObject(i);
                String u = item.getString("USERNAME");
                String id = item.getString("ANS_ID");
                String[] vals = {u,id};
                myDB.doUpdate("Insert into ANSWER_VOTES(USERNAME,ANS_ID) values(?,?)",vals);

            }
            //System.out.println("done processing likes");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}