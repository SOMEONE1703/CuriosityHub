package com.example.damn2.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

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

public class trial extends CardView{
    DatabaseHelper myDB= MainActivity.myDB;
    ConstraintLayout lay;
    Context context;
    ImageButton upvote;
    String user;
    TextView show;
    TextView vote_count;
    TextView userName;
    String message;
    int likes;


    Boolean truth;

    String curr_user;

    int votes;

    int identity;
    int width;




    public trial(Context context,String text,int count,int identity,String user,String curr_user,int width) {
        super(context);
        inflate(context,R.layout.another_trial,this);
        this.setCardElevation(0);
        this.identity=identity;
        this.votes=count;
        likes=count;
        message=text;
        this.user=user;
        this.context=context;
        this.width=width;
        this.curr_user=curr_user;
        show = findViewById(R.id.mess);
        vote_count=findViewById(R.id.vote_count);
        show.setTextColor(getResources().getColor(R.color.white));
        show.setText(message);
        upvote=findViewById(R.id.upvote);
        userName=findViewById(R.id.user);
        this.setMinimumHeight(100);
        ViewGroup.LayoutParams why= new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(why);
//        LinearLayout s=(LinearLayout) findViewById(R.id.to_be_set);
//        LinearLayout.LayoutParams setter = new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT);
//        s.setLayoutParams(setter);
//        GradientDrawable gradientDrawable=new GradientDrawable();
//        gradientDrawable.setStroke(1,getResources().getColor(R.color.black));
//        s.setBackground(gradientDrawable);
        userName.setText(user);

        if (able()){
            //System.out.println("well this is odd");
            //upvote.Ba(@drawable/baseline_thumb_up_off_alt_27);
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
        }
        else{
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
        }

//        vote_count.setTextSize(17);
//        vote_count.setTextColor(getResources().getColor(R.color.black));
        vote_count.setText(String.valueOf(likes));
        //System.out.println("nothing");
//        show.setTextSize(17);
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

        upvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user.equals(curr_user)){
                    //to do something here maybe
                    if (able()){
                        setUpvote(identity);
                        upvote.setBackgroundResource(R.drawable.baseline_thumb_up_20_blue);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
                    }
                    else{
                        setDownVote(identity);
                        upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_20_blue);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                    }
                }
                //show.setText("Button working");
            }
        });

        //System.out.println("constructor is done");
    }

    public void setUpvote(int where){
        voteAdd(where);
        votes++;
        //upvote.setBackgroundResource(R.drawable.baseline_arrow);
        vote_count.setText(String.valueOf(votes));

        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_vote.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(where));
        who.addQueryParameter("vote", String.valueOf(votes));
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
                Thread runner = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals(" Success\n")){
                            System.out.println("vote unsuccessful");
                        }
                        else{
                            System.out.println("voting worked");
                        }
                    }
                });
                runner.start();
            }
        });
        upvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.equals(curr_user)){
                    setDownVote(identity);
                    upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_20_blue);
                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                }
            }
        });


    }

    public void setDownVote(int where){
        voteDelete(where);
        votes--;
        //upvote.setBackgroundResource(R.drawable.baseline_arrow_up);
        vote_count.setText(String.valueOf(votes));
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_vote.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(where));
        who.addQueryParameter("vote", String.valueOf(votes));
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
                Thread runner = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals(" Success\n")){
                            System.out.println("vote unsuccessful");
                        }
                        else{
                            System.out.println("voting worked");
                        }
                    }
                });
                runner.start();
            }
        });
        upvote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.equals(curr_user)){
                    //to do something here maybe
                    if (able()) {
                        setUpvote(identity);
                        upvote.setBackgroundResource(R.drawable.baseline_thumb_up_20_blue);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
                    }
                    else{
                        setDownVote(identity);
                        upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_20_blue);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                    }
                }
            }
        });

    }


    public void voteAdd(int id){
        String[] vals={curr_user, String.valueOf(id)};
        myDB.doUpdate("insert into QUESTION_VOTES(USERNAME,POST_ID) values(?,?)",vals);
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_vote_add.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(id));
        who.addQueryParameter("username", curr_user);
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
                Thread runner = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals(" Success\n")){
                            System.out.println("vote unsuccessful");
                        }
                        else{
                            System.out.println("voting worked");
                        }
                    }
                });
                runner.start();
            }
        });


    }

    public void voteDelete(int id){
        String[] where={curr_user,String.valueOf(id)};
        myDB.doUpdate("delete from QUESTION_VOTES where USERNAME=? and POST_ID=?",where);

        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_vote_del.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(id));
        who.addQueryParameter("username", curr_user);
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
                Thread runner = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!MyRes.equals(" Success\n")){
                            System.out.println("vote unsuccessful");
                        }
                        else{
                            System.out.println("voting worked");
                        }
                    }
                });
                runner.start();
            }
        });


    }

    public Boolean able(){
        String[] wha={String.valueOf(identity)};
        Cursor c=myDB.doQuery("select * from QUESTION_VOTES where POST_ID=?",wha);
        while (c.moveToNext()){
            String damn= c.getString(c.getColumnIndex("USERNAME"));
            //System.out.println(damn);
            if (damn.equals(curr_user)){
                return false;
            }
        }
        c.close();
        return true;

    }

    public void setColor(int i){
        LinearLayout l=findViewById(R.id.to_be_set);
        l.setBackgroundColor(i);
        this.setBackgroundColor(getResources().getColor(R.color.text_grey));

    }

}