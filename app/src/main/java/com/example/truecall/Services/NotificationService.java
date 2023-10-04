package com.example.truecall.Services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.truecall.DAO.BlacklistDAO;
import com.example.truecall.Models.BlacklistModel;
import com.example.truecall.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends Service {
    private static final int FOREGROUND_NOTIFICATION_ID = 1;
    private static final int BAD_CALL_NOTIFICATION_ID = 2;
    private BroadcastReceiver callReceiver;
    private NotificationManager notificationManager;

    private BlacklistDAO blacklistDAO=new BlacklistDAO();
    private ArrayList<BlacklistModel> blacklist =new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();

        blacklistDAO.getBlacklist(blacklist);

        FirebaseFirestore.getInstance().collection("blacklist").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý khi có lỗi xảy ra
                    return;
                }
                if (value != null) {
                    blacklist.clear();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        BlacklistModel model = document.toObject(BlacklistModel.class);
                        blacklist.add(model);
                    }
                }
            }
        });

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(FOREGROUND_NOTIFICATION_ID, createForegroundNotification());


        registerCallReceiver();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callReceiver != null) {
            unregisterReceiver(callReceiver);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerCallReceiver() {
        callReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("TAG", "onCall: " + incomingNumber);
                Log.d("TAG", "onState: " + state);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    boolean isScamCall = checkIfScamCall(incomingNumber);

                    if (isScamCall) {

                        showBadCallNotification(incomingNumber,  "Thực hiện cuộc gọi với mục đích xấu!");
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(callReceiver, filter);
    }

    private boolean checkIfScamCall(String phoneNumber) {

        for (BlacklistModel model : blacklist) {
            if (model.getPhone().equals(phoneNumber)) {

                return true;

            }
        }
        return false;

    }

    private void showBadCallNotification(String title, String message) {
        String channelId = "bad_call_channel";
        String channelName = "Bad Call Channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "bad_call_channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.RED)
                .setAutoCancel(true);

        notificationManager.notify(BAD_CALL_NOTIFICATION_ID, builder.build());
    }
     
    private Notification createForegroundNotification() {
        String channelId = "foreground_channel";
        String channelName = "Foreground Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "foreground_channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("TrueCall")
                .setContentText("Chúng tôi vẫn đang hoạt động để bảo vệ bạn trước những cuộc gọi xấu!")
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }
}

