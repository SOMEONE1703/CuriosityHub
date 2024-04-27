package com.example.damn2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damn2.MainActivity;
import com.example.damn2.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sign_up extends AppCompatActivity {
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        EditText l=findViewById(R.id.username);
        l.setWidth(width);
        EditText m=findViewById(R.id.Password);
        m.setWidth(width);
        EditText n=findViewById(R.id.re_enter);
        n.setWidth(width);
        Button o= findViewById(R.id.sign);
        o.setWidth(width);

    }

    public void sign(View t){
        EditText g = (EditText) findViewById(R.id.username);
        String name = g.getText().toString();
        EditText f = (EditText) findViewById(R.id.Password);
        String pass =  f.getText().toString();
        EditText h = (EditText) findViewById(R.id.re_enter);
        String pass2 =  h.getText().toString();
        //TextView check=findViewById(R.id.error);
        if (name.equals("")){
            //check.setText("Username cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Username cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.equals("")){
            //check.setText("Password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass2.equals("")){
            //check.setText("Re-enter password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Re-enter password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(pass2)){
            //check.setText("Passwords do not match");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length()>15){
            //check.setText("Password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Password must be less than 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length()<=5){
            //check.setText("Password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Password too short!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length()<=5){
            //check.setText("Password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Username too short!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length()>15){
            //check.setText("Password cannot be blank");
            Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
            Toast.makeText(context, "Username must be less than 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/sign.php").newBuilder();
        urlBuilder.addQueryParameter("username",name);
        urlBuilder.addQueryParameter("password",pass);
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
//                    System.out.print("Squeeze:");
//                    System.out.print(MyRes);
//                    System.out.println(":theorem");
                    sign_up.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!MyRes.equals("Success")){
                                //problem tell user that username is taken
                                //TextView who= (TextView) findViewById(R.id.error);
                                //who.setText("Username taken");
                                Context context = getApplicationContext(); // or use 'MainActivity.this' if you are inside an Activity
                                Toast.makeText(context, "Username taken", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent=new Intent(sign_up.this, log_in.class);
                                startActivity(intent);
                                //System.out.println("Just so it doesn't whine");
                            }
                        }
                    });
                }
            }
        });

    }



}