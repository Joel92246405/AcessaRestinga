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
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Telefones;
import com.joel.a0800restinga.Model.TelefonesModel;

import java.util.ArrayList;
import java.util.List;

public class Telefones extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item, whatsapp;
    ArrayAdapter<String> arrayAdapter;
    TelefonesModel user;
    RecyclerAdapter_Telefones adapter;
    Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefones);


        setSupportActionBar(mTopToolbar);

        RecyclerView recyclerView = findViewById(R.id.recicler_telefones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);


    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
/*
    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

 */
}