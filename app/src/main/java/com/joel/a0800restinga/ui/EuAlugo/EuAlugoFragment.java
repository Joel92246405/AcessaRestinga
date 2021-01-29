package com.joel.a0800restinga.ui.EuAlugo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.joel.a0800restinga.Model.EuAlugoModel;


import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EuAlugo;

import java.util.ArrayList;
import java.util.List;

public class EuAlugoFragment extends Fragment implements RecyclerAdapter_EuAlugo.ItemClickListener,  AdapterView.OnItemSelectedListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    DatabaseReference databaseReference;
    List<String> Nome, Telefone, TipodeContrato, Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria;
    ArrayAdapter<String> arrayAdapter;
    EuAlugoModel euAlugoModel;
    RecyclerAdapter_EuAlugo adapter;
    Spinner spinner;
    Button TenhoCasa, ConhecoQuemTenha;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Nome = new ArrayList<String>();
        Telefone = new ArrayList<String>();
        TipodeContrato = new ArrayList<String>();
        Valor = new ArrayList<String>();
        WhatsApp = new ArrayList<String>();
        Titulo = new ArrayList<String>();
        OcultarValor = new ArrayList<String>();
        Endereco = new ArrayList<String>();
        Categoria = new ArrayList<String>();




//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("eu_alugo");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_eu_alugo, R.id.eualugo_titulo, Titulo);



        root = inflater.inflate(R.layout.activity_eualugo, container, false);
        spinner = (Spinner) root.findViewById(R.id.spinner_eualugo);

        spinner.setOnItemSelectedListener(this);


        String[] categorias = getResources().getStringArray(R.array.categorias_aluguel);
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categorias));

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
                    Nome.clear();
                    Telefone.clear();
                    TipodeContrato.clear();
                    Valor.clear();
                    WhatsApp.clear();
                    Titulo.clear();
                    OcultarValor.clear();
                    Endereco.clear();
                    Categoria.clear();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        euAlugoModel = d.getValue(EuAlugoModel.class);
                        Nome.add(euAlugoModel.getNome());
                        Telefone.add(euAlugoModel.getTelefone());

                        Valor.add(euAlugoModel.getValor());
                        WhatsApp.add(euAlugoModel.getWhatsApp());
                        Titulo.add(euAlugoModel.getTitulo());
                        OcultarValor.add(euAlugoModel.getOcultarValor());
                        Endereco.add(euAlugoModel.getEndereco());
                        Categoria.add(euAlugoModel.getCategoria());
                    }

                    recyclerView = root.findViewById(R.id.recicler_eualugo);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_EuAlugo(root.getContext(), Nome, Telefone,  Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria, true);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        TenhoCasa = root.findViewById(R.id.btnQUEROANUNCIAR);
        ConhecoQuemTenha = root.findViewById(R.id.btnoconhecoquemtem);

        TenhoCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Faça o Login no aplicativo no Menu Lateral e preencha o cadastro", Toast.LENGTH_LONG).show();
                /*
                String url = "https://forms.gle/NPBrSTwTykxWeveQ9";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                 */
            }
        });

        ConhecoQuemTenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String texto = "Oi. No app Acessa Restinga ( https://play.google.com/store/apps/details?id=com.joel.a0800restinga ) Você pode ajudar outras pessoas a alugar sua casa ou newgócio. Acesse o App, Faça o Login no Menu Lateral e preencha o cadastro! É facil!!!";;
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                v.getContext().startActivity(sendIntent);
            }
        });
        return root;
    }
    public void onItemSelected(AdapterView<?> parent, final View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        search();

    }
    private void search() {
        String categoria = (String) spinner.getSelectedItem();

        try {
            if (categoria.equals("Todos")) {

                Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("eu_alugo")
                        .orderByChild("Categoria");

                myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);


            } else {

                Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("eu_alugo")
                        .orderByChild("Categoria")
                        .equalTo(categoria);

                myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Nome.clear();
            Telefone.clear();
            TipodeContrato.clear();
            Valor.clear();
            WhatsApp.clear();
            Titulo.clear();
            OcultarValor.clear();
            Endereco.clear();
            Categoria.clear();

            for (DataSnapshot d : snapshot.getChildren()) {
                euAlugoModel = d.getValue(EuAlugoModel.class);
                Nome.add(euAlugoModel.getNome());
                Telefone.add(euAlugoModel.getTelefone());
                Valor.add(euAlugoModel.getValor());
                WhatsApp.add(euAlugoModel.getWhatsApp());
                Titulo.add(euAlugoModel.getTitulo());
                OcultarValor.add(euAlugoModel.getOcultarValor());
                Endereco.add(euAlugoModel.getEndereco());
                Categoria.add(euAlugoModel.getCategoria());
            }

            recyclerView = root.findViewById(R.id.recicler_eualugo);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
            adapter = new RecyclerAdapter_EuAlugo(getContext(), Nome, Telefone, Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria, true);
            recyclerView.setAdapter(adapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {

    }
}