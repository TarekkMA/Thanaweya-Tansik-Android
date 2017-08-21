package com.tmaprojects.tansik;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tmaprojects.tansik.networking.FirebaseManager;
import com.tmaprojects.tansik.views.MainActivity;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import timber.log.Timber;

/**
 * Created by tarekkma on 8/21/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_MSG_TYPE = "msg_type";
    private static final String KEY_MSG_TYPE_UPDATE_YEAR = "update_year";
    private static final String KEY_IDS_TO_UPDATE = "ids_to_update";
    private static final String KEY_MSG_TYPE_UPDATE_APP = "update_app";
    private static final String KEY_NEW_VERSION_CODE = "new_version";

    
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String,String> data = remoteMessage.getData();
            Timber.d("Message data payload: " + data);

            String title = data.get(KEY_TITLE);
            String body = data.get(KEY_BODY);


            String msgType = data.get(KEY_MSG_TYPE);

            if(msgType.equals(KEY_MSG_TYPE_UPDATE_YEAR)){
                String[] ids = data.get(KEY_IDS_TO_UPDATE).split(",");
                for(String id:ids)FirebaseManager.startDownloadingTnasikYear(id);
                sendNotifaction(title,body,null);
            }else if (msgType.equals(KEY_MSG_TYPE_UPDATE_APP)){
                int newVersion = Integer.parseInt(data.get(KEY_NEW_VERSION_CODE));
                if(newVersion > getCurrentVersion()){
                    final String appPackageName = getPackageName();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    sendNotifaction(title,body,intent);
                }
            }else{
                sendNotifaction(title,body,null);
            }

        }

    }

    private int getCurrentVersion(){
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void sendNotifaction(String title,String body,@Nullable Intent intent){
        if(intent==null)intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        NotificationManager notifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notifyMgr.notify(new Random().nextInt(100),notification);
    }

}
