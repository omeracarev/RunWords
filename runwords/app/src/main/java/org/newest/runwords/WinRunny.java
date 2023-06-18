package org.newest.runwords;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.plattysoft.leonids.ParticleSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WinRunny extends AppCompatActivity {
    private ImageView mainrunnywin,sureimage,playquiz;
    private TextView idletextwin,suretext,hourremaining,quitquiz;
    private LinearLayout engwordtami,harflay,harflay2;

    private int boscardsay=0;
    private int winningrunny = 0;

    private DatabaseHelper databaseHelper;

    ArrayList engwords = new ArrayList<>(Arrays.asList(new allwords().engwords));

    private boolean checkInfo(){
        int dpi = getResources().getDisplayMetrics().densityDpi;
        if (dpi<440){
            return false;
        } else {
            return true;
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_runny);

        mainrunnywin = findViewById(R.id.mainrunnywin);
        sureimage = findViewById(R.id.sureimage);
        playquiz = findViewById(R.id.playquiz);
        idletextwin = findViewById(R.id.idletextwin);
        suretext = findViewById(R.id.suretext);
        hourremaining = findViewById(R.id.hourremaining);
        quitquiz = findViewById(R.id.quitquiz);

        databaseHelper = new DatabaseHelper(WinRunny.this);


        idletextwin.setTag(R.string.bastex,"bastex");
        suretext.setTag(R.string.bastex,"bastex");
        hourremaining.setTag(R.string.bastex,"bastex");
        quitquiz.setTag(R.string.bastex,"bastex");

        ViewEditor(idletextwin,15,15);
        ViewEditor(suretext,20,20);
        ViewEditor(hourremaining,11,11);
        ViewEditor(quitquiz,15,15);

        List<Ads> adsList = databaseHelper.getAds();

        quitquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                Toast.makeText(WinRunny.this, winningrunny+" Runny Kazanıldı!", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();

            }
        });

        final int[] time = {15};
        new CountDownTimer(time[0]*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                hourremaining.setText(checkDigit(time[0])+" sn");
                time[0]--;
            }

            public void onFinish() {
                Ads ads = new Ads(-1,0+"",adsList.get(0).getAd_date(),adsList.get(0).getAd_type());
                if (!databaseHelper.deleteAds(adsList.get(0))){
                    if (databaseHelper.AddAds(ads)){
                        hourremaining.setText("Süre Bitti!");
                        hourremaining.setTextColor(Color.RED);
                        Toast.makeText(WinRunny.this, winningrunny+" Runny Kazanıldı!", Toast.LENGTH_SHORT).show();
                        finishAndRemoveTask();
                    }
                }





            }

        }.start();

        initializeİdleAnim(hourremaining,Techniques.Wobble);
        initializeİdleAnim(suretext,Techniques.SlideInLeft);
        initializeİdleAnim(sureimage,Techniques.Tada);
        initializeİdleAnim(mainrunnywin,Techniques.Tada);
        initializeİdleAnim(playquiz,Techniques.Bounce);
        initializeİdleAnim(idletextwin,Techniques.SlideInLeft);

        initializeLayout(WinRunny.this);



    }

    private void initParticuleEfect(int maxParticles,int timetToLive,float speedMin,float speedMax,int minAngle,int maxAngle,View view,int numParticles,int drawKonfet){
        if (checkInfo()){
            new ParticleSystem(WinRunny.this,maxParticles,drawKonfet,timetToLive)
                    .setSpeedModuleAndAngleRange(speedMin, speedMax, minAngle, maxAngle)
                    .oneShot(view, numParticles);
        }

    }

    private void dropcard(CardView cardView){
        cardView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DROP:
                       ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                       String[] bol = item.getText().toString().split("///");
                       if (view.getTag(R.string.wintext).toString().trim().equalsIgnoreCase(bol[0].trim())){
                           findViewById(Integer.parseInt(bol[1])).setVisibility(View.INVISIBLE);
                           cardView.setCardBackgroundColor(Color.parseColor("#e9c670"));
                           TextView textView = (TextView) cardView.getChildAt(0);
                           textView.setVisibility(View.VISIBLE);
                           new Dictionary().useMediaPlayer(R.raw.correcttune,view.getContext());
                           initParticuleEfect(50,1200,0.2f,0.5f,0,270,textView,50,R.drawable.ic_corkonfet);
                           boscardsay--;
                           if (boscardsay==0){
                               initializeLayout(WinRunny.this);
                               initParticuleEfect(50,1200,0.2f,0.5f,0,270,mainrunnywin,50,R.drawable.ic_runnykonfet);
                               suretext.setText("Yaşasın! Bir Runny daha kazandın!\nKalan Süre:");
                               winningrunny++;
                               List<Currents> currents = databaseHelper.getCurrents();
                               Currents newcur = new Currents(-1,currents.get(0).getCorcount(),currents.get(0).getWrocount(),(Integer.parseInt(currents.get(0).getRuncount())+1)+"");
                               if (!databaseHelper.deleteCurrents(currents.get(0))){
                                   if (databaseHelper.AddCurrents(newcur)){
                                       Toast.makeText(WinRunny.this, "+1 Runny Eklendi!", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }
                       } else {
                           new Dictionary().useMediaPlayer(R.raw.incorrect,view.getContext());
                       }

                        break;
                }
                return true;
            }
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

    private void playWord(int rawid){
        new Dictionary().useMediaPlayer(rawid,WinRunny.this);
    }

    private void initializeLayout(Context context){
        engwordtami = findViewById(R.id.engwordtami);
        harflay = findViewById(R.id.harflay);
        harflay2 = findViewById(R.id.harflay2);

        engwordtami.removeAllViews();
        harflay.removeAllViews();
        harflay2.removeAllViews();

        ArrayList newlist = new ArrayList<String>();
        for (int i=0; i<engwords.size(); i++){
            if (engwords.get(i).toString().replace(" ","").trim().length()<9){
                newlist.add(engwords.get(i).toString().replaceAll(" ","").trim().toUpperCase(Locale.ROOT));

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



        playWord(new allwords().voices[Integer.parseInt(ekstrdizo[1])]);

        playquiz.setOnClickListener(new View.OnClickListener() {
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

        ArrayList<String> olcakharf = new ArrayList<String>();
        for (int i=0; i<ekstrdiz.size(); i++){
            for (int j=0; j<alphabet.size(); j++){

                if (ekstrdiz.get(i).equalsIgnoreCase(alphabet.get(j))){
                    olcakharf.add(alphabet.get(j));
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
            textView.setTypeface(ResourcesCompat.getFont(context,R.font.didact_gothic),Typeface.BOLD);
            textView.setTextColor(Color.parseColor("#6200EA"));
            cardView.addView(textView);

            if (i!=num1 && i!=num2){
                textView.setVisibility(View.INVISIBLE);
                cardView.setCardBackgroundColor(Color.parseColor("#7f7681"));
                boscardsay++;
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

    private void yoyoclas(Techniques name, int dur, int rep, View v){
        if (checkInfo()){
            YoYo.with(name)
                    .duration(dur)
                    .repeat(rep)
                    .playOn(v);
        }



    }

    private void initializeİdleAnim(View view, Techniques techniques){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,30000);
                yoyoclas(techniques,2000,0,view);
            }
        };
        handler.postDelayed(runnable,10000);

    }


    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
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

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        finishAndRemoveTask();
        super.onDestroy();
    }
}