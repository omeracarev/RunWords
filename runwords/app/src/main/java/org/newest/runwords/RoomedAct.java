package org.newest.runwords;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RoomedAct extends AppCompatActivity {
    private Bundle extras;
    private TextView userTitle,exitRoom,statusText;

    private CardView card1Room,card2Room,card3Room,card4Room;
    private ArrayList<CardView> cards = new ArrayList<>();
    private TextView engword;
    private ImageView playsoundroom;
    private String[] trlist = new allwords().trwords;
    private int[] rawlist = new allwords().voices;
    private Handler handler;
    private Runnable runnable;

    private String oldTexter = "oldtext";
    private String newTexter = "newText";

    private Boolean status = false;

    private String mainText = "";
    private String mainText2 = "";

    private EditText messagebox;

    private ImageView sendmesimg;

    private LinearLayout addMesScroll;

    int glowidtho,gloheighto;

    private boolean isEditFocus = false;

    private ScrollView scrollroomed;


   private String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";

   private DatabaseHelper databaseHelper;

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
        setContentView(R.layout.activity_roomed);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         glowidtho = displayMetrics.widthPixels;
        gloheighto = displayMetrics.heightPixels;


        extras = getIntent().getExtras();
        userTitle = findViewById(R.id.userTitle);
        exitRoom = findViewById(R.id.exitRoom);
        statusText = findViewById(R.id.statusText);
        playsoundroom = findViewById(R.id.playsoundroom);
        engword = findViewById(R.id.engwordroom);
        card1Room = findViewById(R.id.card1room);
        card2Room = findViewById(R.id.card2room);
        card3Room = findViewById(R.id.card3room);
        card4Room = findViewById(R.id.card4room);
        scrollroomed = findViewById(R.id.scrollroomed);
        cards.add(card1Room);
        cards.add(card2Room);
        cards.add(card3Room);
        cards.add(card4Room);
        messagebox = findViewById(R.id.messagebox);
        userTitle.setTag(R.string.bastex,"bastex");
        exitRoom.setTag(R.string.bastex,"bastex");
        statusText.setTag(R.string.bastex,"bastex");
        messagebox.setTag(R.string.bastex,"basedit");
        messagebox.setTag("basedit");
        ViewEditor(messagebox,18,18);
        ViewEditor(userTitle,20,20);
        ViewEditor(exitRoom,16,16);
        ViewEditor(statusText,23,23);
        userTitle.setText(extras.getString("user"));
        databaseHelper = new DatabaseHelper(RoomedAct.this);
        addMesScroll = findViewById(R.id.addMesScroll);
        sendmesimg = findViewById(R.id.sendmesimg);




        exitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initLayout(RoomedAct.this);
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                initLayout(RoomedAct.this);
                handler.postDelayed(this,5000);
            }
        },5000);

        sendmesimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateChat(databaseHelper.getCheckUsers().get(0).getUsername(),extras.getString("user"),"user//"+databaseHelper.getCheckUsers().get(0).getUsername()+"//"+messagebox.getText().toString(),v.getContext());
                messagebox.setText("");
                changeSendImg(v.getContext());
            }
        });


    }


    private void gelenMes(String message){
        CardView cardView = (CardView) CardView.inflate(RoomedAct.this,R.layout.card_gelmes,null);
        ImageView sendmesimg = cardView.findViewById(R.id.gelmesimg);
        TextView sendmestext = cardView.findViewById(R.id.gelmestext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(glowidtho-glowidtho/4, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.START;
        cardView.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams imglaypar = new LinearLayout.LayoutParams(glowidtho/9,glowidtho/9);
        imglaypar.gravity = Gravity.END|Gravity.CENTER_VERTICAL;
        imglaypar.setMargins(glowidtho/90,glowidtho/90,glowidtho/90,glowidtho/90);
        sendmesimg.setLayoutParams(imglaypar);
        sendmestext.setTag(R.string.bastex,"bastex");
        ViewEditor(sendmestext,20,20);
        sendmestext.setText(message);

        addMesScroll.addView(cardView);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
    }

    private void sendMes(String message){
        CardView cardView = (CardView) CardView.inflate(RoomedAct.this,R.layout.card_sendmes,null);
        ImageView sendmesimg = cardView.findViewById(R.id.sendmesimg);
        LinearLayout cardsendmeslin = cardView.findViewById(R.id.cardsendmeslin);

        TextView sendmestext = cardView.findViewById(R.id.sendmestext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(glowidtho-glowidtho/4, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        cardView.setLayoutParams(layoutParams);
         LinearLayout.LayoutParams imglaypar = new LinearLayout.LayoutParams(glowidtho/9,glowidtho/9);
        imglaypar.gravity = Gravity.END|Gravity.CENTER_VERTICAL;

        //sendmestext ayar cek
        sendmestext.setLayoutParams(new LinearLayout.LayoutParams((glowidtho-glowidtho/4)-glowidtho/7, LinearLayout.LayoutParams.WRAP_CONTENT));
        sendmestext.setTag(R.string.bastex,"bastex");
        ViewEditor(sendmestext,20,20);
        imglaypar.setMargins(glowidtho/90,glowidtho/90,glowidtho/90,glowidtho/90);
        sendmesimg.setLayoutParams(imglaypar);

        sendmestext.setText(message);


        addMesScroll.addView(cardView);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);


    }

    private void statusCard(String message){
       CardView cardView = (CardView) CardView.inflate(RoomedAct.this,R.layout.infomeslay,null);

       TextView wintext = cardView.findViewById(R.id.wintext);
       wintext.setTag(R.string.bastex,"bastex");
       ViewEditor(wintext,25,25);


       wintext.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(glowidtho-glowidtho/4, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        cardView.setLayoutParams(layoutParams);


        addMesScroll.addView(cardView);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        runnable = null;
    }

    private void yoyoclas(Techniques name, int dur, int rep, View v){
        if (checkInfo()){
            YoYo.with(name)
                    .duration(dur)
                    .repeat(rep)
                    .playOn(v);
        }
    }

    private void initParticuleEfect(int maxParticles,int timetToLive,float speedMin,float speedMax,int minAngle,int maxAngle,View view,int numParticles,int drawKonfet){
        if (checkInfo()){
            new ParticleSystem(RoomedAct.this,maxParticles,drawKonfet,timetToLive)
                    .setSpeedModuleAndAngleRange(speedMin, speedMax, minAngle, maxAngle)
                    .oneShot(view, numParticles);
        }


    }

    private void changeText(CardView cardView, String user1, String user2){
        Context context = cardView.getContext();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    response = response.trim();
                    try{
                        initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardView,50,R.drawable.ic_corkonfet);
                        new Dictionary().useMediaPlayer(R.raw.correcttune,RoomedAct.this);
                        initLayout(context);
                    } catch (Exception e) {

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
                params.put("userroom1coni",user1);
                params.put("userroom2coni",user2);
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

    private void changeSendImg(Context context){
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,3));
       LinearLayout mess = findViewById(R.id.messageboxlay);
       mess.removeView(sendmesimg);
       mess.addView(progressBar);
    }

    private void updateChat(String user1, String user2, String chatword,Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    response = response.trim();
                    try{
                        if (response.equalsIgnoreCase("succ")){
                            if (chatword.contains("/stats/")){
                                statusCard(chatword.substring(7));
                                scrollroomed.fullScroll(View.FOCUS_DOWN);
                            } else if (chatword.contains("user//"+databaseHelper.getCheckUsers().get(0).getUsername())){
                                LinearLayout linearLayout = findViewById(R.id.messageboxlay);
                                linearLayout.removeViewAt(1);
                                linearLayout.addView(sendmesimg);
                                sendMes(chatword.split("//")[2]);
                                scrollroomed.fullScroll(View.FOCUS_DOWN);

                            }


                        } else {

                        }
                    } catch (Exception e) {

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
                params.put("chatuser1",user1);
                params.put("chatuser2",user2);
                params.put("chatword",chatword);
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

    private void controlText(CardView cardView, String user1, String user2,String engword){
        Context context = cardView.getContext();
        TextView textView = (TextView) cardView.getChildAt(0);
        String controlText = cardView.getTag(R.string.cards).toString();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    response = response.trim();
                    try{
                       if (response.equalsIgnoreCase("turn")){
                          changeText(cardView,user1,user2);
                           statusText.setText("Sıra Sizde");
                           status = true;
                           String[] conturkce = controlText.split("-");
                           updateChat(user1,user2,"/stats/"+engword+"="+conturkce[0]+"\n"+user1+" Bildi",context);
                       } else {
                          statusText.setText("Sıra Karşı Tarafta");
                           status = false;
                           initParticuleEfect(50,1200,0.2f,0.5f,0,270,cardView,50,R.drawable.ic_wrongkonfet);
                           new Dictionary().useMediaPlayer(R.raw.incorrect,cardView.getContext());
                       }
                    } catch (Exception e) {

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
                params.put("userroom1con",user1);
                params.put("userroom2con",user2);
                params.put("controlText",controlText);
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

    private boolean checkIsAnimate(String oldText, String newText){
        if (oldText.equalsIgnoreCase(newText)){

            return false;
        } else {
            oldTexter = newText;
            return true;
        }

    }
    private void initLayout(Context context){


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    response = response.trim();
                    try{
                        String[] texto = response.split("///");
                        String[] engmerge = texto[0].split("-");
                        String[] tr1merge = texto[1].split("-");
                        String[] tr2merge = texto[2].split("-");
                        String[] tr3merge = texto[3].split("-");
                        String[] tr4merge = texto[4].split("-");
                        String[] tr6merge = texto[6].split("-");
                        String[] tr7merge = texto[7].split("-");
                        String[] tr8merge = texto[8].split("/c/c/");

                        addMesScroll.removeAllViews();
                        if (!tr8merge[0].equalsIgnoreCase("empty")){
                            int lengo = tr8merge.length;
                            if (tr8merge.length>=10){
                                for (int c=lengo-10; c<lengo; c++){
                                    if (tr8merge[c].contains("/stats/")){
                                        statusCard(tr8merge[c].substring(7));
                                    } else if (tr8merge[c].contains("user//"+databaseHelper.getCheckUsers().get(0).getUsername()+"//")){
                                        sendMes(tr8merge[c].split("//")[2]);
                                    }  else if (tr8merge[c].contains("user//"+extras.getString("user")+"//")){
                                        gelenMes(tr8merge[c].split("//")[2]);
                                    }

                                }
                            } else {
                                for (int c=0; c<lengo; c++){
                                    if (tr8merge[c].contains("/stats/")){
                                        statusCard(tr8merge[c].substring(7));
                                    } else if (tr8merge[c].contains("user//"+databaseHelper.getCheckUsers().get(0).getUsername()+"//")){
                                        sendMes(tr8merge[c].split("//")[2]);
                                    }  else if (tr8merge[c].contains("user//"+extras.getString("user")+"//")){
                                        gelenMes(tr8merge[c].split("//")[2]);
                                    }

                                }
                            }


                        }
                        scrollroomed.fullScroll(View.FOCUS_DOWN);

                        mainText = tr6merge[0];
                        mainText2 = tr7merge[0];

                        if (texto[5].equalsIgnoreCase("turn")){
                            statusText.setText("Sıra Sizde");
                            status = true;

                        } else {
                            statusText.setText("Sıra Karşı Tarafta");
                            status = false;

                        }

                        String[] allsik = new String[]{tr1merge[0],tr2merge[0],tr3merge[0],tr4merge[0]};
                        engword.setText(engmerge[0]);

                        for (int i=0; i<rawlist.length; i++){
                            if (Integer.parseInt(engmerge[1])==i){
                                if (checkIsAnimate(oldTexter,newTexter)){
                                    new Dictionary().useMediaPlayer(rawlist[i],context);
                                }

                                int finalI = i;
                                playsoundroom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new Dictionary().useMediaPlayer(rawlist[finalI],v.getContext());
                                        yoyoclas(Techniques.Shake,500,0,v);

                                    }
                                });
                            }
                        }

                        for (int i=0; i<cards.size(); i++){
                            TextView textView = (TextView) cards.get(i).getChildAt(0);
                            textView.setTag(R.string.bastex,"bastex");
                            ViewEditor(textView,20,20);
                            textView.setText(allsik[i]);
                            if (checkIsAnimate(oldTexter,newTexter)){
                                yoyoclas(Techniques.FlipInY,1000,0,cards.get(0));
                                yoyoclas(Techniques.FlipInY,1000,0,cards.get(1));
                                yoyoclas(Techniques.FlipInY,1000,0,cards.get(2));
                                yoyoclas(Techniques.FlipInY,1000,0,cards.get(3));


                            }

                            if (textView.getText().toString().equalsIgnoreCase(mainText) || textView.getText().toString().equalsIgnoreCase(mainText2)){
                                textView.setBackgroundColor(Color.RED);
                                textView.setTextColor(Color.WHITE);
                                initParticuleEfect(50,1200,0.2f,0.5f,0,270,textView,50,R.drawable.ic_wrongkonfet);


                            } else  {
                                textView.setBackgroundColor(Color.TRANSPARENT);
                                textView.setTextColor(Color.BLACK);
                            }

                            newTexter = engmerge[0];

                            cards.get(i).setTag(R.string.cards,texto[i+1]);

                            if (status){
                                cards.get(0).setEnabled(true);
                                cards.get(1).setEnabled(true);
                                cards.get(2).setEnabled(true);
                                cards.get(3).setEnabled(true);
                            } else {
                                cards.get(0).setEnabled(false);
                                cards.get(1).setEnabled(false);
                                cards.get(2).setEnabled(false);
                                cards.get(3).setEnabled(false);
                            }

                            cards.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cards.get(0).setEnabled(false);
                                    cards.get(1).setEnabled(false);
                                    cards.get(2).setEnabled(false);
                                    cards.get(3).setEnabled(false);
                                   controlText((CardView) v,databaseHelper.getCheckUsers().get(0).getUsername(),extras.getString("user"),engword.getText().toString());

                                }
                            });
                        }

                    } catch (Exception e) {

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
                params.put("userroom1",databaseHelper.getCheckUsers().get(0).getUsername());
                params.put("userroom2",extras.getString("user"));
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

    public void ViewEditor(@NonNull View view, int olc1, int olc2){
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