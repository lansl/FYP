package com.example.lzyang.fyptest.Functions;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import com.example.lzyang.fyptest.MainActivity;
import com.example.lzyang.fyptest.RequestActivity;
import com.example.lzyang.fyptest.R;
import com.example.lzyang.fyptest.RequestActivity;

/**
 * Created by Lz-Yang on 18/11/2017.
 */

public class NotificationGenerator {

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 1;

    private static Vibrator vibrator;
    private static Ringtone ringtone;

    public static void openEmergencyEvent(Context context, String emergency_title, String emergency_description) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(context,uri);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, RequestActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle(emergency_title);
        builder.setContentText(emergency_description);

        notificationManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY,builder.build());
        vibrator.vibrate(500);
        ringtone.play();
    }
}
