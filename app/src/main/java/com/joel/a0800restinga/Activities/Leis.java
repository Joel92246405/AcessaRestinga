package com.joel.a0800restinga.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.Model.LeisModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Leis;

import java.util.ArrayList;
import java.util.List;

public class Leis extends AppCompatActivity implements com.joel.a0800restinga.MyRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;




    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, descricao;
    ArrayAdapter<String> arrayAdapter;
    LeisModel leis;
    RecyclerAdapter_Leis adapter;
    Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leis);

        titulo = new ArrayList<String>();
        descricao = new ArrayList<String>();


        setSupportActionBar(mTopToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("leis");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list, R.id.titulo_leis, titulo);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titulo.clear();
                descricao.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    leis = d.getValue(LeisModel.class);
                    String texto =leis.getTitulo();
                    titulo.add(String.valueOf(texto));
                    descricao.add(leis.getDetalhe());
                }

                //RecyclerView recyclerView = findViewById(R.id.recicler_leis);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                adapter = new RecyclerAdapter_Leis(getBaseContext(), titulo, descricao);
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