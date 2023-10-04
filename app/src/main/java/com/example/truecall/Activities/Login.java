package com.example.truecall.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.truecall.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        readLogin();
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);
        ImageView btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                googleLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> googleLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data=result.getData();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                String email=account.getEmail();
                String name= account.getDisplayName();
                String id=account.getId();
                String imgGoogle= String.valueOf(account.getPhotoUrl());
                Log.d(">>>>>TAG","onActivityResult Email: "+email);
                Log.d(">>>>>TAG","onActivityResult Name: "+name);
                writeLogin(id,name,email,imgGoogle);
                Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                Intent homeIntent=new Intent(Login.this,MainActivity.class);
                startActivity(homeIntent);
                finish();

            }catch (Exception e){
                Log.d(">>>>>TAG","onActivityResult ERROR: "+e.getMessage());
                Toast.makeText(Login.this,"Đăng nhập không thành công",Toast.LENGTH_LONG).show();
            }
        }
    });
    private void writeLogin(String id,String name,String email,String img){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("isLoggedIn",true);
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.putString("image", img);
        editor.commit();
    }

    private void readLogin(){
        SharedPreferences preferences= getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Boolean isLoggedIn = preferences.getBoolean("isLoggedIn",false);
        if(isLoggedIn){
            Intent homeIntent=new Intent(Login.this,MainActivity.class);
            startActivity(homeIntent);
            finish();

        }
    }
}