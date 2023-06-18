package org.newest.runwords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class DictAdapter extends ArrayAdapter<dictwords>{

    public DictAdapter(@NonNull Context context, ArrayList<dictwords> dictwordslist) {
        super(context, 0, dictwordslist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.wordcard, parent, false);
        }

        //Edit Text Size and Alphabetic Order





        dictwords dictwords = getItem(position);
        TextView textcard = listitemView.findViewById(R.id.textcard);
        TextView textcard2 = listitemView.findViewById(R.id.textcard2);
        ImageView cardsound = listitemView.findViewById(R.id.cardsound);
        CardView anacard = listitemView.findViewById(R.id.anacard);




       /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
           WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        double width = displayMetrics.widthPixels;
  anacard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (width/2)));
        textcard.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)(width/18));
        textcard2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)(width/25));
        cardsound.setPadding((int) (width/90),(int)width/90,(int)width/90,(int)width/90);
        */

        textcard.setText(dictwords.getEngwords());
        textcard2.setText(dictwords.getTrwords());
        cardsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yoyoclas(Techniques.Bounce,500,0,view);
                ((Dictionary)getContext()).useMediaPlayer(dictwords.getRawid(),view.getContext());

            }
        });

       /*
        anacard.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
                //Dictionaryde popup açtır.
                ((Dictionary)getContext()).openPopup(dictwords.getEngwords(),dictwords.getTrwords(),dictwords.getRawid());
            }
        });
        */


        return listitemView;
    }

    private void yoyoclas(Techniques name, int dur, int rep, View v){
        YoYo.with(name)
                .duration(dur)
                .repeat(rep)
                .playOn(v);


    }

}
