package com.example.truecall.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.truecall.Activities.MainActivity;
import com.example.truecall.R;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity mainActivity= (MainActivity) view.getContext();
        TextView tvName=view.findViewById(R.id.tvName);
        ImageView imgProfile=view.findViewById(R.id.imgProfile);
        tvName.setText(mainActivity.getName());
        Glide.with(view.getContext()).load(mainActivity.getImage()).into(imgProfile);

        btnLogout   =view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityOut = (MainActivity) v.getContext();
                mainActivityOut.logOut();
            }
        });
        return view;
    }
}