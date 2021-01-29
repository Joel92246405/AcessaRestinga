package com.joel.a0800restinga.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.EuAlugoModel;
import com.joel.a0800restinga.Model.LeisModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EuAlugo;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Leis;

import java.util.ArrayList;
import java.util.List;

public class EuAlugo extends AppCompatActivity implements com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EuAlugo.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter_EuAlugo adapter;
    Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eualugo);

        setSupportActionBar(mTopToolbar);

        RecyclerView recyclerView = findViewById(R.id.recicler_eualugo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}