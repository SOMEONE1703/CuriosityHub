package com.example.damn2.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.damn2.MainActivity;
import com.example.damn2.R;
import com.example.damn2.databinding.FragmentHomeBinding;
import com.example.damn2.ui.custom;
import com.example.damn2.ui.log_in;
import com.example.damn2.ui.trial;

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
// create thread that searches for next big thing It's gonna set onclick listener which will
//uithread may be able to do it itself
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Boolean chill=false;
    String Username= log_in.Username;
    int blue= R.color.blue2;
    int white=R.color.white;
    int grey=R.color.bigger_box;
    int width;
    ArrayList<trial> questions = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // HomeViewModel homeViewModel =
          //      new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //System.out.println("I am not sure what to do from here");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        LinearLayout one=new LinearLayout(getActivity());
        one.setId(View.generateViewId());
        one.setOrientation(LinearLayout.VERTICAL);
        ScrollView they= (ScrollView) binding.scroll;
        //they.removeAllViews();

        they.addView(one);


        //System.out.println("we got this");
        OkHttpClient client = new OkHttpClient();

        String url = "https://lamp.ms.wits.ac.za/home/s2553616/question_get.php";

        //HttpUrl.Builder who=HttpUrl.parse(url).newBuilder();
        //who.addQueryParameter("brand","Toyota");
        Request request = new Request.Builder()
                .url(url)
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

                final String responseData = response.body().string();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questions.clear();
                        collect(responseData);
                        one.removeAllViews();

                        //System.out.println("collect completed successfully");
                        int i=5;
                        int d_green=74;
                        int d_blue=172;
                        int d_red=2;

                        for (trial trial : questions) {
                            int min= (int) (Math.random() * (7));
                            ViewGroup parent = (ViewGroup) trial.getParent();
                            if (parent != null) {
                                parent.removeView(trial);
                            }
                            d_green+=i;
                            d_blue+=i;
                            d_red+=i;
                            if (d_blue>=249){
                                i*=-1;
                            }
                            if(d_blue<=172){
                                i*=-1;
                            }
                            //System.out.println(d_red+" "+d_green+" "+d_blue);
                            int color=android.graphics.Color.rgb(d_red,d_green,d_blue);
                            trial.setColor(color);

                            LinearLayout divider = new LinearLayout(getContext());
                            LinearLayout.LayoutParams div= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,15);
                            divider.setLayoutParams(div);

                            divider.setBackgroundColor(getResources().getColor(R.color.text_grey));
                            one.addView(divider);
                            one.addView(trial);
                            //i++;


                        }



                    }
                    //System.out.println("odd, very very odd");
                });

                //System.out.println("what in the actual fuck");
            }

        });

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    
    public void collect(String json){
        max_id=0;
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String question = item.getString("QUESTION");
                String brand = item.getString("VOTE");
                String user = item.getString("USERNAME");
                String id = item.getString("POST_ID");
                int identity = Integer.parseInt(id);
                if (identity>max_id){
                    max_id=identity;
                }
                int count = Integer.parseInt(brand);
                trial what = new trial(getActivity(),question,count,identity,user,Username,width);
                questions.add(what);
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