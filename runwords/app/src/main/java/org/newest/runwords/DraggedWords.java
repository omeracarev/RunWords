package org.newest.runwords;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.android.volley.VolleyError;
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
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.plattysoft.leonids.ParticleSystem;

import org.newest.runwords.Services.GetNewsActionService;

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

public class DraggedWords extends AppCompatActivity {
    private ImageView leftarsy,rightarsy,helprunnsy,runnyimsy,mainrunnysyan,playsounddg;
    private TextView choosertextty,helpidletexty,corscore,wrongscore,runnyscore;

    private String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";

    private LinearLayout engwordtami,harflay,harflay2;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> engwords = new ArrayList<>(Arrays.asList(new allwords().engwords));
    private ArrayList<String> alltrwords = new ArrayList<>(Arrays.asList(new allwords().trwords));
    private RewardedAd mRewardedAd;

    PopupWindow curpopup = null;

    private int bundleIndex = 0;

    private int boscardsay=0;
    private int truescore = 0;
    private int wrongscores = 0;
    private int runnies = 0;

    private int glowidth,gloheight = 0;

    private ArrayList<CardView> hidecards = new ArrayList<>();
    private ArrayList<CardView> visicards = new ArrayList<>();

    private boolean checkInfo(){
        int dpi = getResources().getDisplayMetrics().densityDpi;
        if (dpi<440){
            return false;
        } else {
            return true;
        }
    }

