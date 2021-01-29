package com.joel.a0800restinga.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.Model.InformativosModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Telefones;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Informativos;

import java.util.ArrayList;
import java.util.List;

public class Informativos extends AppCompatActivity implements RecyclerAdapter_Informativos.ItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, descricao;
    ArrayAdapter<String> arrayAdapter;
    InformativosModel informativos;
    RecyclerAdapter_Informativos adapter;
    Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informativos);

        titulo = new ArrayList<String>();
        descricao = new ArrayList<String>();


        setSupportActionBar(mTopToolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("informativos");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_telefones, R.id.titulo_informativos, titulo);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titulo.clear();
                descricao.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    informativos = d.getValue(InformativosModel.class);
                    String texto =informativos.getTitulo();
                    titulo.add(String.valueOf(texto));
                    descricao.add(informativos.getDetalhe());
                }

                RecyclerView recyclerView = findViewById(R.id.recicler_informativos);
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                adapter = new RecyclerAdapter_Informativos(getBaseContext(), titulo, descricao);
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