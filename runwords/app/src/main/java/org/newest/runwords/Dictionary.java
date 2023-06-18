package org.newest.runwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.newest.runwords.Services.GetNewsActionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Dictionary extends AppCompatActivity {
     GridView dictgrid;
     ImageView rightar,leftar;
     TextView choosertext;

     PopupWindow curpopup = null;

    private int bundleIndex = 1;
     MediaPlayer mediaPlayer;

     LinearLayout findletter;

     DatabaseHelper databaseHelper;

     int glowidth,gloheight;

     Integer curAlpInd=0;

     SearchView searchView;
    int widtho;
    int heighto;

   public String[] alp = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

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

    public void getPassingClass(View view){
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

                 if (curpopup!=null){
                     curpopup.dismiss();
                  }


                 Intent intent = new Intent(view.getContext(), PassinAds.class);
                 intent.putExtra("index",bundleIndex);
                 startActivity(intent);
                 finish();
             }
         });
     }
    private void menuPopup(Context context){
        PopupWindow popUp = new PopupWindow(context);

        popUp.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bgtransparent,Dictionary.this.getTheme()));
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widtho = displayMetrics.widthPixels;
        heighto = displayMetrics.heightPixels;

        dictgrid = findViewById(R.id.dictgrid);
        rightar = findViewById(R.id.rightar2);
        leftar = findViewById(R.id.leftar2);
        searchView = findViewById(R.id.searchViewDict);

        choosertext=findViewById(R.id.choosertext2);
        choosertext.setTag(R.string.bastex,"bastex");
        ViewEditor(choosertext,15,15);

        gloheight = displayMetrics.heightPixels;
        glowidth = displayMetrics.widthPixels;

        realtimeNotification(Dictionary.this);



       getPassingClass(rightar);

        leftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             menuPopup(view.getContext());
            }
        });


        ArrayList<dictwords> dictwordsArrayList = new ArrayList<dictwords>();

        ArrayList<String> engwords = new ArrayList<>(Arrays.asList(new allwords().engwords));

        Collections.sort(engwords);
        ArrayList<String> trwords = new ArrayList<>(Arrays.asList(new allwords().trwords));


        for (int i=0; i<engwords.size(); i++){
            String[] coll1 = engwords.get(i).split("-");
            for (int j=0; j<trwords.size(); j++){
                String[] coll2 = trwords.get(j).split("-");
                if (coll1[1].equalsIgnoreCase(coll2[1])){
                    dictwordsArrayList.add(new dictwords(coll1[0],coll2[0],new allwords().voices[Integer.parseInt(coll1[1])]));
                }
            }


        }

        DictAdapter dictAdapter = new DictAdapter(this,dictwordsArrayList);
        dictgrid.setAdapter(dictAdapter);

        findletter = findViewById(R.id.findletter);
        for (int i=0; i<alp.length; i++){
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams laypar = new LinearLayout.LayoutParams(widtho/10,LinearLayout.LayoutParams.MATCH_PARENT);
            laypar.setMargins(widtho/120,widtho/120,widtho/120,widtho/120);
            textView.setLayoutParams(laypar);
            textView.setTypeface(ResourcesCompat.getFont(this,R.font.didact_gothic), Typeface.BOLD);
            textView.setTag(R.string.bastex,"bastex");
            textView.setBackgroundResource(R.drawable.roundbackground4);
            textView.setText(alp[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            ViewEditor(textView,20,20);
            findletter.addView(textView);
            textView.setTag(R.string.focused,"false");

            int finalI = i;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textView.getTag(R.string.focused).toString().equalsIgnoreCase("false")){

                        for (int x=0; x<findletter.getChildCount(); x++){
                            TextView xte = (TextView) findletter.getChildAt(x);
                            xte.setBackgroundResource(R.drawable.roundbackground4);
                            xte.setTag(R.string.focused,"false");
                        }

                        focuser(alp[finalI],engwords,trwords);
                        textView.setTag(R.string.focused,"true");
                        textView.setBackgroundResource(R.drawable.roundbackground);
                        curAlpInd = finalI;
                    } else {
                        textView.setTag(R.string.focused,"false");
                        textView.setBackgroundResource(R.drawable.roundbackground4);
                        ArrayList<dictwords> dictwordsArrayList = new ArrayList<dictwords>();
                        for (int i=0; i<engwords.size(); i++){
                            String[] coll1 = engwords.get(i).split("-");
                            for (int j=0; j<trwords.size(); j++){
                                String[] coll2 = trwords.get(j).split("-");
                                if (coll1[1].equalsIgnoreCase(coll2[1])){
                                    dictwordsArrayList.add(new dictwords(coll1[0],coll2[0],new allwords().voices[Integer.parseInt(coll1[1])]));
                                }
                            }


                        }

                        DictAdapter dictAdapter = new DictAdapter(view.getContext(),dictwordsArrayList);
                        dictgrid.setAdapter(dictAdapter);

                        curAlpInd = 0;
                    }
                }
            });
        }


        searchView.setQueryHint(engwords.size()+" kelime içinden arayın...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.trim().toLowerCase(Locale.ROOT);
                ArrayList<dictwords> dictwordsArrayList2 = new ArrayList<dictwords>();
                for (int i=0; i<engwords.size(); i++){
                    String[] splity = engwords.get(i).split("-");
                    if (splity[0].toLowerCase(Locale.ROOT).contains(newText)){
                        for (int j=0; j<trwords.size(); j++){
                            String[] splity2 = trwords.get(j).split("-");
                            if (splity[1].equalsIgnoreCase(splity2[1])){
                                dictwordsArrayList2.add(new dictwords(splity[0],splity2[0],new allwords().voices[Integer.parseInt(splity[1])]));

                            }
                        }
                    }
                }

                DictAdapter dictAdapter = new DictAdapter(Dictionary.this,dictwordsArrayList2);
                dictgrid.setAdapter(dictAdapter);
                return true;
            }
        });

    }

    private void focuser(String focus,ArrayList<String> engwords,ArrayList<String> trwords){
        dictgrid.setAdapter(null);
        ArrayList<dictwords> dictwordsArrayList = new ArrayList<dictwords>();
       for (int i=0; i<engwords.size(); i++){
           if (engwords.get(i).charAt(0)==focus.charAt(0)){
               String[] coll1 = engwords.get(i).split("-");
               for (int j=0; j<trwords.size(); j++){
                   String[] coll2 = trwords.get(j).split("-");
                   if (coll1[1].equalsIgnoreCase(coll2[1])){
                       dictwordsArrayList.add(new dictwords(coll1[0],coll2[0],new allwords().voices[Integer.parseInt(coll1[1])]));
                   }
               }
           }
       }
        DictAdapter dictAdapter = new DictAdapter(this,dictwordsArrayList);
        dictgrid.setAdapter(dictAdapter);
    }

    private void yoyoclas(Techniques name, int dur, int rep, View v){
        YoYo.with(name)
                .duration(dur)
                .repeat(rep)
                .playOn(v);


    }
    public void openPopup(String engword, String trword, int rawid){
        PopupWindow popUp = new PopupWindow(Dictionary.this);
        popUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.bgtransparent));
        CardView cardView = (CardView) CardView.inflate(this,R.layout.wordcardlay,null);

        darkenBackground(0.4f);

        popUp.setOutsideTouchable(true);
        popUp.setFocusable(true);
        popUp.setContentView(cardView);
        popUp.showAtLocation(cardView, Gravity.CENTER,widtho/2,heighto/2);
        popUp.update(0, 0, widtho/2,heighto/2);

        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });

    }

    private void darkenBackground(Float bgcolor) {
        Dictionary mMainActivity = Dictionary.this;
        WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
        lp.alpha = bgcolor;
        mMainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mMainActivity.getWindow().setAttributes(lp);
    }

    public void useMediaPlayer(int rawid, Context context){
        mediaPlayer = MediaPlayer.create(context,rawid);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            }
        });
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