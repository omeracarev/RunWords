package org.newest.runwords;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.plattysoft.leonids.ParticleSystem;
import org.newest.runwords.Services.GetNewsActionService;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView mainengword,
            chose1,
            chose2,
            chose3,
            chose4,
            corscore,
            wrongscore,
            runnyscore,
            choosertext,
            helpidletext;
    private ArrayList<TextView> numbers = new ArrayList<>();

    private static final String BASTEX = "bastex";
    private static final String WRONGC = "wrongc";
    private static final String WRONG = "wrong";

    private static final String YYMMDD = "yyyy-MM-dd";
    private static final String RUNNYKAZAN = "runnykazan";

    private static final String DAVETGONDER = "Davet\nGönder";

    private PopupWindow serPopup = null;

    private Random random;

   private String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";
    private PopupWindow curpopup = null;

    private int correct = 0,
            falses=0,
            runnies=2,
            idlehelp=0;

    private ImageView leftar,
            rightar;

    private RewardedAd mRewardedAd;
    private int glowidth;
    private int gloheight;

    private int bundleIndex = 2;

    private MediaPlayer mediaPlayer;

    private DatabaseHelper databaseHelper;

    public static WeakReference<MainActivity> weakActivity;

    public static MainActivity getmInstanceActivity() {
        return weakActivity.get();
    }

    private String[] idledialogs = new String[]{"olamazdı zaten...","arada bıraktı ama değil...","çağrışım yaptı fakat yanlış...","kafa karıştırıyor, eleyelim..."};

    private ImageView playsound,
            mainrunny,
            helprunn,
            correctim,
            wrongim,
            runnyim;

    private Bundle extras;

    private boolean checkInfo(){
        int dpi = getResources().getDisplayMetrics().densityDpi;
       if (dpi<440){
           return false;
       } else {
           return true;
       }
    }
    @SuppressLint("MissingInflatedId")

    private void rewardedAdset(DatabaseHelper dbHelper){

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-2627695336411451/5305408746",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                List<Currents> currents = dbHelper.getCurrents();
                                runnies = Integer.parseInt(currents.get(0).getRuncount());
                                runnyscore.setText("x"+runnies+"");
                                mRewardedAd = null;
                            }
                        });
                    }
                });


    }

    private void opensuccPop(Context context){
        int a = correct%10;
        if (correct!=0 && a==0){
            String corcounte = correct+"";
            successPopup(context,corcounte);
        }
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


    private void menuPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,MainActivity.this.getTheme()));
        View layout = View.inflate(context,R.layout.menupopup,null);

        ImageView runimsta          =layout.findViewById(R.id.runimsta);
        TextView runimhaktext       =layout.findViewById(R.id.runimhaktext);
        TextView runimcounter       =layout.findViewById(R.id.runimcounter);
        TextView menupop1           =layout.findViewById(R.id.menupop1);
        TextView menupop2           =layout.findViewById(R.id.menupop2);
        TextView menupop3           =layout.findViewById(R.id.menupop3);
        TextView menupop4           =layout.findViewById(R.id.menupop4);
        TextView menupop5           =layout.findViewById(R.id.menupop5);
        TextView menupop6           =layout.findViewById(R.id.menupop6);
        TextView staticgor          =layout.findViewById(R.id.staticgor);
        TextView dismisspopupmenu   =layout.findViewById(R.id.dismisspopupmenu);

        runimhaktext .setTag(R.string.bastex,BASTEX);
        runimcounter.setTag(R.string.bastex,BASTEX);
        menupop1.setTag(R.string.bastex,BASTEX);
        menupop2.setTag(R.string.bastex,BASTEX);
        menupop3.setTag(R.string.bastex,BASTEX);
        menupop4.setTag(R.string.bastex,BASTEX);
        menupop5.setTag(R.string.bastex,BASTEX);
        menupop6.setTag(R.string.bastex,BASTEX);

        staticgor.setTag(R.string.bastex,BASTEX);
        dismisspopupmenu.setTag(R.string.bastex,BASTEX);

        ViewEditor(runimhaktext,20,20);
        ViewEditor(runimcounter,25,25);
        ViewEditor(menupop1,16,16);
        ViewEditor(menupop2,16,16);
        ViewEditor(menupop3,16,16);
        ViewEditor(menupop4,16,16);
        ViewEditor(menupop5,16,16);
        ViewEditor(menupop6,16,16);
        ViewEditor(staticgor    ,16,16);

        ViewEditor(dismisspopupmenu,15,15);

        getPassingClass(menupop1);
        getPassingClass(menupop2);
        getPassingClass(menupop3);
        getPassingClass(menupop4);
        getPassingClass(menupop5);
        getPassingClass(menupop6);

        List<Ads> adsList = databaseHelper.getAds();
        if (!adsList.isEmpty()){
            if (adsList.get(0).getAd_type().trim().equalsIgnoreCase(RUNNYKAZAN)){

                if (Integer.parseInt(adsList.get(0).getAd_quota())<=0 && !adsList.get(0).getAd_date().trim().equalsIgnoreCase(new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()))){
                    Ads ads = new Ads(-1,"1",new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()),adsList.get(0).getAd_type());
                    if (!databaseHelper.deleteAds(adsList.get(0))){
                        databaseHelper.AddAds(ads);
                    }
                } else if (Integer.parseInt(adsList.get(0).getAd_quota())>0 && adsList.get(0).getAd_date().trim().equalsIgnoreCase(new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()))){
                    createFirstNotification(context, "Bugünün Alıştırmasını Tamamla!", "Buraya dokun ve kelimeleri eşle!", "Runny Kazan!");
                }


            }
        } else {
            Ads ads = new Ads(-1,"1",new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()),RUNNYKAZAN);
            databaseHelper.AddAds(ads);
        }


        staticgor.setOnClickListener(v -> {
            popUp.dismiss();
            Intent intent = new Intent(v.getContext(),staticsfornoti.class);
            startActivity(intent);
            finish();
        });

        if (!adsList.get(0).getAd_quota().equalsIgnoreCase("0")){
            runimhaktext.setText("WinRunny Hakkını Kullan!");
            runimcounter.setText("Challenge başlat!");
            yoyoclas(Techniques.Flash,1000,3,runimcounter);
        } else {
            runimhaktext.setText("WinRunny Hakkın Yok!");

            int onedaysn = 86400;

            Calendar calendar = Calendar.getInstance();
            int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);

            int calculater = (hour24hrs*3600)+(minutes*60)+seconds;




            CountDownTimer countDownTimer = new CountDownTimer(((onedaysn-calculater)*1000),1000) {
                @Override
                public void onTick(long l) {
                    int saat = (int) ((l/1000)/3600);
                    int saattenkalan = (int) ((l/1000)%3600);
                    int dakika = saattenkalan/60;
                    int saniye = saattenkalan%60;
                    runimcounter.setText("Kalan Süre : "+saat+" sa "+dakika+" dk "+saniye+" sn" );
                }

                @Override
                public void onFinish() {
                    Ads ads = new Ads(-1,"1",new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()),adsList.get(0).getAd_type());
                    if (!databaseHelper.deleteAds(adsList.get(0))){
                        databaseHelper.AddAds(ads);
                    }
                    runimhaktext.setText("WinRunny Hakkını Kullan!");
                    runimcounter.setText("Buraya dokunarak challenge başlat!");
                }
            };

            countDownTimer.start();
        }

        runimsta.setOnClickListener(view -> {

            if (!adsList.get(0).getAd_quota().equalsIgnoreCase("0")){
                Ads ads = new Ads(-1,(Integer.parseInt(adsList.get(0).getAd_quota())-1)+"",adsList.get(0).getAd_date(),adsList.get(0).getAd_type());
                if (!databaseHelper.deleteAds(adsList.get(0))){
                    if (databaseHelper.AddAds(ads)){
                        popUp.dismiss();
                        Intent intent = new Intent(view.getContext(),WinRunny.class);
                        startActivity(intent);
                    }
                }
            } else {
                Toast.makeText(context, "Süre Bitince Tekrar Uğra!", Toast.LENGTH_SHORT).show();
                yoyoclas(Techniques.Wobble,1000,1,runimsta);
                yoyoclas(Techniques.Bounce,1000,1,runimcounter);
                yoyoclas(Techniques.Shake,1000,1,runimhaktext);
            }
        });

        runimcounter.setOnClickListener(view -> {

            if (!adsList.get(0).getAd_quota().equalsIgnoreCase("0")){
                Ads ads = new Ads(-1,(Integer.parseInt(adsList.get(0).getAd_quota())-1)+"",adsList.get(0).getAd_date(),adsList.get(0).getAd_type());
                if (!databaseHelper.deleteAds(adsList.get(0))){
                    if (databaseHelper.AddAds(ads)){
                        popUp.dismiss();
                        Intent intent = new Intent(view.getContext(),WinRunny.class);
                        startActivity(intent);

                    }
                }
            } else {
                Toast.makeText(context, "Süre Bitince Tekrar Uğra!", Toast.LENGTH_SHORT).show();
                yoyoclas(Techniques.Wobble,1000,1,runimsta);
                yoyoclas(Techniques.Bounce,1000,1,runimcounter);
                yoyoclas(Techniques.Shake,1000,1,runimhaktext);
            }
        });

        popUp.setOutsideTouchable(false);
        popUp.setFocusable(false);
        popUp.setContentView(layout);

        try{
            popUp.update(0, 0, glowidth-glowidth/8, gloheight/2+gloheight/3);
            popUp.showAtLocation(layout, Gravity.CENTER,0,0);
            curpopup = popUp;
        } catch (Exception e){
            Toast.makeText(context, "Popup Açılamadı...", Toast.LENGTH_SHORT).show();
        }

        darkenBackground(0.3f);

        popUp.setOnDismissListener(() -> {
            darkenBackground(1f);
            curpopup = null;
        });

        dismisspopupmenu.setOnClickListener(v -> popUp.dismiss());

    }

    private void successPopup(Context context,String corcounte){

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_002");
        Intent ii = new Intent(context.getApplicationContext(), staticsfornoti.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("İlerlemeni Görüntüle!");
        bigText.setBigContentTitle("İstatistikleri Gör!");
        bigText.setSummaryText("İstatistikler");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_runwords);
        mBuilder.setContentTitle("İlerlemeni Görüntüle!");
        mBuilder.setContentText("Buraya dokunarak nasıl ilerlediğini görebilirsin!");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);


        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id2";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "İstatistikler",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

        PopupWindow popUp = new PopupWindow(context);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,MainActivity.this.getTheme()));
        View layout = View.inflate(context,R.layout.leveluplay,null);

        TextView devamlup = layout.findViewById(R.id.devamlup);
        TextView helpidletextlup = layout.findViewById(R.id.helpidletextlup);
        TextView hedeftextlup = layout.findViewById(R.id.hedeftextlup);
        TextView nexthedeflup = layout.findViewById(R.id.nexthedeflup);
        LinearLayout leveluppoplay = layout.findViewById(R.id.leveluppoplay);


        devamlup.setVisibility(View.GONE);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(devamlup.getLayoutParams());
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#C51162")));
        leveluppoplay.addView(progressBar,3);


        devamlup.setTag(R.string.bastex,BASTEX);
        helpidletextlup.setTag(R.string.bastex,BASTEX);
        hedeftextlup.setTag(R.string.bastex,BASTEX);
        nexthedeflup.setTag(R.string.bastex,BASTEX);

        ViewEditor(devamlup,13,13);
        ViewEditor(helpidletextlup,12,12);
        ViewEditor(hedeftextlup,15,15);
        ViewEditor(nexthedeflup,20,20);

        List<Currents> currents = databaseHelper.getCurrents();
        Currents currents1 = new Currents(-1,currents.get(0).getCorcount(),0+"",currents.get(0).getRuncount());
        if (!databaseHelper.deleteCurrents(currents.get(0))){
            if (databaseHelper.AddCurrents(currents1)){
                falses=0;
                wrongscore.setText("x"+falses+"");
            }
        }


        if (Integer.parseInt(corcounte)==40){
            hedeftextlup.setText(corcounte+" Doğru Hedefi Tamamlandı!");
            nexthedeflup.setText("Sıradaki Hedef : 3 Runny Kazan!");
        } else if (Integer.parseInt(corcounte)==50){
            hedeftextlup.setText("Süper! 3 Runny Kazandın!");
            nexthedeflup.setText("Sıradaki Hedef : Yanlış Yapmadan Devam!");
            Toast.makeText(context, "3 Runny Kazanıldı (-50 Doğru)", Toast.LENGTH_SHORT).show();
        } else {
            hedeftextlup.setText(corcounte+" Doğru Hedefi Tamamlandı!");
            nexthedeflup.setText("Sıradaki Hedef : "+(Integer.parseInt(corcounte)+10)+" Doğru");
        }





        NativeAdView nativeAdView = (NativeAdView) NativeAdView.inflate(context,R.layout.native_ad_layout,null);
        leveluppoplay.addView(nativeAdView,3);
        nativeAdView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,10));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nativeAdView.getLayoutParams();
        layoutParams.setMargins(10,10,10,10);

        NativeAdView[] finalNativeAdView = {nativeAdView};
        finalNativeAdView[0].setVisibility(View.INVISIBLE);
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-2627695336411451/2076378663")
                .forNativeAd(nativeAd -> {
                         try{
                             finalNativeAdView[0].setVisibility(View.VISIBLE);
                             TextView headlineView = finalNativeAdView[0].findViewById(R.id.ad_headline);
                             if (nativeAd.getHeadline()!=null){
                                 headlineView.setText(nativeAd.getHeadline());
                                 finalNativeAdView[0].setHeadlineView(headlineView);
                             } else {
                                 headlineView.setVisibility(View.INVISIBLE);
                             }

                             TextView adAdvertiser = finalNativeAdView[0].findViewById(R.id.ad_advertiser);
                             if (nativeAd.getAdvertiser()!=null){
                                 adAdvertiser.setText(nativeAd.getAdvertiser());
                                 finalNativeAdView[0].setAdvertiserView(adAdvertiser);
                             } else {
                                 adAdvertiser.setVisibility(View.INVISIBLE);
                             }
                             ImageView ad_app_icon = finalNativeAdView[0].findViewById(R.id.ad_app_icon);
                             if (nativeAd.getIcon()!=null){

                                 ad_app_icon.setImageDrawable(nativeAd.getIcon().getDrawable());
                                 finalNativeAdView[0].setIconView(ad_app_icon);
                             } else {
                                 ad_app_icon.setVisibility(View.INVISIBLE);
                             }

                             TextView advertisingbody = finalNativeAdView[0].findViewById(R.id.ad_body);
                             if (nativeAd.getBody()!=null){
                                 advertisingbody.setText(nativeAd.getBody());
                                 finalNativeAdView[0].setBodyView(advertisingbody);
                             } else {
                                 advertisingbody.setVisibility(View.INVISIBLE);
                             }

                             Button ad_action = finalNativeAdView[0].findViewById(R.id.ad_action);
                             if (nativeAd.getCallToAction()!=null){

                                 ad_action.setText(nativeAd.getCallToAction());
                                 finalNativeAdView[0].setCallToActionView(ad_action);
                             } else {
                                 ad_action.setVisibility(View.INVISIBLE);
                                 finalNativeAdView[0].setCallToActionView(finalNativeAdView[0]);
                             }

                             if (nativeAd.getImages()!=null){
                                 LinearLayout laypop = finalNativeAdView[0].findViewById(R.id.bgpoplay);
                                 laypop.setBackground(nativeAd.getImages().get(0).getDrawable());
                             }

                             finalNativeAdView[0].setNativeAd(nativeAd);

                         } catch (Exception e){
                             Toast.makeText(context, "Bağlantınızı Kontrol Edin...", Toast.LENGTH_SHORT).show();
                         }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }

                    @Override
                    public void onAdClicked() {
                        throw new UnsupportedOperationException();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.

                        .build())
                .build();

              adLoader.loadAd(new AdRequest.Builder().build());

        popUp.setOutsideTouchable(false);
        popUp.setFocusable(false);
        popUp.setContentView(layout);

        try{
            popUp.update(0, 0, glowidth-glowidth/8, gloheight/2+gloheight/3);
            popUp.showAtLocation(layout, Gravity.CENTER,0,0);
        } catch (Exception e){
            Toast.makeText(context, "Popup Açılamadı...", Toast.LENGTH_SHORT).show();
        }

        darkenBackground(0.3f);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
           devamlup.setVisibility(View.VISIBLE);
           leveluppoplay.removeView(progressBar);
        },5000);

        devamlup.setOnClickListener(view -> {
            yoyoclas(Techniques.Bounce,500,0,view);
            popUp.dismiss();
        });

        popUp.setOnDismissListener(() -> {
            darkenBackground(1f);
            finalNativeAdView[0].destroy();
        });
    }



    public void realtimeNotification(Context context){

        // AlarmManager objesini al
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

// Tetiklenecek zaman aralığını belirle
        long intervalMillis = 60 * 1000; // 60 saniye

// Broadcast Receiver için intent oluştur
        Intent intent = new Intent(context,GetNewsActionService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
// AlarmManager'a bildirimi ayarla
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getPassingClass(View view) {
        view.setOnClickListener(v -> {
            switch (v.getId()){
                case R.id.menupop1:
                    bundleIndex = 0;
                    break;
                case R.id.menupop2:
                    bundleIndex = 1;
                    break;
                case R.id.menupop3:
                    bundleIndex = 2;
                    break;
                case R.id.menupop4:
                    bundleIndex = 3;
                    break;
                case R.id.menupop5:
                    bundleIndex = 4;
                    break;
                case R.id.menupop6:
                    bundleIndex = 5;
                    break;

                default:
                    break;
            }
            Intent intent = new Intent(getApplicationContext(), PassinAds.class);
            intent.putExtra("index", bundleIndex);
            startActivity(intent);
            if (curpopup!=null){
                curpopup.dismiss();
            }
            finish();
        });
    }

    public void ViewHeight(View view,int a){
        view.getLayoutParams().height = gloheight/a;
    }
    public void refreshServerPopup(Context context,String which){
        if (serPopup!=null){
            serPopup.dismiss();
            Handler handler = new Handler();
            Runnable runnable = () -> createServerPopup(context,which);
            handler.postDelayed(runnable,1000);
        }

    }
    private void createServerPopup(Context context,String which){
        PopupWindow popUp = new PopupWindow(context);
        View layout = View.inflate(context,R.layout.server_card_layout,null);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,MainActivity.this.getTheme()));

        ArrayList<userData> testUsers = new ArrayList<>();

        TextView baslik = layout.findViewById(R.id.servertext);
        TextView kapat = layout.findViewById(R.id.servershut);
        RecyclerView recyclerView = layout.findViewById(R.id.serverlister);
        TextView runnyidtext = layout.findViewById(R.id.runnyidtext);
        TextView isteklerbut = layout.findViewById(R.id.isteklerbut);
        TextView odalarbut = layout.findViewById(R.id.odalarbut);

        runnyidtext.setText("Your RunnyID:\n"+databaseHelper.getCheckUsers().get(0).getUsername());

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    response = response.trim();

                    if (!response.equalsIgnoreCase("") && !response.equalsIgnoreCase("no")){
                        String[] allof = response.split(">");
                        String[] users = allof[0].split("<");
                        String[] waiters = allof[1].split("<");
                        String[] waiterfroms = allof[2].split("<");
                        String[] roomed = allof[3].split("<");

                        if (roomed[0].equalsIgnoreCase("no")){
                            odalarbut.setText("Odalar"+"("+0+")");
                        } else {
                            odalarbut.setText("Odalar"+"("+roomed.length+")");
                        }

                        int topwait = 0;

                        if (!waiters[0].equalsIgnoreCase("no")){
                            topwait+=waiters.length;
                        }

                        if (!waiterfroms[0].equalsIgnoreCase("no")){
                           topwait+=waiterfroms.length;
                        }

                        isteklerbut.setText("İstekler("+topwait+")");
                        final Boolean[] isClicked = {false,false};

                        //Butonlar arası geçişi kodla

                        isteklerbut.setOnClickListener(v -> {

                                if (!isClicked[0]){
                                    isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground4,getTheme()));
                                    odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                    isClicked[0] = true;
                                    isClicked[1] = false;


                                    testUsers.clear();

                                    for (int i=0; i<waiters.length; i++){
                                        if (!waiters[i].equalsIgnoreCase("no")){
                                            testUsers.add(new userData(waiters[i],"Davet\nBekleniyor"));
                                        }

                                    }

                                    for (int i=0; i<waiterfroms.length; i++){
                                        if (!waiterfroms[i].equalsIgnoreCase("no")){
                                            testUsers.add(new userData(waiterfroms[i],"Daveti\nKabul Et"));
                                        }

                                    }

                                    userAdapter adapter = new userAdapter(context,testUsers);
                                    recyclerView.setAdapter(adapter);

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);


                                } else {
                                    isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                    odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                    isClicked[0] = false;
                                    isClicked[1] = false;
                                    testUsers.clear();

                                    for (int i=0; i<users.length; i++){
                                        if (!databaseHelper.getCheckUsers().isEmpty()){
                                            if (!databaseHelper.getCheckUsers().get(0).getUsername().equalsIgnoreCase(users[i])){
                                                testUsers.add(new userData(users[i],DAVETGONDER));
                                                userAdapter adapter = new userAdapter(context,testUsers);
                                                recyclerView.setAdapter(adapter);

                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                                recyclerView.setLayoutManager(linearLayoutManager);
                                            }
                                        }

                                    }
                                }


                        });



                        odalarbut.setOnClickListener(v -> {
                            if (!isClicked[1]){
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground4,getTheme()));
                                isClicked[1] = true;
                                isClicked[0] = false;

                                testUsers.clear();

                                for (int i=0; i<roomed.length; i++){
                                    if (!roomed[i].equalsIgnoreCase("no")){
                                        testUsers.add(new userData(roomed[i],"Odaya\nKatıl"));
                                    }
                                }

                                userAdapter adapter = new userAdapter(context,testUsers);
                                recyclerView.setAdapter(adapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);


                            } else {
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                isClicked[1] = false;
                                isClicked[0] = false;
                                testUsers.clear();

                                for (int i=0; i<users.length; i++){
                                    if (!databaseHelper.getCheckUsers().isEmpty()){
                                        if (!databaseHelper.getCheckUsers().get(0).getUsername().equalsIgnoreCase(users[i])){
                                            testUsers.add(new userData(users[i],DAVETGONDER));
                                            userAdapter adapter = new userAdapter(context,testUsers);
                                            recyclerView.setAdapter(adapter);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                        }
                                    }

                                }
                            }
                        });

                        for (int i=0; i<users.length; i++){
                            if (!databaseHelper.getCheckUsers().isEmpty()){
                                if (!databaseHelper.getCheckUsers().get(0).getUsername().equalsIgnoreCase(users[i])){
                                    testUsers.add(new userData(users[i],DAVETGONDER));
                                    userAdapter adapter = new userAdapter(context,testUsers);
                                    recyclerView.setAdapter(adapter);

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                }
                            }

                        }

                        if (which.equalsIgnoreCase("istek")){
                            if (!isClicked[0]){
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground4,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                isClicked[0] = true;
                                isClicked[1] = false;

                                testUsers.clear();

                                for (int i=0; i<waiters.length; i++){
                                    if (!waiters[i].equalsIgnoreCase("no")){
                                        testUsers.add(new userData(waiters[i],"Davet\nBekleniyor"));
                                    }

                                }

                                for (int i=0; i<waiterfroms.length; i++){
                                    if (!waiterfroms[i].equalsIgnoreCase("no")){
                                        testUsers.add(new userData(waiterfroms[i],"Daveti\nKabul Et"));
                                    }

                                }

                                userAdapter adapter = new userAdapter(context,testUsers);
                                recyclerView.setAdapter(adapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);


                            } else {
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                isClicked[0] = false;
                                isClicked[1] = false;
                                testUsers.clear();

                                for (int i=0; i<users.length; i++){
                                    if (!databaseHelper.getCheckUsers().isEmpty()){
                                        if (!databaseHelper.getCheckUsers().get(0).getUsername().equalsIgnoreCase(users[i])){
                                            testUsers.add(new userData(users[i],DAVETGONDER));
                                            userAdapter adapter = new userAdapter(context,testUsers);
                                            recyclerView.setAdapter(adapter);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                        }
                                    }

                                }
                            }
                        }
                        else if (which.equalsIgnoreCase("oda")){
                            if (!isClicked[1]){
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground4,getTheme()));
                                isClicked[1] = true;
                                isClicked[0] = false;

                                testUsers.clear();

                                for (int i=0; i<roomed.length; i++){
                                    if (!roomed[i].equalsIgnoreCase("no")){
                                        testUsers.add(new userData(roomed[i],"Odaya\nKatıl"));
                                    }
                                }

                                userAdapter adapter = new userAdapter(context,testUsers);
                                recyclerView.setAdapter(adapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);


                            } else {
                                isteklerbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                odalarbut.setBackground(getResources().getDrawable(R.drawable.roundbackground,getTheme()));
                                isClicked[1] = false;
                                isClicked[0] = false;
                                testUsers.clear();

                                for (int i=0; i<users.length; i++){
                                    if (!databaseHelper.getCheckUsers().isEmpty()){
                                        if (!databaseHelper.getCheckUsers().get(0).getUsername().equalsIgnoreCase(users[i])){
                                            testUsers.add(new userData(users[i],DAVETGONDER));
                                            userAdapter adapter = new userAdapter(context,testUsers);
                                            recyclerView.setAdapter(adapter);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                        }
                                    }

                                }
                            }
                        }
                    }


                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                // Post verilerini tanımlama
                Map<String, String> params = new HashMap<>();
                params.put("getall",databaseHelper.getCheckUsers().get(0).getUsername());
                return params;
            }

            @Nullable
            @Override
            public Response.ErrorListener getErrorListener() {

                return super.getErrorListener();
            }
        };

        requestQueue.add(request);


        baslik.setTag(R.string.bastex,BASTEX);
        kapat.setTag(R.string.bastex,BASTEX);

        ViewEditor(baslik,18,18);
        ViewEditor(kapat,18,18);

        popUp.setOutsideTouchable(false);
        popUp.setFocusable(false);
        popUp.setContentView(layout);

        try{
            popUp.update(0, 0, glowidth-glowidth/8, gloheight/2+gloheight/3);
            popUp.showAtLocation(layout, Gravity.CENTER,0,0);
            serPopup = popUp;
        } catch (Exception e){
            serPopup = null;

        }

        darkenBackground(0.3f);

        kapat.setOnClickListener(v -> popUp.dismiss());

        popUp.setOnDismissListener(() -> {
            serPopup = null;
            darkenBackground(1f);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MainActivity.this.getApplicationContext();

        random = new Random();

        if (getIntent().getExtras()!=null){
            extras = getIntent().getExtras();
            if (extras.getString("openPopup")!=null){
                if (extras.getString("openPopup").equalsIgnoreCase("open")){
                    Toast.makeText(context, "Server Ekranı Açılıyor...", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    Runnable runnable = () -> {
                        if (databaseHelper.getCheckUsers()==null || databaseHelper.getCheckUsers().isEmpty()){
                            Toast.makeText(context, "Kurulum Tamamlanıyor, Bildirimlere İzin Verip Sonra Tekrar Deneyin...", Toast.LENGTH_SHORT).show();
                        } else {
                            createServerPopup(MainActivity.this,"normal");
                        }
                    };
                    handler.postDelayed(runnable,3000);
                }
            }
        }






        weakActivity = new WeakReference<>(MainActivity.this);

        if (!NotificationManagerCompat.from(MainActivity.this).areNotificationsEnabled()){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.mainlay),"Bildirimlere izin verebilirsiniz...",BaseTransientBottomBar.LENGTH_LONG);
            snackbar.setAction("Tamam", view -> snackbar.dismiss());
            snackbar.show();
        }

        realtimeNotification(context);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        Handler handler2 = new Handler();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                rewardedAdset(databaseHelper);
                handler2.removeCallbacks(this::run);
            }
        };
        handler2.postDelayed(runnable2,5000);

        List<Currents> currents = databaseHelper.getCurrents();
        List<Statics> statics = databaseHelper.getStatics();
        if (currents.isEmpty()){
            updateCurrent(databaseHelper);
        } else {
            String[] bolcur = currents.get(0).toString().split(">");
            correct=Integer.parseInt(bolcur[1]);
            falses=Integer.parseInt(bolcur[2]);
            runnies = Integer.parseInt(bolcur[3]);
        }

        if (statics.isEmpty()){
            updateStatics(databaseHelper,0);
        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!statics.isEmpty()){
                    updateStatics(databaseHelper,4);
                }

                handler.postDelayed(this,65000);
            }
        };
        handler.postDelayed(runnable,65000);



        mainrunny = findViewById(R.id.mainrunny);

        chose1=findViewById(R.id.chose1);
        chose2=findViewById(R.id.chose2);
        chose3=findViewById(R.id.chose3);
        chose4=findViewById(R.id.chose4);
        corscore=findViewById(R.id.corscore);
        wrongscore=findViewById(R.id.wrongscore);
        runnyscore=findViewById(R.id.runnyscore);
        playsound=findViewById(R.id.playsound);
        choosertext=findViewById(R.id.choosertext);
        helpidletext = findViewById(R.id.helpidletext);
        leftar = findViewById(R.id.leftar);
        rightar = findViewById(R.id.rightar);
        helprunn = findViewById(R.id.helprunn);
        correctim = findViewById(R.id.correctim);
        wrongim = findViewById(R.id.wrongim);
        runnyim = findViewById(R.id.runnyim);

        chose1.setTag(R.string.bastex,BASTEX);
        chose2.setTag(R.string.bastex,BASTEX);
        chose3.setTag(R.string.bastex,BASTEX);
        chose4.setTag(R.string.bastex,BASTEX);
        chose1.setTag(R.string.cards,R.id.card1);
        chose2.setTag(R.string.cards,R.id.card2);
        chose3.setTag(R.string.cards,R.id.card3);
        chose4.setTag(R.string.cards,R.id.card4);
        corscore.setTag(R.string.bastex,    BASTEX);
        wrongscore.setTag(R.string.bastex,  BASTEX);
        runnyscore.setTag(R.string.bastex,  BASTEX);
        choosertext.setTag(R.string.bastex, BASTEX);
        helpidletext.setTag(R.string.bastex,BASTEX);
        ViewEditor(chose1,20,20);
        ViewEditor(chose2,20,20);
        ViewEditor(chose3,20,20);
        ViewEditor(chose4,20,20);
        ViewEditor(corscore,15,15);
        ViewEditor(wrongscore,15,15);
        ViewEditor(runnyscore,15,15);
        ViewEditor(choosertext,15,15);
        ViewEditor(helpidletext,22,22);

        corscore.setText("x"+correct);
        wrongscore.setText("x"+falses);
        runnyscore.setText("x"+runnies);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gloheight = displayMetrics.heightPixels;
        glowidth = displayMetrics.widthPixels;

        leftar.setOnClickListener(view -> {
            yoyoclas(Techniques.Bounce,500,0,view);
            menuPopup(view.getContext());


        });

      getPassingClass(rightar);


        List<Ads> adsList = databaseHelper.getAds();
        if (adsList.isEmpty()){
            Ads ads = new Ads(-1,"1",new SimpleDateFormat(YYMMDD, Locale.getDefault()).format(new Date()),RUNNYKAZAN);
            databaseHelper.AddAds(ads);
        }



        ArrayList<String> engwords = new ArrayList<>(Arrays.asList(new allwords().engwords));
        ArrayList<String> trwords = new ArrayList<>(Arrays.asList(new allwords().trwords));

        initializeMatchWords(engwords,trwords);
        initializeİdleAnim(correctim,Techniques.Tada);
        initializeİdleAnim(wrongim,Techniques.Tada);
        initializeİdleAnim(runnyim,Techniques.Tada);
        initializeİdleAnim(mainrunny,Techniques.Tada);
        initializeİdleAnim(helprunn,Techniques.Tada);
        initializeİdleAnim(playsound,Techniques.Bounce);
        initializeİdleAnim(helpidletext,Techniques.SlideInLeft);

        helprunnie(runnyim);

        helprunn.setOnClickListener(v -> {
            if (databaseHelper.getCheckUsers()==null || databaseHelper.getCheckUsers().isEmpty()){
                Toast.makeText(context, "Kurulum Tamamlanıyor, Bildirimlere İzin Verip Sonra Tekrar Deneyin...", Toast.LENGTH_SHORT).show();
            } else {
                createServerPopup(v.getContext(),"normal");
            }

        });

        mainrunny.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(),staticsfornoti.class);
            startActivity(intent);
            yoyoclas(Techniques.Bounce,500,0,view);
        });

    }
    private void darkenBackground(Float bgcolor) {
        MainActivity mMainActivity = MainActivity.this;
        WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
        lp.alpha = bgcolor;
        mMainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mMainActivity.getWindow().setAttributes(lp);
    }

    private void helprunnie(View view){
        view.setOnClickListener(view1 -> {
            yoyoclas(Techniques.Bounce,500,0, view1);
            idlehelp++;
            if (runnies>0 && idlehelp<3){
                helpinRunnie();
                yoyoclas(Techniques.Shake,1000,0,runnyim);
                yoyoclas(Techniques.Shake,1000,0,mainrunny);
                yoyoclas(Techniques.Shake,1000,0,helprunn);
            } else if (runnies>0) {
                helpidletext.setText("Bence şimdilik bu kadarı yeterli...");
                yoyoclas(Techniques.FadeIn,500,0,helpidletext);
                yoyoclas(Techniques.Wobble,1000,0,runnyim);
                yoyoclas(Techniques.Wobble,1000,0,mainrunny);
                yoyoclas(Techniques.Wobble,1000,0,helprunn);
                mediaPlayer = MediaPlayer.create(view1.getContext(),R.raw.incorrect);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer=null;
                });
            } else {
                ProgressDialog progressDialog = new ProgressDialog(view1.getContext());
                progressDialog.setMessage("Yükleniyor...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                if (mRewardedAd!=null){
                    rewardAd();
                    progressDialog.dismiss();

                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mainlay),"Runny'e bir daha dokunun",BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAction("OK!", view11 -> {
                      rewardedAdset(databaseHelper);
                      snackbar.dismiss();
                    });
                    snackbar.show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void rewardAd(){
        Activity activityContext = MainActivity.this;
        mRewardedAd.show(activityContext, rewardItem -> {
            // Handle the reward.
            int rewardAmount = rewardItem.getAmount();
            List<Currents> currents = databaseHelper.getCurrents();
            String currunny = currents.get(0).getRuncount();
            Currents currents1 = new Currents(-1,currents.get(0).getCorcount(),currents.get(0).getWrocount(),(Integer.parseInt(currunny)+rewardAmount)+"");
            if (!databaseHelper.deleteCurrents(currents.get(0))){
                databaseHelper.AddCurrents(currents1);
            }

        });
    }


    private void initializeİdleAnim(View view, Techniques techniques){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,10000);
                yoyoclas(techniques,2000,0,view);
            }
        };
        handler.postDelayed(runnable,10000);

    }

    private void initializeMP(int rawid,Context context,Boolean istrue,ArrayList<String> engwords, ArrayList<String> trwords){
        mediaPlayer = MediaPlayer.create(context,rawid);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;

            if (istrue){
                numbers.clear();
                initializeMatchWords(engwords, trwords);
            }
        });
    }
    private void playSound(int id,Boolean istrue,ArrayList<String> engwords, ArrayList<String> trwords){
        playsound.setOnClickListener(view -> {
            initializeMP(id,view.getContext(),istrue,engwords,trwords);
            yoyoclas(Techniques.Shake,500,0,playsound);
        });

    }

    private void initParticuleEfect(int maxParticles,int timetToLive,float speedMin,float speedMax,int minAngle,int maxAngle,View view,int numParticles,int drawKonfet){

        if (checkInfo()){
            new ParticleSystem(MainActivity.this,maxParticles,drawKonfet,timetToLive)
                    .setSpeedModuleAndAngleRange(speedMin, speedMax, minAngle, maxAngle)
                    .oneShot(view, numParticles);
        }

    }


    private void initializeMatchWords(ArrayList<String> engwords, ArrayList<String> trwords){
        numbers.add(findViewById(R.id.chose1));
        numbers.add(findViewById(R.id.chose2));
        numbers.add(findViewById(R.id.chose3));
        numbers.add(findViewById(R.id.chose4));

        helpidletext.setText("Arkadaşlarınla Oynamak İçin Dokun! ===>");
        idlehelp=0;


        Collections.shuffle(numbers);
        mainengword=findViewById(R.id.mainengword);
        Collections.shuffle(engwords);
        Collections.shuffle(trwords);
        String[] coll = engwords.get(0).split("-");
        mainengword.setText(coll[0]);

        for (int i=0; i<numbers.size(); i++){
            MakeUpTextview(numbers.get(i),1);
            yoyoclas(Techniques.FlipInY,1000,0,findViewById(Integer.parseInt(numbers.get(i).getTag(R.string.cards).toString())));
            CardView cardView = findViewById(Integer.parseInt(numbers.get(i).getTag(R.string.cards).toString()));
            cardView.setTag(R.string.runnych,"0");
            cardView.setVisibility(View.VISIBLE);
        }

        initializeMP(new allwords().voices[Integer.parseInt(coll[1])],mainengword.getContext(),false,engwords,trwords);

        int     a = 0,
                b=0,
                c=0;
        for (int i=0; i<trwords.size(); i++){
            String[] words = trwords.get(i).split("-");
            if(words[1].equalsIgnoreCase(coll[1])){
                a=i;
                numbers.get(0).setText(words[0]);
                playSound(new allwords().voices[Integer.parseInt(coll[1])],false,engwords,trwords);
                clickTrue(numbers.get(0),engwords,trwords,numbers);
                CardView cardView = findViewById(Integer.parseInt(numbers.get(0).getTag(R.string.cards).toString()));
                cardView.setTag(R.string.runnych,"true");
            }
        }

        for (int i=0; i<trwords.size(); i++){
            String[] words = trwords.get(i).split("-");
            if (a!=i && a!=i+1 && a!=i-1 && !words[0].equalsIgnoreCase(numbers.get(0).getText().toString())){
                numbers.get(1).setText(words[0]);
                b=i;
                for (int x=0; x<engwords.size(); x++){
                    String[] wordsx = engwords.get(x).split("-");
                    if (wordsx[1].equalsIgnoreCase(words[1])){
                        clickFalse(numbers.get(1),wordsx[0],engwords,trwords);
                        CardView cardView = findViewById(Integer.parseInt(numbers.get(0).getTag(R.string.cards).toString()));
                        cardView.setTag(R.string.runnych,WRONG);
                    }
                }
            }
        }

        for (int i=0; i<trwords.size(); i++){
            String[] words = trwords.get(i).split("-");
            if ((a!=i && a!=i+1 && a!=i-1)&&(b!=i && b!=i+1 && b!=i-1)&&!words[0].equalsIgnoreCase(numbers.get(0).getText().toString())&&!words[0].equalsIgnoreCase(numbers.get(1).getText().toString())){

                numbers.get(2).setText(words[0]);
                c=i;

                for (int x=0; x<engwords.size(); x++){
                    String[] wordsx = engwords.get(x).split("-");
                    if (wordsx[1].equalsIgnoreCase(words[1])){
                        clickFalse(numbers.get(2),wordsx[0],engwords,trwords);
                        CardView cardView = findViewById(Integer.parseInt(numbers.get(2).getTag(R.string.cards).toString()));
                        cardView.setTag(R.string.runnych,WRONG);
                    }
                }
            }

        }

        for (int i=0; i<trwords.size(); i++){
            String[] words = trwords.get(i).split("-");
            if ((a!=i && a!=i+1 && a!=i-1)&&(b!=i && b!=i+1 && b!=i-1)&&(c!=i && c!=i+1 && c!=i-1)&&!words[0].equalsIgnoreCase(numbers.get(0).getText().toString())&&!words[0].equalsIgnoreCase(numbers.get(1).getText().toString())&&!words[0].equalsIgnoreCase(numbers.get(2).getText().toString())){

                numbers.get(3).setText(words[0]);
                for (int x=0; x<engwords.size(); x++){
                    String[] wordsx = engwords.get(x).split("-");
                    if (wordsx[1].equalsIgnoreCase(words[1])){
                        clickFalse(numbers.get(3),wordsx[0],engwords,trwords);
                        CardView cardView = findViewById(Integer.parseInt(numbers.get(3).getTag(R.string.cards).toString()));
                        cardView.setTag(R.string.runnych,WRONG);
                    }
                }
            }
        }

    }

    private void clickTrue(TextView textView,ArrayList<String> engwords, ArrayList<String> trwords,ArrayList<TextView> numbers){
        textView.setOnClickListener(view -> {
            yoyoclas(Techniques.Bounce,500,0,view);
            yoyoclas(Techniques.Tada,1000,0,findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString())));
            initializeMP(R.raw.correcttune,view.getContext(),true,engwords,trwords);
            initParticuleEfect(50,1200,0.2f,0.5f,0,270,textView,50,R.drawable.ic_runnykonfet);
            MakeUpTextview(textView,2);


            for (int i=0; i<numbers.size(); i++){
                numbers.get(i).setOnClickListener(null);
            }

            correct++;

            initParticuleEfect(50,500,0.2f,0.5f,270,-360,correctim,50,R.drawable.ic_corkonfet);
            corscore.setText("x"+correct);
            if(correct>=50){
                opensuccPop(view.getContext());
                runnies=runnies+3;
                animator(corscore,correct,correct-50,1000);
                runnyscore.setText("x"+runnies);
                correct=correct-50;
                initParticuleEfect(50,1200,0.2f,0.5f,0,270,runnyim,50,R.drawable.ic_runnykonfet);

            }
            if(correct>=10){
                opensuccPop(view.getContext());
                if (falses>0){
                    falses--;
                    Toast.makeText(MainActivity.this, "1 yanlış silindi (-10 Doğru)", Toast.LENGTH_SHORT).show();
                    animator(corscore,correct,correct-10,1000);
                    correct=correct-10;
                    wrongscore.setText("x"+falses);

                }

            }

            updateCurrent(databaseHelper);
            updateStatics(databaseHelper,1);
        });
    }

    private void initcardParticulate(int id){
         switch(id){
             case R.id.card1:
                 initParticuleEfect(250,1200,0.4f,0.8f,80,110,mainrunny,250,R.drawable.ic_runnykonfet);
                 break;
             case R.id.card2:
                 initParticuleEfect(250,1200,0.4f,0.8f,200,230,runnyim,250,R.drawable.ic_runnykonfet);
                 break;
             case R.id.card3:
                 initParticuleEfect(250,1200,0.4f,0.8f,30,60,mainrunny,250,R.drawable.ic_runnykonfet);
                 break;
             case R.id.card4:
                 initParticuleEfect(250,1200,0.4f,0.8f,270,300,runnyim,250,R.drawable.ic_runnykonfet);
                 break;
             default:
                 break;
         }
    }

   private void updateStatics(DatabaseHelper databaseHelper,int which){
      List<Statics> statics = databaseHelper.getStatics();
      Statics statics1 = null;
      switch (which){
          case 0: statics1 = new Statics(-1,0+"",0+"",0+"",0+"");
            break;
          case 1: statics1 = new Statics(-1,(Integer.parseInt(statics.get(0).getCorcount())+1)+"",statics.get(0).getWrocount(),statics.get(0).getRuncount(),statics.get(0).getTimecount());
              break;
          case 2: statics1 = new Statics(-1,statics.get(0).getCorcount(),(Integer.parseInt(statics.get(0).getWrocount())+1)+"",statics.get(0).getRuncount(),statics.get(0).getTimecount());
              break;
          case 3: statics1 = new Statics(-1,statics.get(0).getCorcount(),statics.get(0).getWrocount(),(Integer.parseInt(statics.get(0).getRuncount())+1)+"",statics.get(0).getTimecount());
              break;
          case 4: statics1 = new Statics(-1,statics.get(0).getCorcount(),statics.get(0).getWrocount(),statics.get(0).getRuncount(),(Integer.parseInt(statics.get(0).getTimecount())+1)+"");
              break;
          default:
              break;
      }

      if (statics.isEmpty()){
          databaseHelper.AddStatics(statics1);
      }else{
          if (!databaseHelper.deleteStatics(statics.get(0))){
              databaseHelper.AddStatics(statics1);
          }
      }

   }

   private void updateCurrent(DatabaseHelper databaseHelper){
        List<Currents> currents = databaseHelper.getCurrents();
        if (currents.isEmpty()){
            Currents currents1 = new Currents(-1,correct+"",falses+"",runnies+"");
            databaseHelper.AddCurrents(currents1);
        } else {
            Currents currents1 = new Currents(-1,correct+"",falses+"",runnies+"");
            if (!databaseHelper.deleteCurrents(currents.get(0))){
                databaseHelper.AddCurrents(currents1);
            }
        }
   }
    private void helpinRunnie(){
        CardView cardView1 = findViewById(Integer.parseInt(numbers.get(1).getTag(R.string.cards).toString()));
        CardView cardView2 = findViewById(Integer.parseInt(numbers.get(2).getTag(R.string.cards).toString()));
        CardView cardView3 = findViewById(Integer.parseInt(numbers.get(3).getTag(R.string.cards).toString()));



        if (cardView1.getTag(R.string.runnych).toString().equalsIgnoreCase(WRONG)){
            yoyoclas(Techniques.SlideOutDown,500,0,cardView1);
            cardView1.setTag(R.string.runnych,WRONGC);
            runnies--;

            runnyscore.setText("x"+runnies);
            initcardParticulate(Integer.parseInt(numbers.get(1).getTag(R.string.cards).toString()));
            yoyoclas(Techniques.FadeIn,500,0,helpidletext);
            final int randomi = random.nextInt(idledialogs.length);
            helpidletext.setText("'"+numbers.get(1).getText().toString()+"' "+idledialogs[randomi]);
            mediaPlayer = MediaPlayer.create(cardView1.getContext(),R.raw.downcard);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            });
        } else if (cardView2.getTag(R.string.runnych).toString().equalsIgnoreCase(WRONG)){
            yoyoclas(Techniques.SlideOutDown,500,0,cardView2);
            cardView2.setTag(R.string.runnych,WRONGC);
            runnies--;

            runnyscore.setText("x"+runnies);
            initcardParticulate(Integer.parseInt(numbers.get(2).getTag(R.string.cards).toString()));
            yoyoclas(Techniques.FadeIn,500,0,helpidletext);
            final int randomi = random.nextInt(idledialogs.length);
            helpidletext.setText("'"+numbers.get(2).getText().toString()+"' "+idledialogs[randomi]);
            mediaPlayer = MediaPlayer.create(cardView1.getContext(),R.raw.downcard);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            });
        } else if (cardView3.getTag(R.string.runnych).toString().equalsIgnoreCase(WRONG)){
            yoyoclas(Techniques.SlideOutDown,500,0,cardView3);
            cardView3.setTag(R.string.runnych,WRONGC);
            runnies--;

            runnyscore.setText("x"+runnies);
            initcardParticulate(Integer.parseInt(numbers.get(3).getTag(R.string.cards).toString()));

            yoyoclas(Techniques.FadeIn,500,0,helpidletext);
            final int randomi = random.nextInt(idledialogs.length);
            helpidletext.setText("'"+numbers.get(3).getText().toString()+"' "+idledialogs[randomi]);
            mediaPlayer = MediaPlayer.create(cardView1.getContext(),R.raw.downcard);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            });
        }
        updateCurrent(databaseHelper);
        updateStatics(databaseHelper,3);
    }

    private void yoyoclas(Techniques name, int dur, int rep, View v){

        if (checkInfo()){
            YoYo.with(name)
                    .duration(dur)
                    .repeat(rep)
                    .playOn(v);
        }
    }

    private void MakeUpTextview(TextView textView,int isfalse){
        if (isfalse==0){
            CardView cardView = findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString()));
            cardView.setBackgroundResource(R.drawable.ic_wrong);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.drawable.whitebgrad);
            textView.setElevation(10);
        } else if (isfalse==1) {
            textView.setBackgroundResource(0);
            CardView cardView = findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString()));
            cardView.setCardBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.palette4,null));
            cardView.setBackgroundResource(R.drawable.palette4bg);
            cardView.setRadius(10);
            cardView.setElevation(10);
            textView.setTextColor(Color.BLACK);
        } else if (isfalse==2){
            CardView cardView = findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString()));
            cardView.setBackgroundResource(R.drawable.ic_correct);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.drawable.whitebgrad);
            textView.setElevation(10);
        }

    }

    private void animator(TextView textView,int v1,int v2,int duration){
        ValueAnimator animator = ValueAnimator.ofInt(v1, v2); //0 is min number, 600 is max number
        animator.setDuration(duration); //Duration is in milliseconds
        animator.addUpdateListener(animation -> textView.setText("x"+animation.getAnimatedValue().toString()));
        animator.start();
    }
    private void clickFalse(TextView textView,String word,ArrayList<String> engwords, ArrayList<String> trwords){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setOnClickListener(null);
                YoYo.with(Techniques.FlipOutX)
                        .duration(500)
                        .repeat(0)
                        .playOn(findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString())));

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        MakeUpTextview(textView,0);
                        textView.setText(word);
                        YoYo.with(Techniques.FlipInX)
                                .duration(500)
                                .repeat(0)
                                .playOn(findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString())));
                        handler.removeCallbacks(this);
                    }
                };
                handler.postDelayed(runnable,500);

                initializeMP(R.raw.incorrect,view.getContext(),false,engwords,trwords);
                falses++;
                if (runnies>0){
                    runnies--;
                }

                yoyoclas(Techniques.Wobble,500,2,helprunn);
                initParticuleEfect(50,500,0.2f,0.5f,-180,360,wrongim,50,R.drawable.ic_wrongkonfet);
                wrongscore.setText("x"+falses);
                runnyscore.setText("x"+runnies);
                if(correct>=10 && falses>0){
                    falses--;
                    Toast.makeText(MainActivity.this, "1 yanlış silindi (-10 Doğru)", Toast.LENGTH_SHORT).show();
                    animator(corscore,correct,correct-10,1000);
                    correct=correct-10;
                    wrongscore.setText("x"+falses);
                }
                CardView cardView = findViewById(Integer.parseInt(textView.getTag(R.string.cards).toString()));
                cardView.setTag(R.string.runnych,WRONGC);

                updateCurrent(databaseHelper);
                updateStatics(databaseHelper,2);
                updateStatics(databaseHelper,3);
            }
        });
    }

    public void ViewEditor(@NonNull View view, int olc1, int olc2){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double width = displayMetrics.widthPixels;



        if (view.getTag(R.string.bastex).toString().equalsIgnoreCase(BASTEX)){

            int ayar = (int) width/olc1;
            TextView textView = (TextView) view;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,ayar);
        } else if (view.getTag().toString().equalsIgnoreCase("basedit")){
            int ayar = (int) width/olc1;
            EditText editText = (EditText) view;
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,ayar);
        }  else if (view.getTag().toString().equalsIgnoreCase("basbut")){
            int ayar = (int) width/olc1;
            Button button = (Button) view;
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,ayar);
        } else if (view.getTag().toString().equalsIgnoreCase("basswitch")){
            int ayar = (int) width/olc1;
            Switch aSwitch = (Switch) view;
            aSwitch.setTextSize(TypedValue.COMPLEX_UNIT_PX,ayar);
        } else if (view.getTag().toString().equalsIgnoreCase("basimg")){
            int ayar1 = (int) width/olc1;
            int ayar2 = (int) width/olc2;
            ImageView imageView = (ImageView) view;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ayar1,ayar2));
        } else if (view.getTag().toString().equalsIgnoreCase("bascard")){
            int ayar1 = (int) width/olc1;
            int ayar2 = (int) width/olc2;
            CardView cardView = (CardView) view;
            cardView.setLayoutParams(new LinearLayout.LayoutParams(ayar1,ayar2));
        }

    }
}