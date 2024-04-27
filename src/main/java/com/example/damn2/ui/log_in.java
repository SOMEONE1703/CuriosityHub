package com.example.damn2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damn2.MainActivity;
import com.example.damn2.MainActivity2;
import com.example.damn2.R;

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

public class log_in extends AppCompatActivity {

    public static String Username=MainActivity.Username;
    int width;
    public DatabaseHelper myDB=MainActivity.myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        System.out.println("problem is here");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        EditText l=findViewById(R.id.naam);
        l.setWidth(width);
        EditText m=findViewById(R.id.pass);
        m.setWidth(width);
        Button o= findViewById(R.id.button);
        o.setWidth(width);
    }

    public Boolean processJSON(String json,String ju,String jp){
        try {
            //System.out.println(json);
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String u = item.getString("username");
                String p = item.getString("password");
                if(u.equals(ju) &  p.equals(jp) ){
                    Username=u;
                    return true;
                }

            }
            // return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void sign(View t){
        Intent intent = new Intent(this, sign_up.class);
        startActivity(intent);
    }

    public void log(View t){
        EditText g = (EditText) findViewById(R.id.naam);
        String name = g.getText().toString();

        if(name.equals("")){
            //TextView dum = findViewById(R.id.check);
            //dum.setText("Username cannot be empty");
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        EditText f = (EditText) findViewById(R.id.pass);
        String pass =  f.getText().toString();
        if(pass.equals("")){
            //TextView dum = findViewById(R.id.check);
            //dum.setText("Password cannot be empty");
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
//        String[] vals = {name};
//        myDB.doUpdate("insert into LOG(USERNAME,STATUS) values(?,1)",vals);
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/log.php").newBuilder();
        urlBuilder.addQueryParameter("username",name);
        urlBuilder.addQueryParameter("password",pass);
        String url = urlBuilder.build().toString();


        Request request = new Request.Builder().url(url).build();
        //System.out.println(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String MyRes = response.body().string();
                    System.out.print("Squeeze:");
                    System.out.print(MyRes);
                    System.out.println(":theorem");
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (processJSON(MyRes,name,pass)){
                                String[] val={name};
                                myDB.doUpdate("insert into LOG(USERNAME,STATUS) values(?,1);",val);
                                Intent actual = new Intent(log_in.this, MainActivity2.class);
                                startActivity(actual);
                                finish();

                            }
                            else{
                                //TextView dumb = findViewById(R.id.check);
                                //dumb.setText("Invalid login, please try again");
                                Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
                                Toast.makeText(context, "Invalid login, please try again", Toast.LENGTH_SHORT).show();
                                //System.out.println("WRONG PASSWORD");

                                //Intent intent=new Intent(this,log_in.class);
                                //startActivity(intent);
                                //log(t);
                            }
                        }
                    });

                }
                else{
                    //TextView dumb = findViewById(R.id.check);
                    //dumb.setText("Something went wrong, please try again");
                    Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
                    Toast.makeText(context, "Something went wrong,please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void forgot(View t){
        Intent intent=new Intent(this,forgot.class);
        startActivity(intent);
    }
}