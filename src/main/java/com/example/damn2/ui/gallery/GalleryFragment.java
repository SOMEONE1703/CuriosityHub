package com.example.damn2.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.damn2.MainActivity;
import com.example.damn2.R;
import com.example.damn2.databinding.FragmentGalleryBinding;
import com.example.damn2.ui.custom;
import com.example.damn2.ui.log_in;
import com.example.damn2.ui.my_question;
import com.example.damn2.ui.question_layout;
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

public class GalleryFragment extends Fragment {

    ArrayList<question_layout> my_questions=new ArrayList<>();
    String username= log_in.Username;
    private FragmentGalleryBinding binding;

    int blue= R.color.bluey;
    int white=R.color.white;
    int grey=R.color.bigger_box;
    int width;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        start();
        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void start(){
        //System.out.println("I am not sure what to do from here");
        LinearLayout one=new LinearLayout(getActivity());
        one.setId(View.generateViewId());
        one.setOrientation(LinearLayout.VERTICAL);
        ScrollView they= (ScrollView) binding.something;
        they.removeAllViews();
        they.addView(one);


        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/my_question_get.php";

        HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        who.addQueryParameter("username",username);
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

                final String responseData = response.body().string();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        my_questions.clear();
                        collect(responseData);
                        //one.removeAllViews();

                        //System.out.println("collect completed successfully");

                        int i=7;
                        int d_green=0;
                        int d_blue=172;
                        int d_red=0;
                        for (question_layout trial : my_questions) {
                            ViewGroup parent = (ViewGroup) trial.getParent();
                            if (parent != null) {
                                parent.removeView(trial);
                            }
                            d_green+=i;
                            d_blue+=i;
                            d_red+=i;
                            if (d_blue>=240){
                                i*=-1;
                            }
                            if(d_blue<=172){
                                i*=-1;
                            }
                            //System.out.println(d_red+" "+d_green+" "+d_blue);
                            int color=android.graphics.Color.rgb(d_red,d_green,d_blue);
                            trial.setColor(getResources().getColor(R.color.trans));
                            LinearLayout divider = new LinearLayout(getContext());
                            LinearLayout.LayoutParams div= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,15);
                            divider.setLayoutParams(div);
                            divider.setBackgroundColor(getResources().getColor(R.color.trans));
                            one.addView(divider);
                            one.addView(trial);
                            grey=d_red;
                            blue=d_blue;
                            white=d_green;
                        }
                        int color=android.graphics.Color.rgb(grey,white,blue);
                        //one.setBackgroundColor(color);
                    }
                });

                //System.out.println("what in the actual fuck");
            }
        });


    }

    public void collect(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String question = item.getString("QUESTION");
                String brand = item.getString("VOTE");
                String user=item.getString("USERNAME");
                String id = item.getString("POST_ID");
                int identity = Integer.parseInt(id);
                int count = Integer.parseInt(brand);
                question_layout what = new question_layout(getActivity(),question,count,identity,user,username,width);
                my_questions.add(what);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}