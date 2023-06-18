package org.newest.runwords;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class userAdapter extends RecyclerView.Adapter<userAdapter.MyViewHolder> {

    ArrayList<userData> userDataArrayList;
    LayoutInflater layoutInflater;

    public userAdapter(Context context, ArrayList<userData> userDataArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.userDataArrayList = userDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.server_user_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        userData selectedProduct = userDataArrayList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        CardView item_cardView;

        String url = "https://www.xn--mziksayfas-9db95d.com/runword.php";


        TextView actionText;

        CardView allCardview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.usernames);
            item_cardView = (CardView) itemView.findViewById(R.id.useraction);
            allCardview = (CardView) itemView.findViewById(R.id.allcardlay);
            actionText = (TextView) itemView.findViewById(R.id.userActionText);

            productName.setTag(R.string.bastex, "bastex");
            actionText.setTag(R.string.bastex, "bastex");
            MainActivity.getmInstanceActivity().ViewEditor(productName, 15, 15);
            MainActivity.getmInstanceActivity().ViewEditor(actionText, 25, 25);
            MainActivity.getmInstanceActivity().ViewHeight(allCardview, 10);




          actionText.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                  if (actionText.getText().toString().equalsIgnoreCase("Davet\nGönder")){
                      item_cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.serveruser,null));

                      RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                      StringRequest request = new StringRequest(Request.Method.POST, url,
                              response -> {
                                  response = response.trim();
                                  if (response.equalsIgnoreCase("already")){
                                      Toast.makeText(v.getContext(), "Zaten Davetiniz Var... İstekler ve Odalar butonlarını kontrol edin", Toast.LENGTH_LONG).show();
                                  } else {
                                      MainActivity.getmInstanceActivity().refreshServerPopup(v.getContext(),"istek");
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
                              params.put("userone",databaseHelper.getCheckUsers().get(0).getUsername());
                              params.put("usertwo",productName.getText().toString());
                              return params;
                          }

                          @Nullable
                          @Override
                          public Response.ErrorListener getErrorListener() {

                              return super.getErrorListener();
                          }
                      };

                      requestQueue.add(request);
                  } else if (actionText.getText().toString().equalsIgnoreCase("Davet\nBekleniyor")){
                      Toast.makeText(v.getContext(), "Kullanıcının kabul etmesi bekleniyor...", Toast.LENGTH_SHORT).show();
                  } else if (actionText.getText().toString().equalsIgnoreCase("Daveti\nKabul Et")){
                      RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                      StringRequest request = new StringRequest(Request.Method.POST, url,
                              response -> {
                                  response = response.trim();
                                  if (response.equalsIgnoreCase("succ")){
                                      Toast.makeText(v.getContext(), "Çalışma Odasına Girebilirsiniz...", Toast.LENGTH_SHORT).show();
                                      MainActivity.getmInstanceActivity().refreshServerPopup(v.getContext(),"oda");
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
                              params.put("useronek",databaseHelper.getCheckUsers().get(0).getUsername());
                              params.put("usertwok",productName.getText().toString());
                              return params;
                          }

                          @Nullable
                          @Override
                          public Response.ErrorListener getErrorListener() {

                              return super.getErrorListener();
                          }
                      };

                      requestQueue.add(request);
                  } else if (actionText.getText().toString().equalsIgnoreCase("Odaya\nKatıl")){
                      RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                      StringRequest request = new StringRequest(Request.Method.POST, url,
                              response -> {
                                  response = response.trim();
                                  if (response.equalsIgnoreCase("succ")){
                                      Toast.makeText(v.getContext(), "Çalışma Odası Hazırlanıyor...", Toast.LENGTH_SHORT).show();
                                      Intent intent = new Intent(v.getContext(),RoomedAct.class);
                                      intent.putExtra("user",productName.getText().toString());
                                      v.getContext().startActivity(intent);
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
                              params.put("useroneod",databaseHelper.getCheckUsers().get(0).getUsername());
                              params.put("usertwood",productName.getText().toString());
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
          });
        }

        public void setData(userData selectedProduct, int position) {
            this.productName.setText(selectedProduct.getName());
            this.actionText.setText(selectedProduct.getButtext());

            if (this.actionText.getText().toString().equalsIgnoreCase("Davet\nGönder")){
                this.item_cardView.setCardBackgroundColor(this.itemView.getContext().getResources().getColor(R.color.serveruser,null));
            } else if (actionText.getText().toString().equalsIgnoreCase("Davet\nBekleniyor")){
                this.item_cardView.setCardBackgroundColor(this.itemView.getContext().getResources().getColor(R.color.serverwaited,null));
            } else if (this.actionText.getText().toString().equalsIgnoreCase("Daveti\nKabul Et")){
                this.item_cardView.setCardBackgroundColor(this.itemView.getContext().getResources().getColor(R.color.serverwaitedfrom,null));
            } else if (this.actionText.getText().toString().equalsIgnoreCase("Odaya\nKatıl")){
                this.item_cardView.setCardBackgroundColor(this.itemView.getContext().getResources().getColor(R.color.purple_200,null));
            }

        }

    }
}
