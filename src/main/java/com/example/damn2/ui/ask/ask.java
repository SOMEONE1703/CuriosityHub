package com.example.damn2.ui.ask;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.damn2.MainActivity;
import com.example.damn2.R;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.damn2.databinding.*;
import com.example.damn2.ui.log_in;
import com.example.damn2.ui.slideshow.SlideshowViewModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ask extends Fragment {
    String username;
    View why;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ask,
                container, false);
        why=root;
        username= log_in.Username;
        Button the= (Button) root.findViewById(R.id.ask);
        the.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post(root);
            }
        });

        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void post(View t){

        EditText she= (EditText) why.findViewById(R.id.questionIn);
        String question = she.getText().toString();
        TextView him = (TextView) why.findViewById(R.id.textView3);
        if(question.equals("")){
            //him.setText("Enter valid question");
            Toast.makeText(getContext(), "Enter valid question", Toast.LENGTH_LONG).show();
            return;
        }
        if (question.length()>=500){
            she.setText(question);
            //him.setText("Question too long");
            Toast.makeText(getContext(), "Question must be less than 500 characters", Toast.LENGTH_LONG).show();
        }
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_post.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("username",username);
        who.addQueryParameter("question",question);
        Request request = new Request.Builder()
                .url(who.build())
                .build();
        //System.out.println(who.build());
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
                        if (!MyRes.equals("Success")){
                            System.out.println("question post unsuccessful");
                            Toast.makeText(getContext(), "Question post unsuccessful, please try again later", Toast.LENGTH_LONG).show();
                        }
                        else{
                            //TextView him=(TextView) why.findViewById(R.id.textView3);
                            //him.setText("Question posted successfully");
                            Toast.makeText(getContext(), "Question posted successfully", Toast.LENGTH_LONG).show();
                            EditText she=(EditText) why.findViewById(R.id.questionIn);
                            she.setText("");
                            //System.out.println("Just so it doesn't whine");
                        }
                    }
                });

            }
        });

    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}