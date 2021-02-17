package com.joel.a0800restinga.ui.pedido;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Activities.MeusCadastros;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.Model.EuAlugoModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EmpresasPedido;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EuAlugo;

import java.util.ArrayList;
import java.util.List;

public class PedidoFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    DatabaseReference databaseReference;
    List<String> Empresas, Telefone;
    ArrayAdapter<String> arrayAdapter;
    EmpresaModel empresasmodel;
    //EuAlugoModel euAlugoModel;
    RecyclerAdapter_EmpresasPedido adapter;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Empresas = new ArrayList<String>();
        Telefone = new ArrayList<String>();

        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_empresas, R.id.nome_empresa, Empresas);

        root = inflater.inflate(R.layout.activity_empresa_pedido, container, false);


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
                    Empresas.clear();
                    Telefone.clear();


                    for (DataSnapshot d : snapshot.getChildren()) {
                        empresasmodel = d.getValue(EmpresaModel.class);
                        Empresas.add(empresasmodel.getNome());
                        Telefone.add(empresasmodel.getTelefone());
                    }

                    recyclerView = root.findViewById(R.id.recicler_empresaspedido);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_EmpresasPedido(root.getContext(), Empresas, Telefone);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        return root;
    }


}