package com.example.truecall.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.truecall.DAO.BlacklistDAO;
import com.example.truecall.R;

public class ReportFragment extends Fragment {

    private BlacklistDAO blacklistDAO =new BlacklistDAO();
    EditText edtPhone;
    EditText edtName;
    EditText edtNote;
    Button btnReport;
    public ReportFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_report, container, false);
         edtPhone=view.findViewById(R.id.edtPhone);
         edtName=view.findViewById(R.id.edtName);
         edtNote=view.findViewById(R.id.edtNote);
         btnReport=view.findViewById(R.id.btnReport);




        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=edtPhone.getText().toString();
                String name=edtName.getText().toString();
                String note=edtNote.getText().toString();
                if(phone.equals("")){
                    Toast.makeText(getContext(),"Vui lòng nhập 1 số điện thoại",Toast.LENGTH_SHORT).show();
                } else {
                    commitReport(phone,name,note);
                }

            }
        });
        return view;
    }

    private void commitReport(String phone, String name,String note){
        if(name.equals("")){
            name="null";
        }
        if(note.equals("")){
            note="null";
        }
        blacklistDAO.addPhoneNumberToBlacklist(phone,name,note);
        Toast.makeText(getContext(),"Báo cáo thành công",Toast.LENGTH_SHORT).show();
    }

}