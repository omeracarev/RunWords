package org.newest.runwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class staticsfornoti extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private int glowidth;
    private int gloheight;

    private ImageView runimsta;
    private TextView runimhaktext,runimcounter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticsfornoti);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gloheight = displayMetrics.heightPixels;
        glowidth = displayMetrics.widthPixels;

        runimsta = findViewById(R.id.runimsta);
        runimcounter = findViewById(R.id.runimcounter);
        runimhaktext = findViewById(R.id.runimhaktext);

        runimhaktext.setTag(R.string.bastex,"bastex");
        runimcounter.setTag(R.string.bastex,"bastex");

        ViewEditor(runimhaktext,18,18);
        ViewEditor(runimcounter,20,20);



        Context context = this;
        databaseHelper = new DatabaseHelper(context);

        List<Statics> statics = databaseHelper.getStatics();

        int oran = Integer.parseInt(statics.get(0).getCorcount())-(Integer.parseInt(statics.get(0).getWrocount())+Integer.parseInt(statics.get(0).getRuncount()));

        String[] texler = new String[]{statics.get(0).getCorcount()+" Kelime",statics.get(0).getWrocount()+" Kelime",statics.get(0).getRuncount()+" Runny",statics.get(0).getTimecount()+" Dakika"};

        TextView stattext = this.findViewById(R.id.stattext);
        TextView backcal = this.findViewById(R.id.backcal);

        stattext.setTag(R.string.bastex,"bastex");
        backcal.setTag(R.string.bastex,"bastex");

        ViewEditor(stattext,13,13);
        ViewEditor(backcal,13,13);

        LinearLayout scrolay = this.findViewById(R.id.scrolay);

        for (int i=0; i<scrolay.getChildCount(); i++){
            CardView cardView = (CardView) scrolay.getChildAt(i);
            cardView.setCardBackgroundColor(Color.parseColor("#6200EA"));
            LinearLayout cardiclay = (LinearLayout) cardView.getChildAt(0);
            ImageView imageView = (ImageView) cardiclay.getChildAt(0);
            imageView.getLayoutParams().height = gloheight/5;
            LinearLayout cardiclayiclay = (LinearLayout) cardiclay.getChildAt(1);
            TextView basliko = (TextView) cardiclayiclay.getChildAt(0);
            basliko.setTag(R.string.bastex,"bastex");
            ViewEditor(basliko,20,20);
            if (i==0){
                RatingBar ratingBar = (RatingBar) cardiclayiclay.getChildAt(1);
                orandetect(ratingBar,oran);
                ratingBar.setClickable(false);


            } else {
                TextView textView = (TextView) cardiclayiclay.getChildAt(1);
                textView.setTag(R.string.bastex,"bastex");
                ViewEditor(textView,15,15);
                textView.setText(texler[i-1]);

            }
        }

        backcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
    }

    private void yoyoclas(Techniques name, int dur, int rep, View v){
        YoYo.with(name)
                .duration(dur)
                .repeat(rep)
                .playOn(v);
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