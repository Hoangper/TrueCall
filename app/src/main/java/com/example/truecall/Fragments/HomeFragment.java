package com.example.truecall.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.truecall.DAO.BlacklistDAO;
import com.example.truecall.Models.BlacklistModel;
import com.example.truecall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    TextView tvCountPhoneNum;





    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        tvCountPhoneNum=view.findViewById(R.id.tvCountPhoneNum);
        FirebaseFirestore.getInstance().collection("blacklist").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int documentCount = queryDocumentSnapshots.size();
                        tvCountPhoneNum.setText("Hiện tại bạn được bảo vệ khỏi "+ documentCount +" nguời gọi spam.\n Chúng tôi sẽ tiếp tục cập nhật và nhận dạng mọi cuộc gọi.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });


        return view;
    }
}