    private void menuPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,DraggedWords.this.getTheme()));
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

        runimhaktext .setTag(R.string.bastex,"bastex");
        runimcounter.setTag(R.string.bastex,"bastex");
        menupop1.setTag(R.string.bastex,"bastex");
        menupop2.setTag(R.string.bastex,"bastex");
        menupop3.setTag(R.string.bastex,"bastex");
        menupop4.setTag(R.string.bastex,"bastex");
        menupop5.setTag(R.string.bastex,"bastex");
        menupop6.setTag(R.string.bastex,"bastex");
        staticgor.setTag(R.string.bastex,"bastex");
        dismisspopupmenu.setTag(R.string.bastex,"bastex");

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

        staticgor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
                Intent intent = new Intent(v.getContext(),staticsfornoti.class);
                startActivity(intent);
                finish();
            }
        });

        databaseHelper = new DatabaseHelper(context);

        List<Ads> adsList = databaseHelper.getAds();
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
                    Ads ads = new Ads(-1,"1",new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),adsList.get(0).getAd_type());
                    if (!databaseHelper.deleteAds(adsList.get(0))){
                        if (databaseHelper.AddAds(ads)){

                        }
                    }
                    runimhaktext.setText("WinRunny Hakkını Kullan!");
                    runimcounter.setText("Buraya dokunarak challenge başlat!");
                }
            };

            countDownTimer.start();
        }

        runimsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });

        runimcounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        }

        darkenBackground(0.3f);

        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                curpopup = null;
                darkenBackground(1f);

            }
        });

        dismisspopupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
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


    private void rewardedAdset(){

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
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Handler handler = new Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        List<Currents> currents = databaseHelper.getCurrents();
                                        runnies = Integer.parseInt(currents.get(0).getRuncount());
                                        runnyscore.setText("x"+runnies+"");
                                        mRewardedAd = null;
                                    }
                                };
                                handler.postDelayed(runnable,100);

                                Log.d(TAG, "Ad was dismissed.");

                                mRewardedAd = null;
                            }
                        });
                    }
                });


    }

    private void opensuccPop(Context context){
        int a = truescore%10;
        if (truescore!=0 && a==0){
            String corcounte = truescore+"";
            successPopup(context,corcounte);
        }
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
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,DraggedWords.this.getTheme()));
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

        devamlup.setTag(R.string.bastex,"bastex");
        helpidletextlup.setTag(R.string.bastex,"bastex");
        hedeftextlup.setTag(R.string.bastex,"bastex");
        nexthedeflup.setTag(R.string.bastex,"bastex");

        ViewEditor(devamlup,13,13);
        ViewEditor(helpidletextlup,12,12);
        ViewEditor(hedeftextlup,15,15);
        ViewEditor(nexthedeflup,20,20);

        List<Currents> currents = databaseHelper.getCurrents();
        Currents currents1 = new Currents(-1,currents.get(0).getCorcount(),0+"",currents.get(0).getRuncount());
        if (!databaseHelper.deleteCurrents(currents.get(0))){
            if (databaseHelper.AddCurrents(currents1)){
                wrongscores=0;
                wrongscore.setText("x"+wrongscores+"");
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

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.

                    }

                    @Override
                    public void onAdClicked() {

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

        }

        darkenBackground(0.3f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                devamlup.setVisibility(View.VISIBLE);
                leveluppoplay.removeView(progressBar);
            }
        },5000);

        devamlup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                popUp.dismiss();
            }
        });

        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
                finalNativeAdView[0].destroy();
            }
        });
    }

    private void updateCurrent(DatabaseHelper databaseHelper){
        List<Currents> currents = databaseHelper.getCurrents();
        if (currents.isEmpty()){
            Currents currents1 = new Currents(-1,truescore+"",wrongscores+"",runnies+"");
            if (databaseHelper.AddCurrents(currents1)){

            }
        } else {
            Currents currents1 = new Currents(-1,truescore+"",wrongscores+"",runnies+"");
            if (!databaseHelper.deleteCurrents(currents.get(0))){
                if (databaseHelper.AddCurrents(currents1)){

                }
            }
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
        }

        if (statics.isEmpty()){
            if (databaseHelper.AddStatics(statics1)){

            }
        }else{
            if (!databaseHelper.deleteStatics(statics.get(0))){
                if (databaseHelper.AddStatics(statics1)){

                }
            }
        }

    }


    public void getPassingClass(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragged_words);

        Context context = DraggedWords.this;
        databaseHelper = new DatabaseHelper(context);

        rewardedAdset();

        realtimeNotification(DraggedWords.this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         glowidth = displayMetrics.widthPixels;
        gloheight = displayMetrics.heightPixels;


        leftarsy = findViewById(R.id.leftardg);
        rightarsy = findViewById(R.id.rightardg);
        helprunnsy = findViewById(R.id.helprunndg);
        runnyimsy = findViewById(R.id.runnyimdg);
        mainrunnysyan = findViewById(R.id.mainrunnydg);
        choosertextty = findViewById(R.id.choosertextdg);
        helpidletexty = findViewById(R.id.helpidletextdg);
        corscore = findViewById(R.id.corscoredg);
        wrongscore = findViewById(R.id.wrongscoredg);
        runnyscore = findViewById(R.id.runnyscoredg);
        playsounddg = findViewById(R.id.playsounddg);

        List<Currents> currents = databaseHelper.getCurrents();
        List<Statics> statics = databaseHelper.getStatics();
        if (currents.isEmpty()){
            updateCurrent(databaseHelper);
        } else {
            String[] bolcur = currents.get(0).toString().split(">");
            truescore=Integer.parseInt(bolcur[1]);
            wrongscores=Integer.parseInt(bolcur[2]);
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

        corscore.setTag(R.string.bastex,"bastex");
        wrongscore.setTag(R.string.bastex,"bastex");
        runnyscore.setTag(R.string.bastex,"bastex");
        choosertextty.setTag(R.string.bastex,"bastex");
        helpidletexty.setTag(R.string.bastex,"bastex");
        ViewEditor(choosertextty,15,15);
        ViewEditor(helpidletexty,22,22);
        ViewEditor(corscore,15,15);
        ViewEditor(wrongscore,15,15);
        ViewEditor(runnyscore,15,15);
        runnyscore.setText("x"+runnies);
        corscore.setText("x"+truescore);
        wrongscore.setText("x"+wrongscores);

        leftarsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               menuPopup(view.getContext());
            }
        });

         getPassingClass(rightarsy);

        helprunnsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yoyoclas(Techniques.Bounce,500,0,v);
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("openPopup","open");
                startActivity(intent);
                finish();
            }
        });

        runnyimsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yoyoclas(Techniques.Bounce,500,0,v);
                helprunnie();
            }
        });

        mainrunnysyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yoyoclas(Techniques.Bounce,500,0,v);
                Intent intent = new Intent(v.getContext(),staticsfornoti.class);
                startActivity(intent);
            }
        });

        initializeİdleAnim(findViewById(R.id.correctimdg),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.wrongimdg),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.runnyimdg),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.mainrunnydg),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.helprunndg),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.helpidletextdg),Techniques.SlideInLeft);
        initializeLayout(context);
    }

    private void playWord(int rawid){
        new Dictionary().useMediaPlayer(rawid,DraggedWords.this);
    }

    private void helprunnie(){

        if (runnies<=0){
            runnies = 0;
        }

        if (runnies>0){

            runnies--;
            runnyscore.setText("x"+runnies);

            yoyoclas(Techniques.Flash,1000,4,hidecards.get(0));
            for (int i=0; i<harflay.getChildCount(); i++){
                CardView cardView = (CardView) harflay.getChildAt(i);
                TextView textView = (TextView) cardView.getChildAt(0);

                TextView textView2 = (TextView) hidecards.get(0).getChildAt(0);
                if (textView.getText().toString().equalsIgnoreCase(textView2.getText().toString())){
                    yoyoclas(Techniques.Flash,1000,4,cardView);
                }
            }

            for (int i=0; i<harflay2.getChildCount(); i++){
                CardView cardView = (CardView) harflay2.getChildAt(i);
                TextView textView = (TextView) cardView.getChildAt(0);

                TextView textView2 = (TextView) hidecards.get(0).getChildAt(0);
                if (textView.getText().toString().equalsIgnoreCase(textView2.getText().toString())){
                    yoyoclas(Techniques.Flash,1000,4,cardView);
                }
            }

            new Dictionary().useMediaPlayer(R.raw.downcard,DraggedWords.this);
            updateCurrent(databaseHelper);
            updateStatics(databaseHelper,3);
        } else {
            ProgressDialog progressDialog = new ProgressDialog(DraggedWords.this);
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            if (mRewardedAd!=null){
                rewardAd();
                progressDialog.dismiss();

            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.maindragwords),"Runny'e bir daha dokunun", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("OK!", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                progressDialog.dismiss();
            }
        }

    }

    private void createServerPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);
        View layout = View.inflate(context,R.layout.server_card_layout,null);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,DraggedWords.this.getTheme()));

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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                            int finalTopwait = topwait;
                            final Boolean[] isClicked = {false,false};

                            //Butonlar arası geçişi kodla

                            isteklerbut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

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
                                                    testUsers.add(new userData(users[i],"Davet\nGönder"));
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
                            });



                            odalarbut.setOnClickListener(v -> {
                                if (isClicked[1]==false){
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
                                                testUsers.add(new userData(users[i],"Davet\nGönder"));
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
                                        testUsers.add(new userData(users[i],"Davet\nGönder"));
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


        baslik.setTag(R.string.bastex,"bastex");
        kapat.setTag(R.string.bastex,"bastex");

        ViewEditor(baslik,18,18);
        ViewEditor(kapat,18,18);

        popUp.setOutsideTouchable(false);
        popUp.setFocusable(false);
        popUp.setContentView(layout);

        try{
            popUp.update(0, 0, glowidth-glowidth/8, gloheight/2+gloheight/3);
            popUp.showAtLocation(layout, Gravity.CENTER,0,0);
        } catch (Exception e){

        }

        darkenBackground(0.3f);

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }


    private void rewardAd(){
        Activity activityContext = DraggedWords.this;
        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                // Handle the reward.
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
                DatabaseHelper databaseHelper = new DatabaseHelper(DraggedWords.this);

                List<Currents> currents = databaseHelper.getCurrents();
                String currunny = currents.get(0).getRuncount();
                Currents currents1 = new Currents(-1,currents.get(0).getCorcount(),currents.get(0).getWrocount(),(Integer.parseInt(currunny)+rewardAmount)+"");
                if (!databaseHelper.deleteCurrents(currents.get(0))){
                    if (databaseHelper.AddCurrents(currents1)){

                    }
                }

            }
        });
    }

    private void dropcard(CardView cardView){
        cardView.setOnDragListener((view, dragEvent) -> {
            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                    String[] bol = item.getText().toString().split("///");
                    TextView textView = (TextView) cardView.getChildAt(0);
                    if (view.getTag(R.string.wintext).toString().trim().equalsIgnoreCase(bol[0].trim())&&!textView.isShown()){
                        findViewById(Integer.parseInt(bol[1])).setVisibility(View.INVISIBLE);
                        cardView.setCardBackgroundColor(Color.parseColor("#e9c670"));
                        hidecards.remove(cardView);
                        textView.setVisibility(View.VISIBLE);
                        new Dictionary().useMediaPlayer(R.raw.correcttune,view.getContext());
                        initParticuleEfect(50,1200,0.2f,0.5f,0,270,textView,50,R.drawable.ic_corkonfet);
                        boscardsay--;
                        if (boscardsay==0){
                            initializeLayout(DraggedWords.this);
                            initParticuleEfect(50,1200,0.2f,0.5f,0,270,mainrunnysyan,50,R.drawable.ic_runnykonfet);
                            truescore++;
                            corscore.setText("x"+truescore);

                            if(truescore>=50){
                                opensuccPop(DraggedWords.this);
                                runnies=runnies+3;
                                animator(corscore,truescore,truescore-50,1000);
                                truescore=truescore-50;
                                runnyscore.setText("x"+runnies);
                                initParticuleEfect(50,1200,0.2f,0.5f,0,270,runnyimsy,50,R.drawable.ic_runnykonfet);
                                List<Currents> currents = databaseHelper.getCurrents();
                                Currents newcur = new Currents(-1,currents.get(0).getCorcount(),currents.get(0).getWrocount(),(Integer.parseInt(currents.get(0).getRuncount())+1)+"");
                                if (!databaseHelper.deleteCurrents(currents.get(0))){
                                    if (databaseHelper.AddCurrents(newcur)){
                                        Toast.makeText(DraggedWords.this, "+1 Runny Eklendi!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if(truescore>=10){
                                opensuccPop(DraggedWords.this);
                                if (wrongscores>0){
                                    wrongscores--;
                                    animator(corscore,truescore,truescore-10,1000);
                                    truescore=truescore-10;
                                    wrongscore.setText("x"+wrongscores);

                                }

                            }


                            updateCurrent(databaseHelper);
                            updateStatics(databaseHelper,1);
                        }
                    } else {
                        new Dictionary().useMediaPlayer(R.raw.incorrect,view.getContext());
                        wrongscores++;
                        if (runnies>0){
                            runnies--;
                        }

                        wrongscore.setText("x"+wrongscores);
                        runnyscore.setText("x"+runnies);
                        if(truescore>=10){
                            if (wrongscores>0){
                                wrongscores--;
                                animator(corscore,truescore,truescore-10,1000);
                                truescore=truescore-10;
                                wrongscore.setText("x"+wrongscores);

                            }

                        }
                        updateCurrent(databaseHelper);
                        updateStatics(databaseHelper,2);
                        updateStatics(databaseHelper,3);
                        yoyoclas(Techniques.Wobble,500,2,helprunnsy);
                    }

                    break;
            }
            return true;
        });
    }

    private void dragcard(CardView cardView){

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ClipData.Item item = new ClipData.Item((CharSequence) cardView.getTag(R.string.wintext));
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData dragData = new ClipData(view.getTag(R.string.wintext).toString(),mimeTypes, item);
                        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(cardView);
                        view.startDragAndDrop(dragData,myShadow,null,0);
                        break;
                }
                return true;
            }
        });
    }


    private void initializeLayout(Context context){

        hidecards.removeAll(hidecards);
        visicards.removeAll(visicards);



        engwordtami = findViewById(R.id.engwordtamidg);
        harflay = findViewById(R.id.harflaydg);
        harflay2 = findViewById(R.id.harflay2dg);

        engwordtami.removeAllViews();
        harflay.removeAllViews();
        harflay2.removeAllViews();

        ArrayList newlist = new ArrayList<String>();
        for (int i=0; i<engwords.size(); i++){
            String[] engbol = engwords.get(i).trim().split("-");
            if (engbol[0].replaceAll(" ","").trim().length()<7){
                newlist.add(engwords.get(i).replaceAll(" ","").trim().toUpperCase(Locale.ROOT));

            }
        }

        ArrayList<String> alphabet = new ArrayList<String>(Arrays.asList(new Dictionary().alp));
        Collections.shuffle(alphabet);

        Collections.shuffle(newlist);

        String ekstr = newlist.get(0).toString();
        String[] ekstrdizo = ekstr.split("-");
        String[] ekstrdizk = ekstrdizo[0].split("");

        ArrayList<String> ekstrdiz = new ArrayList();
        for (int i=0; i<ekstrdizk.length; i++){
            if (!ekstrdizk[i].isEmpty()){
                ekstrdiz.add(ekstrdizk[i]);
            }
        }




        for (int i=0; i<alltrwords.size(); i++){
            String[] trword = alltrwords.get(i).split("-");
            if (trword[1].equalsIgnoreCase(ekstrdizo[1])){
                TextView textView = findViewById(R.id.mainengworddg);
                textView.setText(trword[0]);
                textView.setTag(R.string.bastex,"bastex");
                ViewEditor(textView,10,10);
            }
        }

        playWord(new allwords().voices[Integer.parseInt(ekstrdizo[1])]);

        playsounddg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playWord(new allwords().voices[Integer.parseInt(ekstrdizo[1])]);
                yoyoclas(Techniques.Shake,500,0,view);
            }
        });


        int num1 = new Random().nextInt(ekstrdiz.size());
        int num2 = new Random().nextInt(ekstrdiz.size());
        if (num1==num2){
            while(num1!=num2){
                num1 = new Random().nextInt(ekstrdiz.size());
                num2 = new Random().nextInt(ekstrdiz.size());
            }
        }

        engwordtami.setWeightSum(ekstrdizo[0].length());
        harflay.setWeightSum(ekstrdizo[0].length());
        harflay2.setWeightSum(ekstrdizo[0].length());
        engwordtami.setWeightSum(ekstrdizo[0].length());

        ArrayList<String> olcakharf = new ArrayList<String>();
        for (int i=0; i<ekstrdiz.size(); i++){
            for (int j=0; j<alphabet.size(); j++){
                if (ekstrdiz.get(i).equalsIgnoreCase(alphabet.get(j))){
                    olcakharf.add(ekstrdiz.get(i));
                    if (j==alphabet.size()-1){
                        olcakharf.add(alphabet.get(j-2));
                    } else {
                        olcakharf.add(alphabet.get(j+1));
                    }
                }
            }
        }

        Collections.shuffle(olcakharf);
        for (int i=0; i<ekstrdiz.size(); i++){


            CardView cardView = new CardView(context);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1));
            cardView.setRadius(15);
            cardView.setElevation(5);
            cardView.setCardBackgroundColor(Color.parseColor("#e9c670"));
            cardView.setUseCompatPadding(true);
            engwordtami.addView(cardView);

            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTag(R.string.bastex,"bastex");
            textView.setText(ekstrdiz.get(i));
            ViewEditor(textView,8,8);
            textView.setTypeface(ResourcesCompat.getFont(context,R.font.didact_gothic), Typeface.BOLD);
            textView.setTextColor(Color.parseColor("#6200EA"));
            cardView.addView(textView);

            if (i!=num1 && i!=num2){
                textView.setVisibility(View.INVISIBLE);
                cardView.setCardBackgroundColor(Color.parseColor("#7f7681"));
                boscardsay++;
                hidecards.add(cardView);
            } else {
                visicards.add(cardView);
            }
            cardView.setTag(R.string.wintext,textView.getText().toString());
            dropcard(cardView);


        }

        int l=0;


        for (int i=0; i<ekstrdiz.size(); i++){

            if (l!=0){
                l++;
            }

            CardView cardView = new CardView(context);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1));
            cardView.setRadius(15);
            cardView.setElevation(5);
            cardView.setCardBackgroundColor(Color.parseColor("#6200EA"));
            cardView.setUseCompatPadding(true);
            harflay.addView(cardView);

            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTag(R.string.bastex,"bastex");
            try{
                textView.setText(olcakharf.get(l));
            } catch (Exception e){

            }

            ViewEditor(textView,8,8);
            textView.setTypeface(ResourcesCompat.getFont(context,R.font.didact_gothic),Typeface.BOLD);
            textView.setTextColor(Color.parseColor("#e9c670"));
            cardView.addView(textView);

            l++;

            CardView cardView2= new CardView(context);
            cardView2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1));
            cardView2.setRadius(15);
            cardView2.setElevation(5);
            cardView2.setCardBackgroundColor(Color.parseColor("#6200EA"));
            cardView2.setUseCompatPadding(true);
            harflay2.addView(cardView2);

            TextView textView2 = new TextView(context);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            textView2.setGravity(Gravity.CENTER);
            textView2.setTag(R.string.bastex,"bastex");
            try{
                textView2.setText(olcakharf.get(l));
            } catch (Exception e){

            }
            ViewEditor(textView2,8,8);
            textView2.setTypeface(ResourcesCompat.getFont(context,R.font.didact_gothic),Typeface.BOLD);
            textView2.setTextColor(Color.parseColor("#e9c670"));
            cardView2.addView(textView2);

            cardView.setId(i+10000);
            cardView2.setId(i+20000);

            cardView.setTag(R.string.wintext,textView.getText().toString()+"///"+cardView.getId());
            cardView2.setTag(R.string.wintext,textView2.getText().toString()+"///"+cardView2.getId());

            dragcard(cardView);
            dragcard(cardView2);


        }
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

    private void initParticuleEfect(int maxParticles,int timetToLive,float speedMin,float speedMax,int minAngle,int maxAngle,View view,int numParticles,int drawim){
        if (checkInfo()){
            new ParticleSystem(DraggedWords.this,maxParticles,drawim,timetToLive)
                    .setSpeedModuleAndAngleRange(speedMin, speedMax, minAngle, maxAngle)
                    .oneShot(view, numParticles);
        }

    }

    private void animator(TextView textView,int v1,int v2,int duration){
        ValueAnimator animator = ValueAnimator.ofInt(v1, v2); //0 is min number, 600 is max number
        animator.setDuration(duration); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("x"+animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }



    private void yoyoclas(Techniques name, int dur, int rep, View v){
        if (checkInfo()){
            YoYo.with(name)
                    .duration(dur)
                    .repeat(rep)
                    .playOn(v);
        }

    }

    private void darkenBackground(Float bgcolor) {
        DraggedWords mMainActivity = DraggedWords.this;
        WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
        lp.alpha = bgcolor;
        mMainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mMainActivity.getWindow().setAttributes(lp);
    }

    private void ViewEditor(@NonNull View view, int olc1, int olc2){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double width = displayMetrics.widthPixels;

        if (view.getTag(R.string.bastex).toString().equalsIgnoreCase("bastex")){

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