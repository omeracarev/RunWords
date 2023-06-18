package org.newest.runwords.Services;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.newest.runwords.DatabaseHelper;
import org.newest.runwords.R;
import org.newest.runwords.WinRunny;
import org.newest.runwords.checkUsers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GetNewsActionService extends BroadcastReceiver {
    DatabaseHelper databaseHelper;
    String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";

    public GetNewsActionService(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        databaseHelper = new DatabaseHelper(context);

        boolean isAppInForeground = isAppInForeground(context);
        if (isAppInForeground) {
            if (!databaseHelper.getCheckUsers().isEmpty()){
               updateStatus(context,databaseHelper.getCheckUsers(),"online");
            }

        } else {
            if (!databaseHelper.getCheckUsers().isEmpty()){
                updateStatus(context,databaseHelper.getCheckUsers(),"offline");
            }
        }

        createNewUser(databaseHelper,context,databaseHelper.getCheckUsers());


    }

    private String generateRandomUsername() {
        // Kullanıcı adını rastgele bir şekilde oluşturun (Örnek amaçlı basit bir şekilde sadece rakamlardan oluşturuyoruz)
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return "Runny"+sb.toString();
    }

    private void createNewUser(DatabaseHelper databaseHelper,Context context,List<checkUsers> checkUsers ){
        if (checkUsers.isEmpty()){
            String data = generateRandomUsername();
            checkUsers checkUsers1 = new checkUsers(-1,data);
            RequestQueue requestQueue = Volley.newRequestQueue(context);



                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response = response.trim();
                                if (!response.equalsIgnoreCase("no")){
                                    if (databaseHelper.AddUser(checkUsers1)){

                                    }
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Post verilerini tanımlama
                        Map<String, String> params = new HashMap<>();
                        params.put("username", data);
                        return params;
                    }

                    @Nullable
                    @Override
                    public Response.ErrorListener getErrorListener() {

                        return super.getErrorListener();
                    }
                };

                requestQueue.add(request);

        }
    }

    private boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.processName.equals(context.getPackageName()) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }


        public void updateStatus(Context context,List<checkUsers> checkUsers,String status){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String data = databaseHelper.getCheckUsers().get(0).getUsername();


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post verilerini tanımlama
                Map<String, String> params = new HashMap<>();
                params.put("userstatus",status);
                params.put("usernamex",data);
                return params;
            }

            @Nullable
            @Override
            public Response.ErrorListener getErrorListener() {

                return super.getErrorListener();
            }
        };

        requestQueue.add(request);
    }

    public void createFirstNotification(Context mContext, String title, String text,String sumText) {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_001");
        Intent ii = new Intent(mContext.getApplicationContext(), WinRunny.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(text);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText(sumText);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_runwords);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Alıstırmalar",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }

}
