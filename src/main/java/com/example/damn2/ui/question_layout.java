package com.example.damn2.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.damn2.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class question_layout extends CardView {

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

    public question_layout(Context context,String text,int count,int identity,String user,String curr_user,int width) {
        super(context);
        inflate(context,R.layout.question_layout,this);
        this.setCardElevation(0);
        this.identity=identity;
        this.votes=count;
        this.width=width;
        likes=count;
        message=text;
        this.user=user;
        this.curr_user=curr_user;
        this.context=context;
        this.setMinimumHeight(100);
        ViewGroup.LayoutParams why= new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(why);
        del=findViewById(R.id.del);
        //del.setBackgroundResource(R.drawable.baseline_delete_outline_30);
        del.setImageDrawable(getResources().getDrawable(R.drawable.baseline_delete_31));
        del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        show = findViewById(R.id.message);
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
        this.setBackgroundColor(i);
    }
}
