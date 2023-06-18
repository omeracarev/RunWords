package org.newest.runwords;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CreateNotifications extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createFirstNotificationChannel();

    }

    private void createFirstNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Utils.FIRST_CHANNEL_ID,Utils.FIRST_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Utils.FIRST_CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
