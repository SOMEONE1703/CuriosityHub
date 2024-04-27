package com.example.damn2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class add_answer extends AppCompatActivity {
    String Username= log_in.Username;
    int identity=answers.identity;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        EditText l=findViewById(R.id.answerIn);
        l.setWidth(width);
        Button m = findViewById(R.id.add);
        m.setWidth(width);

    }

    public void post(View t){

        EditText she = findViewById(R.id.answerIn);
        String answer = she.getText().toString();

        if (answer.equals("")){
            //him.setText("Enter valid answer");
            Toast.makeText(this, "Enter valid answer", Toast.LENGTH_LONG).show();
            return;
        }

        if (answer.length()>=500){
            //she.setText(answer);
            //him.setText("Question too long");
            Toast.makeText(this, "Answer must be less than 500 characters", Toast.LENGTH_LONG).show();
        }
        she.setText("");

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2553616/answers_post.php").newBuilder();
        urlBuilder.addQueryParameter("username", Username);
        urlBuilder.addQueryParameter("answer", answer);
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
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code "+ response);
                }

                final String MyRes = response.body().string();
                Thread runner = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals("Success")){
                            System.out.println("answer post unsuccessful");
                            TextView him=(TextView) findViewById(R.id.textView4);
                            Toast.makeText(add_answer.this, "Network error, Check internet connection and try again", Toast.LENGTH_LONG).show();
                            //him.setText("Network error, Check internet connection and try again");
                        }
                        else{
                            TextView him=(TextView) findViewById(R.id.textView4);
                            answers.ready=true;
                            Toast.makeText(add_answer.this, "Answer posted successfully, Press back", Toast.LENGTH_LONG).show();
                            //him.setText("Answer posted successfully, Press back");
                            //finish();
                            //System.out.println("Just so it doesn't whine");
                        }
                    }
                });
                runner.start();
            }
        });

    }

    public void done(View t){
        Intent intent = new Intent(this,answers.class);
        startActivity(intent);
    }



}