package com.crezy.fuinykook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    int contener=0;


    EditText nameTv ,ageTv;
    Button regsterBte,likeBte,sharebte;


    List<ModelAddPost>modelAddPosts;

    TextView titleTv ,dosTv,likeTv;
    ImageView imageTv,imageLike;
    String likes;

    SharedPreferences sharedPreferences;

    private AdView mAdView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        MobileAds.initialize(this, "ca-app-pub-3677695007403196~1602334785");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = getSharedPreferences("Likes",MODE_PRIVATE);
        contener = sharedPreferences.getInt("contener",0);

        titleTv = findViewById(R.id.titleTv);
        dosTv = findViewById(R.id.dosTv);
        likeTv = findViewById(R.id.pLikesTv);
        imageTv =findViewById(R.id.imageTv);
        likeBte = findViewById(R.id.likeBtn);
        sharebte = findViewById(R.id.shareBtn);
        imageLike = findViewById(R.id.imageTvLike);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("اضحك واسعج من حولك");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        String title = getIntent().getStringExtra("title");
        String dos = getIntent().getStringExtra("dos");
        String like = getIntent().getStringExtra("like");

        titleTv.setText(title);
        dosTv.setText(dos);
       likeTv.setText(like+"  اعجابات   ");



        sharebte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dooss = dosTv.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT,"مشاركة النطته");
                intent.putExtra(Intent.EXTRA_TEXT,dooss);
                startActivity(intent);
            }
        });

        likeBte.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                contener ++;




                sharedPreferences = getSharedPreferences("Likes",MODE_PRIVATE);




                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("contener",contener);

                editor.commit();


                likeTv.setText(Integer.toString(contener)+ "   اعجاب   ");
                imageLike.setImageResource(R.drawable.ic_hearts_black_24dp);




            }
        });


    }




}
