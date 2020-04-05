package com.crezy.fuinykook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    Context context;

    private View view;
    ItemClickListener itemClickListener;

    Button shareBtn,likeBtn;

    int contener=0;
    SharedPreferences sharedPreferences;




    public AdapterViewHolder(@NonNull View itemView) {
        super(itemView);

       view = itemView;



       itemView.setOnClickListener(this);








    }
    void setDialag(final Context context, String title, final String dos , final String like ){
        final TextView titleTv ,dosTv,likeTV;
        final ImageView imageTv, imageTvLike;;


        titleTv = view.findViewById(R.id.titleTv);
        dosTv = view.findViewById(R.id.dosTv);
        imageTv = view.findViewById(R.id.imageTv);
        shareBtn = view.findViewById(R.id.shareBtn);
        likeTV = view.findViewById(R.id.pLikesTv);

        imageTvLike =view.findViewById(R.id.imageTvLike);

        likeBtn= view.findViewById(R.id.likeBtn);



        titleTv.setText(title);
        dosTv.setText(dos);
        likeTV.setText(like+"  اعجابات  ");


       likeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                contener ++;






                sharedPreferences =context.getSharedPreferences("Likes",MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("contener",contener);

                editor.commit();
                likeTV.setText(contener + "   اعجاب   ");

                imageTvLike.setImageResource(R.drawable.ic_hearts_black_24dp);
            }
        });





    }

    @Override
    public void onClick(View v) {

        this.itemClickListener.onItemClickListener(v,getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    // private AdapterViewHolder.ClickListener onclickListener;

   // public interface ClickListener{
    //    void onItemClick(View view,int i);

  //  }

  //  public void setOnClickListener(AdapterViewHolder.ClickListener clickListener){
        //onclickListener =clickListener;


}
