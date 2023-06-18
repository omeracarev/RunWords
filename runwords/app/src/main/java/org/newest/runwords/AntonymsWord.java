package org.newest.runwords;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
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
import android.widget.RatingBar;
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

public class AntonymsWord extends AppCompatActivity {
    private ImageView leftarsy,rightarsy,helprunnsy,runnyimsy,mainrunnysyan;

    private String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";
    private ArrayList<TextView> optexts = new ArrayList<TextView>();
    private ArrayList<TextView> coptexts = new ArrayList<TextView>();
    private ArrayList<String> sygur1 = new ArrayList<>(Arrays.asList(new antwords().col1words));
    private ArrayList<String> sygur2 = new ArrayList<>(Arrays.asList(new antwords().col2words));
    private ArrayList<String> sygur3 = new ArrayList<>(Arrays.asList(new antwords().coltrwords));
    private int[] allrawsop = new antwords().colrawsop;
    private int[] allrawscop = new antwords().colrawscop;

    private PopupWindow curpopup = null;

    private int bundleIndex = 4;

    private RewardedAd mRewardedAd;
    private int countdown=0;
    private int truescore = 0;
    private int wrongscores = 0;
    private int runnies = 0;

    private int glowidth,gloheight=0;

    private DatabaseHelper databaseHelper;

    private String colorop = "#e05d5d";
    private String colorcop = "#00ACC1";

    private String selectedcol = "#AA00FF";
    TextView choosertextty,helpidletexty,corscore,wrongscore,runnyscore,op1drag,op2drag,op3drag,op4drag,op5drag,cop1drag,cop2drag,cop3drag,cop4drag,cop5drag;

