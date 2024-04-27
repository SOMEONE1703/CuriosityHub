package com.example.damn2.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.damn2.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class my_question extends LinearLayout{
    Context context;
    String user;
    TextView show;
    String message;
    int likes;

    ImageButton del;

    int votes;

    int identity;
    String curr_user;
    int width;


    public my_question(Context context,String text,int count,int identity,String user,String curr_user,int width) {
        super(context);
        this.identity=identity;
        this.votes=count;
        this.width=width;
        this.setOrientation(HORIZONTAL);
        likes=count;
        message=text;
        this.user=user;
        this.curr_user=curr_user;
        this.context=context;
        del=new ImageButton(context);
        del.setId(View.generateViewId());
        del.setBackgroundResource(R.drawable.baseline_delete_outline_30);
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        show = new TextView(context);
        show.setId(View.generateViewId());
        show.setTextColor(getResources().getColor(R.color.black));
        show.setText(message);
        //System.out.println("nothing");
        show.setTextSize(17);
        show.setText(message);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,answers.class);
                intent.putExtra("identity",identity);
                intent.putExtra("question",message);
                intent.putExtra("username",user);
                context.startActivity(intent);
            }
        });


        LinearLayout first=new LinearLayout(context);
        show.setMinimumHeight(150);
        first.setMinimumHeight(150);
        LinearLayout.LayoutParams what=new LinearLayout.LayoutParams(width-200, ViewGroup.LayoutParams.WRAP_CONTENT);
        what.weight=0;
        //first is the textView layout,you can add another textView for the name here
        first.addView(show,what);
        //show.setHeight(-1);
        LinearLayout second=new LinearLayout(context);
        second.setOrientation(HORIZONTAL);
        second.addView(del);
        addView(first);
        addView(second);
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setStroke(1,getResources().getColor(R.color.black));
        this.setBackground(gradientDrawable);

    }

    public void delete(){

        post_delete();
        ((ViewGroup)this.getParent()).removeView(this);
    }
    public void post_delete(){
        System.out.println("executing post_delete");
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/ultimate_delete.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(identity));
        //upvote.setText(votes);
        Request request = new Request.Builder()
                .url(who.build())
                .build();
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
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals("Successfully deleted records.\n")){
                            System.out.println("ultimate delete unsuccessful");
                        }
                        else{
                            System.out.println("ultimate delete successful");
                        }
                    }
                });
                //runner.start();
            }
        });
    }

    public void setColor(int i){
        this.setBackgroundColor(getResources().getColor(i));
    }

}

