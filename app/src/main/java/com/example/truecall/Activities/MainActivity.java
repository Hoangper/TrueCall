package com.example.truecall.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.truecall.Fragments.HomeFragment;
import com.example.truecall.Fragments.NewsFragment;
import com.example.truecall.Fragments.ProfileFragment;
import com.example.truecall.Fragments.ReportFragment;
import com.example.truecall.R;
import com.example.truecall.Services.NotificationService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG},
                    121);
        } else {
            Intent serviceIntent = new Intent(this, NotificationService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
        replaceFragment(new HomeFragment());


        BottomNavigationView bnv=findViewById(R.id.bottom_nav);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.nav_report:
                        replaceFragment(new ReportFragment());
                        break;

                    case R.id.nav_news:
                        replaceFragment(new NewsFragment());
                        break;
                    case R.id.nav_profile:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return true;
            }
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    public void logOut(){

        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        if(preferences.getString("type","null").equals("google")){
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
            googleSignInClient.signOut();
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("id");
        editor.remove("type");
        editor.remove("name");
        editor.remove("email");
        editor.remove("image");
        editor.apply();
        Intent loginIntent=new Intent(MainActivity.this,Login.class);
        startActivity(loginIntent);
        finish();
    }

    public String getName(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        String name=preferences.getString("name","Võ Hoàng Minh Thư");
        return name;
    }
    public String getIdUser(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        String id=preferences.getString("id","minhthu");
        return id;
    }
    public Uri getImage(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Uri linkImg= Uri.parse(preferences.getString("image","null"));
        return linkImg;
    }
}