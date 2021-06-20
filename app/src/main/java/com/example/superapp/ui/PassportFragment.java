package com.example.superapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.superapp.R;

public class PassportFragment extends Fragment {

    private int id;
    private String companyName;
    private String text;
    private ImageView image;
    private TextView tw_name;
    private TextView tw_text;
    private int resourceId;
    private ClickCallback clickCallback;


    public interface ClickCallback {
        void clickPerformed(int fragmentId);
    }

    public PassportFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            companyName = arguments.getString("companyName");
            text = arguments.getString("text");
            id = arguments.getInt("id");
            resourceId = arguments.getInt("resourceId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        image = view.findViewById(R.id.imageView);
        tw_name = view.findViewById(R.id.tw_name);
        tw_text = view.findViewById(R.id.tw_text);
        tw_text.setText(text);
        tw_name.setText(companyName);
        image.setImageResource(resourceId);
        image.setOnClickListener((v) -> {
            if (clickCallback != null) {
                clickCallback.clickPerformed(id);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        clickCallback = (ClickCallback) context;
    }

    public static PassportFragment newInstance(int id, String companyName, String text, int resourceId) {
        Bundle bundle = new Bundle();
        bundle.putString("companyName", companyName);
        bundle.putString("text", text);
        bundle.putInt("id", id);
        bundle.putInt("resourceId", resourceId);
        PassportFragment passportFragment = new PassportFragment();
        passportFragment.setArguments(bundle);
        return passportFragment;
    }
}