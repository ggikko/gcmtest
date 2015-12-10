package ggikko.me.ggikkogcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by ggikko on 2015. 12. 2..
 */

public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    //디바이스에 받은 메세지를 내용을 정의하는 클래슨
    private static final String TAG = "GcmListenerService";
    private static final int REQUEST_CODE = 0;
    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String title = data.getString("title");
        String message = data.getString("message");

        try {
            String getTitle = URLDecoder.decode(title, "UTF-8");
            String getMessage = URLDecoder.decode(message, "UTF-8");
            sendNotification(getTitle, getMessage);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Bitmap
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.mipmap.logotest);
        Bitmap picture2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ggikko);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.opened4)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(notificationBuilder);
        style.bigLargeIcon(picture)
                .bigPicture(picture2)
                .setBigContentTitle("Byit vs Switcher")
                .setSummaryText("11 : 0 Cold game Byit Win!!");

        notificationBuilder.setStyle(style);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
