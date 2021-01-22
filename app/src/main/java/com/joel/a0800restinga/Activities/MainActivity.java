package com.joel.a0800restinga.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.MyRecyclerViewAdapter;
import com.joel.a0800restinga.Model.Users;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item, whatsapp;
    ArrayAdapter<String> arrayAdapter;
    Users user;
    MyRecyclerViewAdapter adapter;
    Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        whatsapp= new ArrayList<String>();


        setSupportActionBar(mTopToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("telefones");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list, R.id.txtnome, titulo);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titulo.clear();
                item.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    user = d.getValue(Users.class);
                    String texto =user.getTel();
                    titulo.add(String.valueOf(texto));
                    item.add(user.getEnd());
                    whatsapp.add(user.getWhatsapp());

                }

                RecyclerView recyclerView = findViewById(R.id.recicler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                adapter = new MyRecyclerViewAdapter(MainActivity.this, getBaseContext(), titulo, item, whatsapp);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}