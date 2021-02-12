package com.joel.a0800restinga.ui.estoudoando;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.joel.a0800restinga.Activities.MeusCadastros;
import com.joel.a0800restinga.Model.EstouDoandoModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EstouDoando;

import java.util.ArrayList;
import java.util.List;

public class EstouDoandoFragment extends Fragment{

    DatabaseReference databaseReference;
    List<String> nome, conteudo, email, telefone, whatsapp;

    EstouDoandoModel estouDoandoModel;
    TextView Nome, Conteudo, Telefone;
    RecyclerAdapter_EstouDoando adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("estoudoando");

        nome = new ArrayList<String>();
        conteudo = new ArrayList<String>();
        email = new ArrayList<String>();
        telefone = new ArrayList<String>();
        whatsapp = new ArrayList<String>();

        final View root = inflater.inflate(R.layout.activity_estoudoando, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab_EstouDoando);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Acesse o Menu Meus Cadastros para inserir a sua doação!", Snackbar.LENGTH_LONG)
                        .setAction("IR AGORA", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent meusCadastros = new Intent(v.getContext(), MeusCadastros.class);
                                startActivity(meusCadastros);
                            }
                        }).show();
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nome.clear();
                    conteudo.clear();
                    email.clear();
                    telefone.clear();
                    whatsapp.clear();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        estouDoandoModel = d.getValue(EstouDoandoModel.class);
                        nome.add(estouDoandoModel.getNome());
                        conteudo.add(estouDoandoModel.getConteudo());
                        email.add(estouDoandoModel.getEmail());
                        telefone.add(estouDoandoModel.getTelefone());
                        whatsapp.add(estouDoandoModel.getWhatsapp());
                    }

                    recyclerView = root.findViewById(R.id.recicler_estoudoando);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_EstouDoando(getContext(), nome, conteudo, telefone, whatsapp, false);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        return root;
    }
/*
    @Override
    public void onItemClick(int position) {

    }

 */
}