    private boolean checkInfo(){
        int dpi = getResources().getDisplayMetrics().densityDpi;
        if (dpi<440){
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("MissingInflatedId")

    public void realtimeNotification(Context context){

        // AlarmManager objesini al
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

// Tetiklenecek zaman aralığını belirle
        long intervalMillis = 60 * 1000; // 60 saniye

// Broadcast Receiver için intent oluştur
        Intent intent = new Intent(context, GetNewsActionService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
// AlarmManager'a bildirimi ayarla
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);


    }

    private void createServerPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);
        View layout = View.inflate(context,R.layout.server_card_layout,null);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,AntonymsWord.this.getTheme()));

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

    private void menuPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,AntonymsWord.this.getTheme()));
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
                darkenBackground(1f);
                curpopup=null;
            }
        });

        dismisspopupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

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
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
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
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,AntonymsWord.this.getTheme()));
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
                    public void onAdFailedToLoad(LoadAdError adError) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antonyms_word);

        leftarsy = findViewById(R.id.leftarsyan);
        rightarsy = findViewById(R.id.rightarsyan);
        helprunnsy = findViewById(R.id.helprunnsyan);
        runnyimsy = findViewById(R.id.runnyimsyan);
        choosertextty = findViewById(R.id.choosertextsyan);
        helpidletexty = findViewById(R.id.helpidletextsyan);
        corscore = findViewById(R.id.corscoresyan);
        wrongscore = findViewById(R.id.wrongscoresyan);
        runnyscore = findViewById(R.id.runnyscoresyan);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gloheight = displayMetrics.heightPixels;
        glowidth = displayMetrics.widthPixels;

        databaseHelper = new DatabaseHelper(AntonymsWord.this);
        rewardedAdset();

        realtimeNotification(AntonymsWord.this);

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

        leftarsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
             menuPopup(view.getContext());
            }
        });

        getPassingClass(rightarsy);

        op1drag = findViewById(R.id.op1dragan);
        op2drag = findViewById(R.id.op2dragan);
        op3drag = findViewById(R.id.op3dragan);
        op4drag = findViewById(R.id.op4dragan);
        op5drag = findViewById(R.id.op5dragan);
        cop1drag = findViewById(R.id.cop1dragan);
        cop2drag = findViewById(R.id.cop2dragan);
        cop3drag = findViewById(R.id.cop3dragan);
        cop4drag = findViewById(R.id.cop4dragan);
        cop5drag = findViewById(R.id.cop5dragan);
        mainrunnysyan = findViewById(R.id.mainrunnysyan);

        op1drag.setTag(R.string.bastex,"bastex");
        op2drag.setTag(R.string.bastex,"bastex");
        op3drag.setTag(R.string.bastex,"bastex");
        op4drag.setTag(R.string.bastex,"bastex");
        op5drag.setTag(R.string.bastex,"bastex");

        cop1drag.setTag(R.string.bastex,"bastex");
        cop2drag.setTag(R.string.bastex,"bastex");
        cop3drag.setTag(R.string.bastex,"bastex");
        cop4drag.setTag(R.string.bastex,"bastex");
        cop5drag.setTag(R.string.bastex,"bastex");

        op1drag.setTag(R.string.cards,R.id.op1cardan);
        op2drag.setTag(R.string.cards,R.id.op2cardan);
        op3drag.setTag(R.string.cards,R.id.op3cardan);
        op4drag.setTag(R.string.cards,R.id.op4cardan);
        op5drag.setTag(R.string.cards,R.id.op5cardan);

        cop1drag.setTag(R.string.cards,R.id.cop1cardan);
        cop2drag.setTag(R.string.cards,R.id.cop2cardan);
        cop3drag.setTag(R.string.cards,R.id.cop3cardan);
        cop4drag.setTag(R.string.cards,R.id.cop4cardan);
        cop5drag.setTag(R.string.cards,R.id.cop5cardan);

        ViewEditor(op1drag,20,20);
        ViewEditor(op2drag,20,20);
        ViewEditor(op3drag,20,20);
        ViewEditor(op4drag,20,20);
        ViewEditor(op5drag,20,20);
        ViewEditor(cop1drag,20,20);
        ViewEditor(cop2drag,20,20);
        ViewEditor(cop3drag,20,20);
        ViewEditor(cop4drag,20,20);
        ViewEditor(cop5drag,20,20);



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

        initializeGroups();
        initializeİdleAnim(findViewById(R.id.correctimsyan),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.wrongimsyan),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.runnyimsyan),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.mainrunnysyan),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.helprunnsyan),Techniques.Tada);
        initializeİdleAnim(findViewById(R.id.helpidletextsyan),Techniques.SlideInLeft);
        runnyscore.setText("x"+runnies);
        corscore.setText("x"+truescore);
        wrongscore.setText("x"+wrongscores);

        helprunnsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                intent.putExtra("openPopup","open");
                startActivity(intent);
                finish();
            }
        });

        runnyimsy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                helpinRunnie();

            }
        });

        mainrunnysyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                // createPopup(mainrunny,view.getContext());
                Intent intent = new Intent(view.getContext(),staticsfornoti.class);
                startActivity(intent);
            }
        });




    }

    private void orandetect(RatingBar ratingBar, int oran){
        if (oran<=0){
            ratingBar.setRating(0.5F);
        } else if (oran>=1 && oran<=10){
            ratingBar.setRating(1);
        } else if (oran>10 && oran<=30){
            ratingBar.setRating(1.5F);
        } else if (oran>30 && oran<=100){
            ratingBar.setRating(2F);
        } else if (oran>100 && oran<=300){
            ratingBar.setRating(2.5F);
        } else if (oran>300 && oran<=1000){
            ratingBar.setRating(3F);
        } else if (oran>1000 && oran<=3000){
            ratingBar.setRating(3.5F);
        } else if (oran>3000 && oran<=10000){
            ratingBar.setRating(4F);
        } else if (oran>10000 && oran<=10000){
            ratingBar.setRating(4.5F);
        } else if (oran>100000){
            ratingBar.setRating(5F);
        }
    }


    private void createPopup(View dropdown, Context context){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int glowidth = displayMetrics.widthPixels;
        int gloheight = displayMetrics.heightPixels;

        PopupWindow popUp = new PopupWindow(context);
        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent));
        View layout = View.inflate(context,R.layout.statics,null);

        List<Statics> statics = databaseHelper.getStatics();

        int oran = Integer.parseInt(statics.get(0).getCorcount())-(Integer.parseInt(statics.get(0).getWrocount())+Integer.parseInt(statics.get(0).getRuncount()));

        String[] texler = new String[]{statics.get(0).getCorcount()+" Kelime",statics.get(0).getWrocount()+" Kelime",statics.get(0).getRuncount()+" Runny",statics.get(0).getTimecount()+" Dakika"};


        TextView stattext = layout.findViewById(R.id.stattext);
        TextView backcal = layout.findViewById(R.id.backcal);

        stattext.setTag(R.string.bastex,"bastex");
        backcal.setTag(R.string.bastex,"bastex");

        ViewEditor(stattext,13,13);
        ViewEditor(backcal,13,13);

        stattext.setBackgroundResource(R.drawable.roundbackground4);

        LinearLayout scrolay = layout.findViewById(R.id.scrolay);

        for (int i=0; i<scrolay.getChildCount(); i++){
            CardView cardView = (CardView) scrolay.getChildAt(i);
            cardView.setCardBackgroundColor(Color.parseColor(colorop));
            LinearLayout cardiclay = (LinearLayout) cardView.getChildAt(0);
            ImageView imageView = (ImageView) cardiclay.getChildAt(0);
            imageView.getLayoutParams().height = gloheight/5;
            LinearLayout cardiclayiclay = (LinearLayout) cardiclay.getChildAt(1);
            TextView basliko = (TextView) cardiclayiclay.getChildAt(0);
            basliko.setTag(R.string.bastex,"bastex");
            basliko.setTextColor(Color.parseColor(colorop));
            ViewEditor(basliko,20,20);
            if (i==0){
                RatingBar ratingBar = (RatingBar) cardiclayiclay.getChildAt(1);
                orandetect(ratingBar,oran);
                ratingBar.setClickable(false);
            } else {
                TextView textView = (TextView) cardiclayiclay.getChildAt(1);
                textView.setTag(R.string.bastex,"bastex");
                ViewEditor(textView,15,15);
                textView.setTextColor(Color.parseColor(colorop));
                textView.setText(texler[i-1]);
            }
        }

        backcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                popUp.dismiss();
            }
        });


        darkenBackground(0.3f);

        popUp.setOutsideTouchable(true);
        popUp.setFocusable(true);
        popUp.setContentView(layout);
        popUp.update(glowidth/2, gloheight/2, glowidth, gloheight/2+gloheight/5);
        popUp.showAsDropDown(dropdown);

        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    private void darkenBackground(Float bgcolor) {
        AntonymsWord mMainActivity = AntonymsWord.this;
        WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
        lp.alpha = bgcolor;
        mMainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mMainActivity.getWindow().setAttributes(lp);
    }

    private void initializeGroups(){

        LinearLayout cardopgroup = findViewById(R.id.cardopgroupan);
        LinearLayout cardcopgroup = findViewById(R.id.cardcopgroupan);

        for (int i=0; i<cardopgroup.getChildCount(); i++){
            CardView cardop = (CardView) cardopgroup.getChildAt(i);
            CardView cardcop = (CardView) cardcopgroup.getChildAt(i);
            TextView textop = (TextView) cardop.getChildAt(0);
            TextView textcop = (TextView) cardcop.getChildAt(0);
            yoyoclas(Techniques.SlideInLeft,500,0,cardop);
            yoyoclas(Techniques.SlideInRight,500,0,cardcop);

            cardop.setVisibility(View.VISIBLE);
            cardcop.setVisibility(View.VISIBLE);

            cardop.setCardBackgroundColor(Color.parseColor(colorop));
            cardcop.setCardBackgroundColor(Color.parseColor(colorcop));
        }



        optexts.removeAll(optexts);
        coptexts.removeAll(coptexts);


        optexts.add(findViewById(R.id.op1dragan));
        optexts.add(findViewById(R.id.op2dragan));
        optexts.add(findViewById(R.id.op3dragan));
        optexts.add(findViewById(R.id.op4dragan));
        optexts.add(findViewById(R.id.op5dragan));

        coptexts.add(findViewById(R.id.cop1dragan));
        coptexts.add(findViewById(R.id.cop2dragan));
        coptexts.add(findViewById(R.id.cop3dragan));
        coptexts.add(findViewById(R.id.cop4dragan));
        coptexts.add(findViewById(R.id.cop5dragan));

        helpidletexty.setText("Arkadaşlarınla Oynamak İçin Dokun! ===>");

        Collections.shuffle(optexts);
        Collections.shuffle(coptexts);
        Collections.shuffle(sygur1);
        Collections.shuffle(sygur2);



        for (int i=0; i<cardopgroup.getChildCount(); i++){
            String[] ispl = sygur1.get(i).split("-");
            optexts.get(i).setText(ispl[0]);
            optexts.get(i).setTag(R.string.otherspl,ispl[1]);
            for (int j=0; j<sygur3.size(); j++){
                String[] jspl = sygur3.get(j).split("-");
                if (ispl[1].equalsIgnoreCase(jspl[1])){
                    optexts.get(i).setTag(R.string.trmean,jspl[0]);
                }
            }
            initOpGroup(findViewById(Integer.parseInt(optexts.get(i).getTag(R.string.cards).toString())),optexts.get(i),allrawsop[Integer.parseInt(ispl[1])]);
            for (int k=0; k<sygur2.size(); k++){
                String[] kspl = sygur2.get(k).split("-");

                if (ispl[1].equalsIgnoreCase(kspl[1])){
                    coptexts.get(i).setText((kspl[0]));
                    coptexts.get(i).setTag(R.string.otherspl,kspl[1]);
                    for (int j=0; j<sygur3.size(); j++){
                        String[] jspl = sygur3.get(j).split("-");
                        if (kspl[1].equalsIgnoreCase(jspl[1])){
                            coptexts.get(i).setTag(R.string.trmean,jspl[0]);
                        }
                    }

                    iniCopGroup(findViewById(Integer.parseInt(coptexts.get(i).getTag(R.string.cards).toString())),coptexts.get(i),allrawscop[Integer.parseInt(kspl[1])]);
                }
            }
        }



    }


    private void initOpGroup(CardView cardView,TextView textView,int rawID){
        cardView.setTag(R.string.focused,"falseop");
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yoyoclas(Techniques.FlipInX,500,0,view);
                if (cardView.getTag(R.string.focused).toString().equalsIgnoreCase("trueop")){
                    cardView.setCardBackgroundColor(Color.parseColor(colorop));
                    cardView.setTag(R.string.focused,"falseop");
                } else {
                    cardView.setCardBackgroundColor(Color.parseColor(selectedcol));
                    cardView.setTag(R.string.focused,"trueop");
                    new Dictionary().useMediaPlayer(rawID,view.getContext());
                    closeotherCards("op",cardView);
                    isCorrect();
                }

            }
        });
    }


    private void iniCopGroup(CardView cardView,TextView textView,int rawID){
        cardView.setTag(R.string.focused,"falsecop");
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yoyoclas(Techniques.FlipInX,500,0,view);
                if (cardView.getTag(R.string.focused).toString().equalsIgnoreCase("truecop")){
                    cardView.setCardBackgroundColor(Color.parseColor(colorcop));
                    cardView.setTag(R.string.focused,"falsecop");

                } else {
                    cardView.setCardBackgroundColor(Color.parseColor(selectedcol));
                    cardView.setTag(R.string.focused,"truecop");
                    new Dictionary().useMediaPlayer(rawID,view.getContext());
                    closeotherCards("cop",cardView);
                    isCorrect();


                }
            }
        });
    }

    private void closeotherCards(String grup, CardView curcard){
        LinearLayout cardopgroup = findViewById(R.id.cardopgroupan);
        LinearLayout cardcopgroup = findViewById(R.id.cardcopgroupan);

        if (grup.equalsIgnoreCase("op")){
            for (int i=0; i<cardopgroup.getChildCount(); i++){
                CardView cardView = (CardView) cardopgroup.getChildAt(i);
                if (cardView.getTag(R.string.focused).toString().equalsIgnoreCase("trueop")&&cardView!=curcard){
                    yoyoclas(Techniques.FlipInX,500,0,cardView);
                    cardView.setCardBackgroundColor(Color.parseColor(colorop));
                    cardView.setTag(R.string.focused,"falseop");
                }
            }
        } else {
            for (int i=0; i<cardcopgroup.getChildCount(); i++){
                CardView cardView = (CardView) cardcopgroup.getChildAt(i);
                if (cardView.getTag(R.string.focused).toString().equalsIgnoreCase("truecop")&&cardView!=curcard){
                    yoyoclas(Techniques.FlipInX,500,0,cardView);
                    cardView.setCardBackgroundColor(Color.parseColor(colorcop));
                    cardView.setTag(R.string.focused,"falsecop");
                }
            }
        }
    }

    private void isCorrect(){
        LinearLayout cardopgroup = findViewById(R.id.cardopgroupan);
        LinearLayout cardcopgroup = findViewById(R.id.cardcopgroupan);

        for (int i=0; i<cardopgroup.getChildCount(); i++){
            CardView cardop = (CardView) cardopgroup.getChildAt(i);
            for (int j=0; j<cardcopgroup.getChildCount();j++){
                CardView cardcop = (CardView) cardcopgroup.getChildAt(j);
                if (cardop.getTag(R.string.focused).toString().equalsIgnoreCase("trueop")&&cardcop.getTag(R.string.focused).toString().equalsIgnoreCase("truecop")){

                    TextView optext = (TextView) cardop.getChildAt(0);
                    TextView coptext = (TextView) cardcop.getChildAt(0);
                    if (optext.getTag(R.string.otherspl).toString().equalsIgnoreCase(coptext.getTag(R.string.otherspl).toString())){
                        new Dictionary().useMediaPlayer(R.raw.correcttune,cardop.getContext());
                        yoyoclas(Techniques.Tada,900,0,cardop);
                        yoyoclas(Techniques.Tada,900,0,cardcop);
                        initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardop,50,R.drawable.ic_runnykonfet);
                        initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardcop,50,R.drawable.ic_runnykonfet);
                        countdown++;
                        truescore++;
                        corscore.setText("x"+truescore);
                        initParticuleEfect(50,500,0.2f,0.5f,270,-360,findViewById(R.id.correctimsyan),50,R.drawable.ic_corkonfet);
                        if(truescore>=50){
                            opensuccPop(AntonymsWord.this);
                            runnies=runnies+3;
                            animator(corscore,truescore,truescore-50,1000);
                            truescore=truescore-50;
                            runnyscore.setText("x"+runnies);
                            initParticuleEfect(50,1200,0.2f,0.5f,0,270,runnyimsy,50,R.drawable.ic_runnykonfet);

                        }
                        if(truescore>=10){
                            opensuccPop(AntonymsWord.this);
                            if (wrongscores>0){
                                wrongscores--;
                                animator(corscore,truescore,truescore-10,1000);
                                truescore=truescore-10;
                                wrongscore.setText("x"+wrongscores);
                            }
                        }

                        helpidletexty.setText(optext.getText().toString()+"->"+optext.getTag(R.string.trmean).toString()+"<-"+coptext.getText().toString());


                        updateCurrent(databaseHelper);
                        updateStatics(databaseHelper,1);

                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                cardop.setVisibility(View.GONE);
                                cardcop.setVisibility(View.GONE);

                                cardop.setTag(R.string.focused,"falseop");
                                cardcop.setTag(R.string.focused,"falsecop");
                                if (countdown==cardopgroup.getChildCount()){
                                    initializeGroups();
                                    countdown=0;
                                }
                            }
                        };
                        handler.postDelayed(runnable,1000);



                    } else {
                        wrongscores++;
                        if (runnies>0){
                            runnies--;
                        }

                        yoyoclas(Techniques.Wobble,500,2,helprunnsy);
                        initParticuleEfect(50,500,0.2f,0.5f,-180,360,findViewById(R.id.wrongimsyan),50,R.drawable.ic_wrongkonfet);
                        new Dictionary().useMediaPlayer(R.raw.incorrect,cardopgroup.getContext());
                        wrongscore.setText("x"+wrongscores);
                        runnyscore.setText("x"+runnies);
                        yoyoclas(Techniques.FlipInX,500,0,cardop);
                        yoyoclas(Techniques.FlipInX,500,0,cardcop);
                        cardop.setTag(R.string.focused,"falseop");
                        cardcop.setTag(R.string.focused,"falseop");
                        cardop.setCardBackgroundColor(Color.parseColor(colorop));
                        cardcop.setCardBackgroundColor(Color.parseColor(colorcop));


                        updateCurrent(databaseHelper);
                        updateStatics(databaseHelper,2);
                        updateStatics(databaseHelper,3);

                    }
                }
            }
        }
    }

    private void initParticuleEfect(int maxParticles,int timetToLive,float speedMin,float speedMax,int minAngle,int maxAngle,View view,int numParticles,int drawim){
        if (checkInfo()){
            new ParticleSystem(AntonymsWord.this,maxParticles,drawim,timetToLive)
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

    private void helpinRunnie(){

        if (runnies<=0){
            runnies = 0;
        }

        if (runnies>0){
            runnies--;
            runnyscore.setText("x"+runnies);
            LinearLayout cardopgroup = findViewById(R.id.cardopgroupan);
            LinearLayout cardcopgroup = findViewById(R.id.cardcopgroupan);

            for (int i=0; i<cardopgroup.getChildCount(); i++){
                Boolean breakloop = false;
                for (int j=0; j<cardcopgroup.getChildCount(); j++) {
                    CardView cardView = (CardView) cardopgroup.getChildAt(i);
                    CardView cardView2 = (CardView) cardcopgroup.getChildAt(j);

                    TextView textView = (TextView) cardView.getChildAt(0);
                    TextView textView2 = (TextView) cardView2.getChildAt(0);
                    if (textView.getTag(R.string.trmean).toString().equalsIgnoreCase(textView2.getTag(R.string.trmean).toString())){
                        if (cardView.isShown()&&cardView2.isShown()){
                            ArrayList<String> commeRunny = new ArrayList<String>();
                            commeRunny.add(textView.getText().toString()+" ile "+textView2.getText().toString()+" kelimeleri ayrı dünyalarda...");
                            commeRunny.add(textView.getText().toString()+" ile "+textView2.getText().toString()+" kulağa zıt geliyor...");
                            commeRunny.add(textView.getText().toString()+" ve "+textView2.getText().toString()+" birbirinin tam tersi gibi...");
                            commeRunny.add(textView.getText().toString()+" ve "+textView2.getText().toString()+" birbirinin zıt anlamı olabilir...");
                            Collections.shuffle(commeRunny);

                            yoyoclas(Techniques.Bounce,1000,2,cardView);
                            yoyoclas(Techniques.Bounce,1000,2,cardView2);
                            initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardView,50,R.drawable.ic_runnykonfet);
                            initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardView2,50,R.drawable.ic_runnykonfet);
                            new Dictionary().useMediaPlayer(R.raw.downcard,cardopgroup.getContext());
                            helpidletexty.setText(commeRunny.get(0));
                            breakloop = true;
                            updateCurrent(databaseHelper);
                            updateStatics(databaseHelper,3);

                        }
                    }

                }
                if (breakloop){
                    break;
                }
            }



        } else {
            ProgressDialog progressDialog = new ProgressDialog(AntonymsWord.this);
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.setCancelable(true);
            progressDialog.show();
            if (mRewardedAd!=null){
                rewardAd();
                progressDialog.dismiss();

            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.antanylay),"Runny'e bir daha dokunun", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("OK!", view -> snackbar.dismiss());
                snackbar.show();
                progressDialog.dismiss();
            }
        }

    }

    private void rewardAd(){
        Activity activityContext = AntonymsWord.this;
        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                // Handle the reward.
                int rewardAmount = rewardItem.getAmount();
                DatabaseHelper databaseHelper = new DatabaseHelper(AntonymsWord.this);

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