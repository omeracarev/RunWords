package org.newest.runwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PassinAds extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ImageView runimsta;
    private TextView  runimhaktext;
    private TextView  runimcounter;

    private SeekBar geciseek;

    private Class curclass;

    private Class[] classes = new Class[]{Dictionary.class,MainActivity.class, SynonymsWord.class,AntonymsWord.class, IrregularVerbs.class, DraggedWords.class};

    private Bundle extras;

    private Handler handler;
    private int progressStatus = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passin_ads);

        Context context = this;

        databaseHelper = new DatabaseHelper(context);

        runimsta     =findViewById(R.id.runimstap);
        runimhaktext =findViewById(R.id.runimhaktextp);
        runimcounter =findViewById(R.id.runimcounterp);
        geciseek =findViewById(R.id.geciseek);
        geciseek.setMax(100);

        runimhaktext .setTag(R.string.bastex,"bastex");
        runimcounter.setTag(R.string.bastex,"bastex");

        ViewEditor(runimhaktext,20,20);
        ViewEditor(runimcounter,25,25);

        List<Ads> adsList = databaseHelper.getAds();

        extras = getIntent().getExtras();

        int index = extras.getInt("index");
        curclass = classes[index];

        handler = new Handler();

        Intent intent = new Intent(context,curclass);

        // SeekBar'ı güncellemek için Runnable oluşturuluyor
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Progress durumunu artırma

                geciseek.setProgress(progressStatus);
                // 50 milisaniye gecikme ile tekrar çalıştırma
                handler.postDelayed(this, 80);
                // 5 saniye tamamlandığında durdurma
                if (progressStatus < 100) {
                    progressStatus++;

                    // SeekBar'ı güncelleme

                } else {
                    handler.removeCallbacks(this);
                    startActivity(intent);
                    finish();
                }
            }
        };

        handler.postDelayed(runnable, 500);




        if (!adsList.get(0).getAd_quota().equalsIgnoreCase("0")){
            runimhaktext.setText("WinRunny Hakkını Kullan!");
            runimcounter.setText("Runny ikonuna dokunarak başlatabilirsin!");

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
                    runimcounter.setText("Runny ikonuna dokunarak başlatabilirsin!");
                }
            };

            countDownTimer.start();
        }
       LinearLayout passinglay = findViewById(R.id.passinglay);
        NativeAdView nativeAdView = (NativeAdView) NativeAdView.inflate(context,R.layout.gecis_reklam,null);
        passinglay.addView(nativeAdView,1);
        nativeAdView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,16));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nativeAdView.getLayoutParams();
        layoutParams.setMargins(10,10,10,10);

        NativeAdView[] finalNativeAdView = {nativeAdView};
        finalNativeAdView[0].setVisibility(View.INVISIBLE);
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-2627695336411451/2286747134")
                .forNativeAd(nativeAd -> {
                    try{
                        finalNativeAdView[0].setVisibility(View.VISIBLE);

                        MediaView mediaView = finalNativeAdView[0].findViewById(R.id.nativeVideo);

                        if (nativeAd.getMediaContent().hasVideoContent()){
                           mediaView.setMediaContent(nativeAd.getMediaContent());
                            mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        RatingBar nativeRating = finalNativeAdView[0].findViewById(R.id.nativeRating);

                        if (nativeAd.getStarRating()!=null){
                            nativeRating.setRating(nativeAd.getStarRating().floatValue());
                        }

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
                        .setVideoOptions(new VideoOptions.Builder()
                                .setStartMuted(true).build())
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());


    }


    private void yoyoclas(Techniques name, int dur, int rep, View v){
        YoYo.with(name)
                .duration(dur)
                .repeat(rep)
                .playOn(v);


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