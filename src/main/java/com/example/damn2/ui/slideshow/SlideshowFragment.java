package com.example.damn2.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.damn2.MainActivity;
import com.example.damn2.MainActivity2;
import com.example.damn2.databinding.FragmentSlideshowBinding;
import com.example.damn2.ui.DatabaseHelper;
import com.example.damn2.ui.log_in;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlideshowFragment extends Fragment {
    String username;
    DatabaseHelper myDB=MainActivity.myDB;
    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        username= log_in.Username;
        Button log=binding.button3;
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp(view);
            }
        });



        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void trunk(){
        myDB.doUpdate("delete from LOG");
    }

    public void temp(View t){
        trunk();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //requireActivity().finish();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}