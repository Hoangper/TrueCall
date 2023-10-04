package com.example.truecall.DAO;

import androidx.annotation.NonNull;

import com.example.truecall.Models.BlacklistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlacklistDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void addPhoneNumberToBlacklist(String phone, String name, String note) {
        Map<String, Object> item = new HashMap<>();
        item.put("phone", phone);
        item.put("name", name);
        item.put("note", note);

        db.collection("blacklist")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Xử lý thành công khi thêm mục mới vào Firestore
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi thất bại trong việc thêm mục mới vào Firestore
                    }
                });
    }

    public void getBlacklist(List<BlacklistModel> blacklist) {
        db.collection("blacklist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String phone = document.getString("phone");
                                String name = document.getString("name");
                                String note = document.getString("note");
                                BlacklistModel item = new BlacklistModel(phone, name, note);
                                blacklist.add(item);
                            }

                        } else {

                        }
                    }
                });
    }






}
