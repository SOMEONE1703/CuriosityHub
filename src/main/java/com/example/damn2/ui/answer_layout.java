package com.example.damn2.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.damn2.MainActivity;
import com.example.damn2.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class answer_layout extends CardView {
    DatabaseHelper myDB= MainActivity.myDB;

    ConstraintLayout lay;
    Context context;
    ImageButton upvote;
    TextView show;
    String user;
    String message;
    int likes;
    TextView vote_count;
    String curr_user;
    int width;

    TextView he_who_answered;

    int votes;

    int identity;

    public answer_layout(Context context, String text, int count, int identity,String user,String curr_user,int width) {
        super(context);
        inflate(context,R.layout.answer_new,this);
        this.setCardElevation(0);
        this.identity=identity;
        this.votes=count;
        this.width=width;
        likes=count;
        message=text;
        this.context=context;
        this.user=user;
        this.curr_user=curr_user;
        vote_count=findViewById(R.id.vote_count);

        he_who_answered = findViewById(R.id.user);
        he_who_answered.setText(user);

        vote_count.setText(String.valueOf(votes));
        //System.out.println("just a hunch");
        show = findViewById(R.id.mess);
        show.setText(message);
        upvote=findViewById(R.id.upvote);
        //System.out.println("unamanga");
        this.setMinimumHeight(70);
        ViewGroup.LayoutParams why= new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(why);
        if (able()){
            //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24);
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
        }
        else{
            //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_24);
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
        }
        if (user.equals(curr_user)){
            //upvote.setBackgroundResource(R.drawable.baseline_delete_outline_27);
            upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_delete_outline_20));
            String word=String.valueOf(votes);
            vote_count.setText(word);
        }


        show.setText(message);

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.equals(curr_user)) {
                    if (able()) {
                        setUpVote(identity);
                        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_24);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
                    } else {
                        setDownVote(identity);
                        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                    }
                    //show.setText("Button working");
                }
                else{
                    delete();
                }
            }
        });


//        GradientDrawable gradientDrawable=new GradientDrawable();
//        gradientDrawable.setStroke(1,getResources().getColor(R.color.black));
//        this.setBackground(gradientDrawable);

    }

    public void setUpVote(int where){
        voteAdd(where);
        votes++;
        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_24);
        vote_count.setText(String.valueOf(votes));
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/answer_vote.php";

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
                        if (!MyRes.equals(" Success")){
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
                    //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24);
                    upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                }
            }
        });

    }

    public void setDownVote(int where){
        voteDelete(where);
        votes--;
        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24);
        vote_count.setText(String.valueOf(votes));
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/answer_vote.php";

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
                        if (!MyRes.equals(" Success")){
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
                if(!user.equals(curr_user)) {
                    if (able()) {
                        setUpVote(identity);
                        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_24);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_20_blue));
                    } else {
                        setDownVote(identity);
                        //upvote.setBackgroundResource(R.drawable.baseline_thumb_up_off_alt_24);
                        upvote.setImageDrawable(getResources().getDrawable(R.drawable.baseline_thumb_up_off_alt_20_blue));
                    }
                }


            }
        });

    }

    public void voteAdd(int id){
        String[] vals={curr_user, String.valueOf(id)};
        myDB.doUpdate("insert into ANSWER_VOTES(USERNAME,ANS_ID) values(?,?)",vals);
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/answer_vote_add.php";

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
        String[] where={String.valueOf(id)};
        myDB.doUpdate("delete from ANSWER_VOTES where ANS_ID=?",where);

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

    //return true if user hasn't liked
    public Boolean able(){
        String[] wha={curr_user,String.valueOf(identity)};
        Cursor c=myDB.doQuery("select * from ANSWER_VOTES where USERNAME= ? and ANS_ID=?",wha);
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

    public void delete(){
        avotes_delete();
        ans_delete();
        ((ViewGroup)this.getParent()).removeView(this);
    }
    public void ans_delete(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/delete_answer.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("identity", String.valueOf(identity));
        //upvote.setText(votes);
        //System.out.println(who.build());
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
    public void avotes_delete(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/related_delete_A.php";

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

    public void setColor(int i){
        LinearLayout l=findViewById(R.id.to_be_set);
        l.setBackgroundColor(getResources().getColor(R.color.bigger_box));
        this.setBackgroundColor(getResources().getColor(R.color.greyer));

    }


}
