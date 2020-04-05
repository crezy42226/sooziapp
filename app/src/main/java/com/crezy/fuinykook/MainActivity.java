package com.crezy.fuinykook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editText, etd;
    private Button button,shareBtn;
    int contener=0;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    RecyclerView recyclerView;

    List<ModelAddPost>list;

    String name ,dos;

    Button goBte;

    ImageView imageTvLike;

    private AdView mAdView;


    EditText titleTv;

    SharedPreferences sharedPreferences,sharedPreferences1;

    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3677695007403196~1602334785");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        ref = FirebaseDatabase.getInstance().getReference("posts");
        imageTvLike = findViewById(R.id.imageTvLike);


        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("الرئيسية");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        sharedPreferences = getSharedPreferences("SortSetting",MODE_PRIVATE);
        String sortString = sharedPreferences.getString("Sort","new");

        if (sortString.equals("new")){

            layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);

        }
        if (sortString.equals("oldest")){

            layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(false);
            layoutManager.setStackFromEnd(false);

        }

        recyclerView = findViewById(R.id.recy_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        shareBtn = findViewById(R.id.shareBtn);


        loadData();

    }

    private void loadData() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");

        FirebaseRecyclerOptions<ModelAddPost> options =
                new FirebaseRecyclerOptions.Builder<ModelAddPost>().setQuery(query,ModelAddPost.class).build();

       adapter = new FirebaseRecyclerAdapter<ModelAddPost, AdapterViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull AdapterViewHolder holder, final int position, @NonNull ModelAddPost model) {



               holder.setDialag(getApplicationContext(),model.getTitle(),model.getDos(),model.getLike());

               sharedPreferences = getSharedPreferences("Likes",MODE_PRIVATE);
               contener = sharedPreferences.getInt("contener",0);


             holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     String dooss = getItem(position).getTitle();
                     Intent intent = new Intent(Intent.ACTION_SEND);
                     intent.setType("text/plan");
                     intent.putExtra(Intent.EXTRA_SUBJECT,"مشاركة النطته");
                     intent.putExtra(Intent.EXTRA_TEXT,dooss);
                     startActivity(intent);
                 }
             });


               holder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onItemClickListener(View view, int position) {
                       String title = getItem(position).getTitle();
                       String doos = getItem(position).getDos();
                       String like = getItem(position).getLike();


                       Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                       intent.putExtra("title",title);
                       intent.putExtra("dos",doos);
                       intent.putExtra("like",like);
                       startActivity(intent);
                   }
               });





           }

           @NonNull
           @Override
           public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

               final View itemView = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.row_list, parent, false);

               AdapterViewHolder adapterViewHolder = new AdapterViewHolder(itemView);

               return new AdapterViewHolder (itemView);

           }
       };
       recyclerView.setLayoutManager(layoutManager);
       adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem =menu.findItem(R.id.searchA);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                seachShow(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                seachShow(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.additem){


            addData();
        }
        if (id==R.id.sortitem){
            showSortItem();

            return true;
        }
        if (id == R.id.searchA) {



        }


        return super.onOptionsItemSelected(item);
    }

    private void seachShow(String searchText) {



        String stringquri = searchText.toLowerCase();
        Query query =ref.orderByChild("dos").limitToFirst(100).endAt(stringquri+"\uf0ff");



        FirebaseRecyclerOptions<ModelAddPost> options =
                new FirebaseRecyclerOptions.Builder<ModelAddPost>().setQuery(query,ModelAddPost.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelAddPost, AdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdapterViewHolder holder, final int position, @NonNull ModelAddPost model) {



                holder.setDialag(getApplicationContext(),model.getTitle(),model.getDos(),model.getLike());

                sharedPreferences = getSharedPreferences("Likes",MODE_PRIVATE);
                contener = sharedPreferences.getInt("contener",0);



                holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String dooss = getItem(position).getTitle();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plan");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"مشاركة النكت");
                        intent.putExtra(Intent.EXTRA_TEXT,dooss);
                        startActivity(intent);
                    }
                });


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                        String title = getItem(position).getTitle();
                        String doos = getItem(position).getDos();
                        String like = getItem(position).getLike();


                        Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("dos",doos);
                        intent.putExtra("like",like);
                        startActivity(intent);
                    }
                });





            }

            @NonNull
            @Override
            public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

                final View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_list, parent, false);

                AdapterViewHolder adapterViewHolder = new AdapterViewHolder(itemView);









                return new AdapterViewHolder (itemView);

            }
        };
        recyclerView.setLayoutManager(layoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }

    private void addData() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("شارك نكته جديدة");
        final View view = getLayoutInflater().inflate(R.layout.row_aadditeme,null);
        builder.setView(view);
        builder.setPositiveButton("ارسال", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editTitle = view.findViewById(R.id.titleEdit);
                EditText editdos = view.findViewById(R.id.dosEdit);
                String message = "text";

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("posts").push();
                Map<String, Object> map = new HashMap<>();
                map.put("id", databaseReference.getKey());
                map.put("title", editTitle.getText().toString());
                map.put("dos", editdos.getText().toString());
                map.put("likess", "0");
                map.put("search", " ");


                databaseReference.setValue(map);

                NotificationCompat.Builder builder1 = new NotificationCompat.Builder( MainActivity.this)
                        .setSmallIcon(R.drawable.ic_heart_black_24dp)
                        .setContentTitle("test")
                        .setContentText(message)
                        .setAutoCancel(true);

                NotificationManager notificationManager=  (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );
                notificationManager.notify(0,builder1.build());


            }
        }).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        })
        ;
        builder.create();
        builder.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void showSortItem() {

        String[] sort= {"الجديد","القديم"};

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("عرض حسب").setIcon(R.drawable.ic_sort_by_alpha_black_24dp)
                .setItems(sort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which==0){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort","new");
                            editor.apply();
                            recreate();

                        }


                        if (which==1){

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();
                            recreate();


                        }



                    }
                });

        builder.show();

    }


}



